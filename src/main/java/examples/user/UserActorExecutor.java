package examples.user;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.likes.Type;
import com.vk.api.sdk.objects.wall.responses.PostResponse;
import com.vk.api.sdk.queries.likes.LikesAddQuery;
import com.vk.api.sdk.queries.wall.WallPostQuery;
import examples.Utils;

import java.util.Properties;

public class UserActorExecutor {

    public static void main(String[] args) {
        // initializing VK client for further interaction with API
        HttpTransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);

        // parse incoming arguments
        Properties properties = Utils.readProperties();
        UserActor userActor = Utils.createUserActor(properties);
        Long groupId = Long.valueOf(properties.getProperty("group.id"));

        // executing sample actions as a demonstration of thr interaction with API
        Integer newPostId = executeWallPostMethod(vk, userActor, groupId);
        executeLikesAddMethod(vk, userActor, groupId, newPostId);
    }

    // тестовый запрос метода wall.post с использованием ключа доступа пользователя
    // в качестве тестовых данных добавим небольшой пост на стене группы
    private static Integer executeWallPostMethod(VkApiClient vk, UserActor actor, Long groupId) {
        try {
            WallPostQuery query = vk.wall().post(actor)
                    .ownerId(-groupId)
                    .message("sample post from user");
            PostResponse response = query.execute();
            System.out.println("Received response for wall.post method: " + response);
            return response.getPostId();
        } catch (Exception e) {
            System.err.println("Exception during executing wall.post method: " + e.getLocalizedMessage());
        }
        return 0;
    }

    // тестовый запрос метода likes.add с использованием ключа доступа пользователя
    // в качестве тестовых данных поставим лайк на какой-нибудь пост на стене группы
    private static void executeLikesAddMethod(VkApiClient vk, UserActor actor, Long groupId, Integer postId) {
        try {
            LikesAddQuery query = vk.likes().add(actor)
                    .type(Type.POST)
                    .ownerId(-groupId)
                    .itemId(postId)
                    .accessKey(actor.getAccessToken());
            System.out.println("Received response for likes.add method: " + query.executeAsString());
        } catch (Exception e) {
            System.err.println("Exception during executing likes.add method: " + e.getLocalizedMessage());
        }
    }
}