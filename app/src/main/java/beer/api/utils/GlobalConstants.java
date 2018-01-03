package beer.api.utils;

import android.content.Context;
import android.os.Environment;

public class GlobalConstants {

    public static final String SERVER_SSL_URL = "https://api.punkapi.com";

    public static String getCacheFolder(Context context) {
        return context.getCacheDir().getPath();
    }

}
