package examples.group;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.queries.wall.WallCloseCommentsQuery;
import com.vk.api.sdk.queries.wall.WallCreateCommentQuery;
import examples.Utils;

import java.time.Instant;
import java.util.Properties;

public class GroupActorExecutor {
    public static void main(String[] args) {
        // initializing VK client for further interaction with API
        HttpTransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);

        // parse incoming arguments
        Properties properties = Utils.readProperties();
        GroupActor groupActor = Utils.createGroupActor(properties);
        Integer postId = Integer.valueOf(properties.getProperty("group.post.id"));

        // executing sample actions as a demonstration of thr interaction with API
        executeWallCreateCommentMethod(vk, groupActor, postId);
        executeWallCloseCommentsMethod(vk, groupActor, postId);
    }

    // тестовый запрос метода wall.createComment с использованием ключа доступа сообщества
    // в качестве тестовых данных добавим новый комментарий от имени сообщества
    // к какому-нибудь посту на стене этого же сообщества
    private static void executeWallCreateCommentMethod(VkApiClient vk, GroupActor actor, Integer postId) {
        try {
            WallCreateCommentQuery query = vk.wall().createComment(actor)
                    .ownerId(-actor.getGroupId())
                    .postId(postId)
                    .fromGroup(actor.getGroupId()) // identifier of the author of the comment
                    .message("sample message")
                    .guid(Instant.now().toString()); // unique identifier of comment to prevent repetition
            System.out.println("Received response for wall.createComment method: " + query.executeAsString());
        } catch (Exception e) {
            System.err.println("Exception during executing wall.createComment method: " + e.getLocalizedMessage());
        }
    }

    // тестовый запрос метода wall.closeComments с использованием ключа доступа сообщества
    // в качестве тестовых данных закроем комментарии под одним из постов, сделанных от имени сообщества
    private static void executeWallCloseCommentsMethod(VkApiClient vk, GroupActor actor, Integer postId) {
        try {
            WallCloseCommentsQuery query = vk.wall().closeComments(actor)
                    .ownerId(-actor.getGroupId())
                    .postId(postId);
            System.out.println("Received response for wall.closeComments method: " + query.executeAsString());
        } catch (Exception e) {
            System.err.println("Exception during executing wall.closeComments method: " + e.getLocalizedMessage());
        }
    }
}
