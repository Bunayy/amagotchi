package projekt.fhflensburg.amagotchi;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Howie on 05.10.2015.
 */
public class MainService extends Service
{
    private static final String LOG_TAG = "MainService";
    private IBinder mBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(LOG_TAG, "in onCreate");

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(LOG_TAG, "in onBind");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v(LOG_TAG, "in onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(LOG_TAG, "in onUnbind");
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "in onDestroy");

    }

    public String getTest() {
        return "lol";
    }

    public class MyBinder extends Binder {
        MainService getService() {
            return MainService.this;
        }
    }
}
