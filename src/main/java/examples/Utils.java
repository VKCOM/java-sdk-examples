package examples;

import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.client.actors.UserActor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {
    public static GroupActor createGroupActor(Properties properties) {
        String groupId = properties.getProperty("group.id");
        String accessToken = properties.getProperty("group.token");
        return new GroupActor(Long.parseLong(groupId), accessToken);
    }

    public static ServiceActor createServiceActor(Properties properties) {
        Integer appId = Integer.valueOf(properties.getProperty("app.id"));
        String appAccessToken = properties.getProperty("app.access.token");
        return new ServiceActor(appId, appAccessToken);
    }

    public static UserActor createUserActor(Properties properties) {
        Long userId = Long.valueOf(properties.getProperty("user.id"));
        String userToken = properties.getProperty("user.token");
        return new UserActor(userId, userToken);
    }

    public static Properties readProperties() {
        try (InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                throw new FileNotFoundException("property file config.properties not found in the classpath");
            }
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Incorrect properties file");
        }
    }
}
