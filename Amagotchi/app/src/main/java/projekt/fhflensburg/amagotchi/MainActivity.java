package projekt.fhflensburg.amagotchi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Debug;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    private ViewFlipper flipper;
    Amagotchi ama;

    public static SoundService mSoundService;
    boolean mSoundServiceBounded = false;

    //Bei Start der App
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flipper = (ViewFlipper) findViewById(R.id.flipper);


        //Log.v(LOG_TAG, "---BEFORE------------------");
        ama = new Amagotchi("Hugo1337", "1", this.getBaseContext());
        /*Log.v(LOG_TAG, "---START---");
        Log.v(LOG_TAG, ama.getSaveString());
        Log.v(LOG_TAG, "---END---");
        Log.v(LOG_TAG, " ");

        Log.v(LOG_TAG, "---SAVING---");*/
        if(Amagotchi.getState() != null)//Spiel gestartet
        {
            Sys.saveGame(Amagotchi.getState(), this);
        }
        //Log.v(LOG_TAG, "---END---");
        //Log.v(LOG_TAG, " ");



        Amagotchi amaLoad = Sys.loadGame(this);

        //Log.v(LOG_TAG, "---LOADING---");
        Log.v(LOG_TAG, amaLoad.getName());
        /*Log.v(LOG_TAG, "---END---");
        Log.v(LOG_TAG, " ");*/

        Intent intent = new Intent(MainActivity.this,SoundService.class);

        Log.v(LOG_TAG, "startServiceBtn onClick()");
        bindService(intent, mSoundServiceConnection, Context.BIND_AUTO_CREATE);

    }

    //Beim Verlust des Focus
    public void onStop() {
        Log.v(LOG_TAG, "onStop");

        //saveOnExit(count);

        /*if(Amagotchi.getState() != null)//Spiel gestartet
        {
            Sys.saveGame(Amagotchi.getState(), this);
        }

        super.onStop();

        finish();
        System.exit(0); //App beenden*/

        super.onStop();
    }

    //Beim Stopp der App
    public void onDestroy() {
        Log.v(LOG_TAG, "onDestroy");

        //saveOnExit(count);

        if(Amagotchi.getState() != null)//Spiel gestartet
        {
            Sys.saveGame(Amagotchi.getState(), this);
        }

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onNewGame(View view)
    {
        mSoundService.playSounds("selection");
        Log.d(LOG_TAG, "onNewGame()");
        flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.gameView)));
    }

    public void onContGame(View view)
    {
        Log.d(LOG_TAG, "onContGame()");
    }

    public void onSettings(View view)
    {
        Log.d(LOG_TAG, "onSettings():");
        flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.settingsView)));
    }

    public void onGameHistory(View view)
    {

    }
    public void onFeedingPressed(View v)
    {
        Log.d(LOG_TAG, "Gib Gib GIb !");
    }

    public void onLightPressed(View v)
    {
        Log.d(LOG_TAG, "Licht An/Aus");

    }

    public void onCleaningPressed(View v)
    {
        Log.v("test", "Reinigen");
    }

    public void onStatsPressed(View v)
    {
        Log.d(LOG_TAG, "Hier sind deine Stats");
    }

    public void onMedicinePressed(View v)
    {
        //Log.d(LOG_TAG, "Ih bah Medizin !");
        ama.healAmagotchi();
    }

    public void onDisciplinePressed(View v)
    {
        Log.d(LOG_TAG, "Böses amagee");
    }

    public void onMinigamesPressed(View v)
    {
        Log.d(LOG_TAG, "Juhu Spiele!");
        flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.minigamesChooserView)));
    }

    public void onWalkingPressed(View v)
    {
        Log.d(LOG_TAG, "Wenns denn sein muss !");
    }


    public void onHigherOrLowerStarted(View v)
    {
        Log.d(LOG_TAG, "onHigherOrLowerStarted()");
    }

    public void onLeftOrRightStarted(View v)
    {
        Log.d(LOG_TAG, "onLeftOrRightStarted()");
    }

    public void onSumUpGameStarted(View v)
    {
        Log.d(LOG_TAG, "onSumUpGameStarted()");
        SumUpGame sum = new SumUpGame(this);
        flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.sumUpGameView)));

    }

    public void onSoundCheck(View v)
    {
        Log.d(LOG_TAG, "onSoundCheck()");
        if (mSoundService != null) mSoundService.playSounds("happy");

    }

    private ServiceConnection mSoundServiceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v(LOG_TAG, "onServiceConnected");
            SoundService.SoundBinder soundBinder = (SoundService.SoundBinder)service;
            mSoundService = soundBinder.getService();
            mSoundServiceBounded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v(LOG_TAG, "onServiceDisconnected");
            mSoundServiceBounded = false;
            mSoundService = null;
        }
    };

}
