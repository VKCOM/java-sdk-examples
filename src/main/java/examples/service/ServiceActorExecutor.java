package examples.service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.base.NameCase;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.wall.GetFilter;
import com.vk.api.sdk.queries.users.UsersGetQuery;
import com.vk.api.sdk.queries.wall.WallGetQuery;
import examples.Utils;

import java.util.Properties;

public class ServiceActorExecutor {
    public static void main(String[] args) {
        // initializing VK client for further interaction with API
        HttpTransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);

        // parse incoming arguments
        Properties properties = Utils.readProperties();
        ServiceActor serviceActor = Utils.createServiceActor(properties);
        Long userId = Long.valueOf(properties.getProperty("service.user.id"));

        // executing sample actions as a demonstration of thr interaction with API
        executeWallGetMethod(vk, serviceActor, userId);
        executeUsersGetMethod(vk, serviceActor, properties.getProperty("service.user.id"));
    }

    // тестовый запрос метода wall.get с использованием сервисного ключа доступа
    // в качестве тестовых данных получим недавние 10 постов со страницы пользователя
    private static void executeWallGetMethod(VkApiClient vk, ServiceActor actor, Long userId) {
        try {
            WallGetQuery query = vk.wall().get(actor)
                    .unsafeParam("owner_id", userId)
                    .count(10) // не более 10 записей в ответе
                    .filter(GetFilter.ALL); // получаем все записи
            System.out.println("Received response for wall.get method: " + query.executeAsString());
        } catch (Exception e) {
            System.err.println("Exception during executing wall.get method: " + e.getLocalizedMessage());
        }
    }

    // тестовый запрос метода users.get с использованием сервисного ключа доступа
    // в качестве тестовых данных получим данные с профиля пользователя
    private static void executeUsersGetMethod(VkApiClient vk, ServiceActor actor, String userId) {
        try {
            UsersGetQuery query = vk.users().get(actor)
                    .userIds(userId)
                    .fields( // тестовый случайный набор полей
                            Fields.ABOUT,
                            Fields.SITE,
                            Fields.INTERESTS,
                            Fields.CITY
                    )
                    .nameCase(NameCase.INSTRUMENTAL); // также укажем творительный падеж в имени и фамилиии пользователя
            System.out.println("Received response for users.get method: " + query.executeAsString());
        } catch (Exception e) {
            System.err.println("Exception during executing users.get method: " + e.getLocalizedMessage());
        }
    }
}
