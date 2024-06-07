package examples.bot;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.events.longpoll.GroupLongPollApi;
import com.vk.api.sdk.objects.callback.MessageEdit;
import com.vk.api.sdk.objects.callback.MessageNew;

import java.util.function.BiConsumer;

// sample class with overriding handling of LongPoll API events
// now it contains only sample simple logic, feel free to create your own implementations
public class LongPollHandler extends GroupLongPollApi {

    private final BiConsumer<Long, String> completion;

    protected LongPollHandler(VkApiClient client, GroupActor actor, int waitTime, BiConsumer<Long, String> completion) {
        super(client, actor, waitTime);
        this.completion = completion;
    }

    @Override
    public void messageNew(Integer groupId, MessageNew message) {
        this.completion.accept(message.getObject().getMessage().getFromId(), message.toPrettyString());
    }

    @Override
    public void messageEdit(Integer groupId, MessageEdit message) {
        this.completion.accept(message.getObject().getFromId(), message.toPrettyString());
    }
}
