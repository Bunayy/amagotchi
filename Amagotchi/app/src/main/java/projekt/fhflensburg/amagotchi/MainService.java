package projekt.fhflensburg.amagotchi;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Howie on 05.10.2015.
 */
public class MainService extends Service
{
    private static final String LOG_TAG = "MainService";
    private IBinder mBinder = new MyBinder();

    static Handler handler;

    //Daten
    static Amagotchi ama = null;
    static Boolean run = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(LOG_TAG, "in onCreate");

        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                tick();
            }

        };

        new Thread(new Runnable(){
            public void run() {

                while(true)
                {
                    try {
                        Thread.sleep(1000);
                        handler.sendEmptyMessage(0);

                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();

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

    public static void setAma(Amagotchi pAma)
    {
        ama = pAma;
    }

    public static Amagotchi getAma()
    {
        return ama;
    }

    public static void run()
    {
        run = true;
    }

    public static void stop()
    {
        run = false;
    }

    public class MyBinder extends Binder {
        MainService getService() {
            return MainService.this;
        }
    }

    private void tick()
    {
        //Log.v(LOG_TAG, "tick " + run);

        //Initialisieren mit Grundwerten
        double healthThisPeriod = 0.043;
        double repletionThisPeriod = -0.17;
        double sleepThisPeriod = -0.1;
        double motivationThisPeriod = 0.0;
        double happinessThisPeriod = 0.0;
        double fitnessThisPeriod = -0.12;
        double attentionThisPeriod = -0.28;

        if(ama != null && run)
        {
            //SChalfen, ShitLiegtRum
            if(ama.getIsAsleep())
            {
                sleepThisPeriod += 0.2;
            }

            if(ama.getFeces())
            {

            }





            /*ama.setMotivation(ama.getMotivation() + 0.1);
            //Log.v(LOG_TAG, "MotIs: " + ama.getMotivation());
            //Log.v(LOG_TAG, "Saving");*/

            Sys.saveGame(ama, this);
            MainActivity.instance.updateInterface();
        }
    }


    //INPUT
    public void doFeedBurger()
    {
        ama.setLevel(ama.getLevel() + 1);
    }

    public void doFeedSweets()
    {

    }

    public void doFeedHealthy()
    {

    }



    //OUTPUT


}
