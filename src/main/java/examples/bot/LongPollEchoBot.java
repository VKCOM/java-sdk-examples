package examples.bot;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;

import java.util.Objects;
import java.util.Properties;
import java.util.function.BiConsumer;

import examples.Utils;

public class LongPollEchoBot {
    public static void main(String[] args) throws ClientException, ApiException {
        // initializing VK client for further interaction with API
        HttpTransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);

        // parse incoming arguments
        Properties properties = Utils.readProperties();
        GroupActor groupActor = Utils.createGroupActor(properties);
        Long echoId = Long.valueOf(properties.getProperty("bot.echo.user.id"));

        // setting default necessary permissions for the proper work of echo bot
        if (!vk.groups().getLongPollSettings(groupActor, groupActor.getGroupId()).execute().getIsEnabled()) {
            vk.groups().setLongPollSettings(groupActor, groupActor.getGroupId())
                    .enabled(true)
                    .messageNew(true)
                    .messageEdit(true)
                    .execute();
        }

        // initializing the completion block for custom handling of incoming events
        BiConsumer<Long, String> completion = (fromId, eventDescription) -> {
            if (!Objects.equals(fromId, Math.negateExact(groupActor.getGroupId()))) {
                try {
                    // for example, we'll send as a response textual repsentation of incoming event
                    String result = vk.messages().sendUserIds(groupActor)
                            .userId(echoId)
                            .randomId(0)
                            .message(eventDescription)
                            .executeAsString();
                    System.out.println("Callback event successfully parsed.\nEvent: "+ eventDescription + "\nEcho result: " + result);
                } catch (ClientException e) {
                    System.err.println("Error during echoing callback event.\nMessage details: " + eventDescription + "\nError log: " + e.getLocalizedMessage());
                }
            } else {
                System.out.println("Self callback event found. Will skip it due to escape spam in dialog.");
            }
        };

        // initalizing Long Poll handler and starting to listen to the new events
        LongPollHandler handler = new LongPollHandler(vk, groupActor, 1, completion);
        handler.run();
    }
}
