package beer.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Alexei on 1/3/2018.
 */
@RunWith(AndroidJUnit4.class)
public class ReceiverTest extends BroadcastReceiver {
    private static final String TAG = "ReceiverTest";

    @Override
    public void onReceive(Context ctx, Intent i)
    {
        //Context context = InstrumentationRegistry.getTargetContext();
        isNetworkAvailable();
    }

    @Test
    public void isNetworkAvailable() {
        boolean isConnected=false;
        Context context = InstrumentationRegistry.getTargetContext();

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int j = 0; j < info.length; j++) {
                    if (info[j].getState() == NetworkInfo.State.CONNECTED) {
                        if (!isConnected) {
                            Log.v(TAG, "Now you are connected to Internet!");
                            isConnected = true;
                        }
                    }
                }
            }
        }
        Log.v(TAG, "You are not connected to Internet!");

        assertTrue(isConnected);
        //return isConnected;
    }

}
