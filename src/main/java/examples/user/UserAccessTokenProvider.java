package examples.user;

import examples.Utils;

import java.awt.*;
import java.net.URL;
import java.time.Instant;
import java.util.Properties;

public class UserAccessTokenProvider {

    // default redirect uri
    // feel free to configure it by setting your own value
    //
    // for example: you can set the link to your specific endpoint,
    // which is listened by your server
    // thus, you can create your custom redirects with custom logic
    private static final String REDIRECT_URI = "https://oauth.vk.com/blank.html";

    // setting the bitmask to access various sections
    // more: https://dev.vk.com/reference/access-rights
    private static final int ACCESS_RIGHTS =
            (1 << 0) //notify
            + (1 << 1) // friends
            + (1 << 2) // photos
            + (1 << 3) // audio
            + (1 << 4) // video
            + (1 << 6) // stories
            + (1 << 7) // pages
            + (1 << 8) // menu
            + (1 << 10) // status
            + (1 << 11) // notes
//            + (1 << 12) // messages --- this section is unavailable for our app
            + (1 << 13) // wall
            + (1 << 15) // ads
            + (1 << 16) // offline
            + (1 << 17) // docs
            + (1 << 18) // groups
            + (1 << 19) // notifications
            + (1 << 20) // stats
            + (1 << 22) // email
            + (1 << 27) // market
            + (1 << 28) // phone_number
    ;

    public static void main(String[] args) {
        Properties properties = Utils.readProperties();
        Integer appId = Integer.valueOf(properties.getProperty("app.id"));

        String urlPath = "https://oauth.vk.com/authorize?"
                + "client_id=" + appId
                + "&redirect_uri=" + REDIRECT_URI
                + "&display=page"
                + "&scope=" + ACCESS_RIGHTS
                + "&response_type=token"
                + "&state=" + Instant.now()
                + "&revoke=1"
        ;

        try {
            URL url = new URL(urlPath);
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(url.toURI()); //save token to user_token field in config.properties
            }
        } catch (Exception e) {
            System.err.println("Error during getting access examples.user token: " + e.getLocalizedMessage());
        }
    }
}
