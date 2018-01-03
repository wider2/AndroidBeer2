package beer.api;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

import beer.api.utils.SharedStatesMap;
import beer.api.utils.Utilities;

/**
 * Created by Aleksei Jegorov on 02/01/2018.
 */
public class BeerApiApplication extends Application {

    private static final String TAG = "BEERAPI";
    private static BeerApiApplication sApp;
    public BeerDatabase beerDatabase;
    public static Thread.UncaughtExceptionHandler androidDefaultUEH;
    SharedStatesMap mSharedStates;

    public BeerDatabase getInstanceDatabase() {
        return beerDatabase;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        //initialize Singletons to store value used in fragments
        mSharedStates = SharedStatesMap.getInstance();
        mSharedStates.setKeyInt("favoriteDisplay", 0);
        mSharedStates.setKeyInt("detailsDisplay", 0);
        mSharedStates.setKeyInt("productId", 0);
        mSharedStates.setKeyInt("sortFlag", 0);
        mSharedStates.setKeyInt("sortByName", 0);
        mSharedStates.setKeyInt("sortByAbv", 0);
        mSharedStates.setKeyInt("sortByIbu", 0);
        mSharedStates.setKeyInt("sortByEbc", 0);

        sApp = this;

        beerDatabase = Room.databaseBuilder(getApplicationContext(),
                BeerDatabase.class, "beer-db").build();


        //we can save exception to SD card. In production version it could be send to Fabric.io or firebase
        androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                String report = "";
                StackTraceElement[] arr = paramThrowable.getStackTrace();
                report = paramThrowable.toString() + "\r\n";
                report += "--------- Stack trace ---------\r\n" + paramThread.toString();
                for (int i = 0; i < arr.length; i++) {
                    report += "    " + arr[i].toString() + "\r\n";
                }

                Throwable cause = paramThrowable.getCause();
                if (cause != null) {
                    report += "\n------------ Cause ------------\r\n";
                    report += cause.toString() + "\r\n";
                    arr = cause.getStackTrace();
                    for (int i = 0; i < arr.length; i++) {
                        report += "    " + arr[i].toString() + "\r\n";
                    }
                }

                String rep = "";
                rep += "Time: " + Utilities.GetTimeNow() + "\r\n";
                rep += "Message: " + paramThrowable.getMessage() + "\r\n";

                Utilities.writeFile("CrashReport.txt", rep + report + "\r\n", false, getApplicationContext(), "");

                androidDefaultUEH.uncaughtException(paramThread, paramThrowable);
            }
        });
    }

    public static BeerApiApplication getApp() {
        return sApp;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        onTrimMemory(TRIM_MEMORY_COMPLETE);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            Log.d(TAG, "Application not visible anymore");
        } else if (level == ComponentCallbacks2.TRIM_MEMORY_COMPLETE) {
            Log.d(TAG, "Application is going to be killed");
        }
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}