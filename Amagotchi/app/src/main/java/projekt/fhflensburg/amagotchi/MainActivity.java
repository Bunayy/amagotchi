package projekt.fhflensburg.amagotchi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.text.DecimalFormat;

import projekt.fhflensburg.amagotchi.MainService.MyBinder;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";
    public static MainActivity instance;


    //Refrences
    public static ViewFlipper flipper;
    private TextView textViewStatsMain;

    public static GameView gv;
    public static SumUpGameView sugv;
    public static LeftOrRightGameView lorgv;


    //Main Service
    MainService mainService;
    boolean mainServiceBound = false;

    //Sound Service
    public static SoundService mSoundService;
    boolean mSoundServiceBounded = false;

    //Zuletzt gewähltes Amagotchi
    private boolean amagotchiClicked = false;


    public static LeftOrRightGame lorGame;
    //Bei Start der App
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.instance = this;

        flipper = (ViewFlipper) findViewById(R.id.flipper);

        //Refrences
        //textViewStatsMain = (TextView) findViewById(R.id.textViewStatsMain);


/*
        //Log.v(LOG_TAG, "---BEFORE------------------");
        ama = new Amagotchi("Hugo1337", "1", this.getBaseContext());
        Log.v(LOG_TAG, "---START---");
        Log.v(LOG_TAG, ama.getSaveString());
        Log.v(LOG_TAG, "---END---");
        Log.v(LOG_TAG, " ");

        Log.v(LOG_TAG, "---SAVING---");
        if(Amagotchi.getState() != null)//Spiel gestartet
        {
            Sys.saveGame(Amagotchi.getState(), this);
        }
        //Log.v(LOG_TAG, "---END---");
        //Log.v(LOG_TAG, " ");



        Amagotchi amaLoad = Sys.loadGame(this);

        //Log.v(LOG_TAG, "---LOADING---");
        Log.v(LOG_TAG, amaLoad.getName());
        Log.v(LOG_TAG, "---END---");
        Log.v(LOG_TAG, " ");*/
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Main Service
        Intent intentMain = new Intent(this, MainService.class);
        startService(intentMain);
        bindService(intentMain, mainServiceConnection, Context.BIND_AUTO_CREATE);

        //Sound Service
        Intent intent = new Intent(MainActivity.this,SoundService.class);
        Log.v(LOG_TAG, "startServiceBtn onClick()");
        bindService(intent, mSoundServiceConnection, Context.BIND_AUTO_CREATE);

        gv = (GameView)findViewById(R.id.canvasContainer);
        sugv = (SumUpGameView)findViewById(R.id.sumUpGameViewAmagotchi);
        lorgv = (LeftOrRightGameView)findViewById(R.id.lorGameViewAmagee);

        //Service Test
        /*Log.v(LOG_TAG, "---DONE---");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Log.v(LOG_TAG, "---3s---");
                if (mainServiceBound)
                {
                    //Log.v(LOG_TAG, "---BOUND---" + mainService.getTest());
                }
            }
        }, 3000);
*/

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
        flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.newGameView)));
    }

    public void onContGame(View view)
    {
        Log.d(LOG_TAG, "onContGame()");

        //Load
        Amagotchi ama = Sys.loadGame(this);
        Amagotchi.setInstance(ama);
        MainService.setAma(ama);
        MainService.run();
        updateInterface();
        flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.gameView)));
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
        Log.d(LOG_TAG, "onFeedingPressed");
        flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.foodChooserView)));
    }

    public void onFeedBurger(View v)
    {
        Log.d(LOG_TAG, "onFeedBurger");
        if (mainServiceBound)
        {
            mainService.doFeedBurger();
        }
        flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.gameView)));
    }
    public void onFeedSweets(View v)
    {
        Log.d(LOG_TAG, "onFeedSweets");
        if (mainServiceBound)
        {
            mainService.doFeedSweets();
        }
        flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.gameView)));
    }
    public void onFeedHealthy(View v)
    {
        Log.d(LOG_TAG, "onFeedHealthy");
        if (mainServiceBound)
        {
            mainService.doFeedHealthy();
        }
        flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.gameView)));
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
        Log.d(LOG_TAG, "onStatsPressed");
        flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.statsView)));

    }

    public void onStatsBack(View v)
    {
        Log.d(LOG_TAG, "onStatsBack");
        flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.gameView)));

    }
    public void onMedicinePressed(View v)
    {
        //Log.d(LOG_TAG, "Ih bah Medizin !");
        //ama.healAmagotchi();
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

    public void onWalkingPressed(View v) {
        Log.d(LOG_TAG, "Wenns denn sein muss !");
    }


    public void onHigherOrLowerStarted(View v)
    {
        Log.d(LOG_TAG, "onHigherOrLowerStarted()");
    }

    public void onLeftOrRightStarted(View v)
    {
        Log.d(LOG_TAG, "onLeftOrRightStarted()");
        lorGame = new LeftOrRightGame(this);

        sugv.setVisibility(View.INVISIBLE);
        gv.setVisibility(View.INVISIBLE);
        lorgv.setVisibility(View.VISIBLE);

        flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.leftOrRightGameView)));
    }

    public void onSumUpGameStarted(View v)
    {
        Log.d(LOG_TAG, "onSumUpGameStarted()");
        SumUpGame sum = new SumUpGame(this);
        flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.sumUpGameView)));

        sugv.setVisibility(View.VISIBLE);
        gv.setVisibility(View.INVISIBLE);
        lorgv.setVisibility(View.INVISIBLE);

    }

    public void onAmagotchiClicked(View v)
    {
        amagotchiClicked = true;
        ImageButton clickedAmagotchi = (ImageButton) v;
        v.setBackgroundColor(Color.GRAY);
    }
    public void onBeginNewGame(View v)
    {
        TextView nameField = (TextView)findViewById(R.id.amagotchi_name_edittext);
        if(nameField.getText().length() == 0)
            Toast.makeText(getApplicationContext(), "Bitte geben Sie einen Namen an.", Toast.LENGTH_SHORT).show();
        else
        {
            if (amagotchiClicked)
            {
                String name = nameField.getText().toString();
                String type = "1";

                mSoundService.playSounds("selection");
                Log.d(LOG_TAG, "onNewGame()");

                //Create
                Amagotchi ama = new Amagotchi(name, type, this);
                Amagotchi.setInstance(ama);
                MainService.setAma(ama);
                MainService.run();

                ((GameView)findViewById(R.id.canvasContainer)).init();

                sugv.setVisibility(View.INVISIBLE);
                gv.setVisibility(View.VISIBLE);
                lorgv.setVisibility(View.INVISIBLE);

                updateInterface();
                flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.gameView)));
            }
            else
                Toast.makeText(getApplicationContext(), "Bitte wählen Sie ein Amagotchi aus.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSoundCheck(View v)
    {
        Log.d(LOG_TAG, "onSoundCheck()");
        if (mSoundService != null) mSoundService.playSounds("happy");

    }

    @Override
    public void onBackPressed()
    {
        if (Amagotchi.getState() != null)
        {
            Amagotchi outAma = MainService.getAma();
            sugv.setVisibility(View.INVISIBLE);
            gv.setVisibility(View.VISIBLE);
            lorgv.setVisibility(View.INVISIBLE);
            if (outAma.getLevel() != 0)
                flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.gameView)));
            else
            {
                //flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.gameView)));
            }
        }
    }

    private ServiceConnection mainServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v(LOG_TAG, "onServiceDisconnected Main");
            mainServiceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v(LOG_TAG, "onServiceConnected Main");
            MyBinder myBinder = (MyBinder) service;
            mainService = myBinder.getService();
            mainServiceBound = true;
        }
    };

    private ServiceConnection mSoundServiceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v(LOG_TAG, "onServiceConnected Sound");
            SoundService.SoundBinder soundBinder = (SoundService.SoundBinder)service;
            mSoundService = soundBinder.getService();
            mSoundServiceBounded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v(LOG_TAG, "onServiceDisconnected Sound");
            mSoundServiceBounded = false;
            mSoundService = null;
        }
    };

    public void updateInterface()
    {
        Amagotchi outAma = MainService.getAma();
        String temp = "";

        /*temp += "Name: " + outAma.getName() + "\n";
        temp += "Type: " + outAma.getType() + "\n";
        temp += "Mutation: " + outAma.getMutation() + "\n\n";

        temp += "Level: " + outAma.getLevel() + "\n";
        temp += "Development Points: " + outAma.getDevelopmentPoints() + "\n";
        temp += "Time To Hatch: " + outAma.getTimeToHatch() + "\n\n";

        temp += "Is Sick Infection: " + outAma.getIsSickInfection() + "\n";
        temp += "Is Sick Overeating: " + outAma.getIsSickOveraeting() + "\n";
        temp += "feces: " + outAma.getFeces() + "\n";
        temp += "feces Countdown: " + outAma.getFecesCountdown() + "\n";
        temp += "Is Dead: " + outAma.getIsDead() + "\n";
        temp += "Is Asleep: " + outAma.getIsAsleep() + "\n\n";*/

        /*temp += "health: " + outAma.getHealth() + "\n";
        temp += "repletion: " + outAma.getRepletion() + "\n";
        temp += "sleep: " + outAma.getSleep() + "\n";
        temp += "motivation: " + outAma.getMotivation() + "\n";
        temp += "happiness: " + outAma.getHappiness() + "\n";
        temp += "fitness: " + outAma.getFitness() + "\n";
        temp += "attention: " + outAma.getAttention() + "\n";
        temp += "age: " + outAma.getAge() + "\n";
        temp += "weight: " + outAma.getWeight() + "\n";*/

        /*temp += "health: " + String.format("%.1f", outAma.getHealth()) + "\n";
        temp += "repletion: " + String.format("%.1f", outAma.getRepletion()) + "\n";
        temp += "sleep: " + String.format("%.1f", outAma.getSleep()) + "\n";
        temp += "motivation: " + String.format("%.1f", outAma.getMotivation()) + "\n";
        temp += "happiness: " + String.format("%.1f", outAma.getHappiness()) + "\n";
        temp += "fitness: " + String.format("%.1f", outAma.getFitness()) + "\n";
        temp += "attention: " + String.format("%.1f", outAma.getAttention()) + "\n";
        temp += "age: " + outAma.getAge() + "\n";
        temp += "weight: " + String.format("%.1f", outAma.getWeight()) + "\n";

        textViewStatsMain.setText(temp);*/

        ProgressBar healthBar = (ProgressBar)findViewById(R.id.healthBar);
        healthBar.setVisibility(View.VISIBLE);
        healthBar.setMax(100);

        healthBar.setProgress(Math.min((int) outAma.getHealth(), 100));

        ProgressBar repletionBar = (ProgressBar)findViewById(R.id.repletionBar);
        repletionBar.setVisibility(View.VISIBLE);
        repletionBar.setMax(100);

        repletionBar.setProgress((int) outAma.getRepletion());

        ProgressBar sleepBar = (ProgressBar)findViewById(R.id.sleepBar);
        sleepBar.setVisibility(View.VISIBLE);
        sleepBar.setMax(100);

        sleepBar.setProgress((int) outAma.getSleep());

        ProgressBar motivationBar = (ProgressBar)findViewById(R.id.motivationBar);
        motivationBar.setVisibility(View.VISIBLE);
        motivationBar.setMax(100);

        motivationBar.setProgress((int) outAma.getMotivation());

        ProgressBar happinessBar = (ProgressBar)findViewById(R.id.happinessBar);
        happinessBar.setVisibility(View.VISIBLE);
        happinessBar.setMax(100);

        happinessBar.setProgress((int) outAma.getHappiness());

        ProgressBar fitnessBar = (ProgressBar)findViewById(R.id.fitnessBar);
        fitnessBar.setVisibility(View.VISIBLE);
        fitnessBar.setMax(100);

        fitnessBar.setProgress((int) outAma.getFitness());

        ProgressBar attentionBar = (ProgressBar)findViewById(R.id.attentionBar);
        attentionBar.setVisibility(View.VISIBLE);
        attentionBar.setMax(100);

        attentionBar.setProgress((int) outAma.getAttention());

        TextView nameText = (TextView)findViewById(R.id.nameText);
        nameText.setText(outAma.getName());

        TextView ageText = (TextView)findViewById(R.id.ageText);
        ageText.setText(String.valueOf(outAma.getAge()));

        DecimalFormat f = new DecimalFormat("#0.00");

        TextView weightText = (TextView)findViewById(R.id.weightText);
        weightText.setText(String.valueOf(f.format(outAma.getWeight())));
    }

}
