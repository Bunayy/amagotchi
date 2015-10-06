package projekt.fhflensburg.amagotchi;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Howie on 05.10.2015.
 */
public class MainService extends Service
{
    private static final String LOG_TAG = "MainService";
    private IBinder mBinder = new MyBinder();
    Random r = new Random();

    static Handler handler;

    //Daten
    static Amagotchi ama = null;
    static Boolean run = false;

    int loopCount = 0;

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

        //Alte Werte merken
        double oldAttention = 0.0;

        if(ama != null && run)
        {
            oldAttention = ama.getAttention();

            if(ama.getIsDead() == false)
            {
                if (ama.getLevel() == 0) {
                    ////////////////////////////////////////
                    // Hatching
                    ////////////////////////////////////////

                    if (ama.getTimeToHatch() > 1) {
                        ama.setTimeToHatch(ama.getTimeToHatch() - 1);
                    } else {
                        ama.setLevel(1);
                    }
                } else {
                    ////////////////////////////////////////
                    // This Period
                    ////////////////////////////////////////

                    //Feces Countdown
                    if (ama.getFecesCountdown() > 1) {
                        ama.setFecesCountdown(ama.getFecesCountdown() - 1);
                    } else if (ama.getFecesCountdown() == 1) {
                        ama.setIsSickInfection(true);
                        healthThisPeriod -= 30;
                        motivationThisPeriod -= 15;
                        ama.setFecesCountdown(0);
                    }

                    //Cure from overeating
                    if (ama.getIsSickOveraeting() && ama.getRepletion() <= 100) {
                        ama.setIsSickOveraeting(false);
                    }

                    //Schläft
                    if (ama.getIsAsleep()) {
                        sleepThisPeriod += 0.2;
                    }

                    //Fäkalien liegen rum
                    if (ama.getFeces()) {
                        happinessThisPeriod -= 0.085;
                    }

                    //Überfressen
                    if (ama.getIsSickOveraeting()) {
                        healthThisPeriod -= 0.085;
                        happinessThisPeriod -= 0.25;
                    }

                    //Infektion
                    if (ama.getIsSickInfection()) {
                        healthThisPeriod -= 0.17;
                        motivationThisPeriod -= 0.125;
                        happinessThisPeriod -= 0.17;
                    }

                    //Sättigung
                    if (ama.getRepletion() == 0) {
                        healthThisPeriod -= 0.34;
                        motivationThisPeriod -= 0.125;
                        happinessThisPeriod -= 0.17;
                        attentionThisPeriod -= 25;
                    } else if (ama.getRepletion() <= 20) {
                        healthThisPeriod -= 0.17;
                        motivationThisPeriod -= 0.125;
                        happinessThisPeriod -= 0.17;
                    } else if (ama.getRepletion() <= 40) {
                        motivationThisPeriod -= 0.125;
                    }

                    //Schlaf
                    if (ama.getSleep() == 0) {
                        healthThisPeriod -= 0.34;
                        motivationThisPeriod -= 25;
                        happinessThisPeriod -= 0.17;
                    } else if (ama.getSleep() <= 10) {
                        healthThisPeriod -= 0.17;
                        motivationThisPeriod -= 25;
                        happinessThisPeriod -= 0.17;
                    } else if (ama.getSleep() <= 20) {
                        motivationThisPeriod -= 25;
                        happinessThisPeriod -= 0.17;
                    }

                    //Fitness
                    if (ama.getFitness() <= 20) {
                        healthThisPeriod -= 0.021;
                    } else if (ama.getFitness() >= 80) {
                        healthThisPeriod -= 0.042;
                    }

                    ////////////////////////////////////////
                    // Berechnung
                    ////////////////////////////////////////

                    ama.setHealth(ama.getHealth() + healthThisPeriod);
                    ama.setRepletion(ama.getRepletion() + repletionThisPeriod);
                    ama.setSleep(ama.getSleep() + sleepThisPeriod);
                    ama.setMotivation(ama.getMotivation() + motivationThisPeriod);
                    ama.setHappiness(ama.getHappiness() + happinessThisPeriod);
                    ama.setFitness(ama.getFitness() + fitnessThisPeriod);
                    ama.setAttention(ama.getAttention() + attentionThisPeriod);


                    //Aufmerksamkeit

                    //loopCount


                    //Gamble Feces
                    if (ama.getAge() % 60 == 0 && !ama.getFeces()) {
                        Log.v(LOG_TAG, "PoopGamble");

                        if (r.nextInt(4) == 2) {
                            ama.setFeces(true);
                            ama.setFecesCountdown(180);
                        }
                    }

                    //Gamble Overeating
                    if (ama.getRepletion() >= 120) {
                        ama.setIsSickOveraeting(true);
                    } else if (ama.getRepletion() >= 110) {
                        if (r.nextInt(10) > 7) {
                            ama.setIsSickOveraeting(true);
                        }
                    } else if (ama.getRepletion() >= 105) {
                        if (r.nextInt(4) == 1) {
                            ama.setIsSickOveraeting(true);
                        }
                    }

                }
                //Altern
                ama.setAge(ama.getAge() + 1);
            }

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
