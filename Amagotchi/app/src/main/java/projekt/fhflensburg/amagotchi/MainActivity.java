package projekt.fhflensburg.amagotchi;

import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    private ViewFlipper flipper;

    //Bei Start der App
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flipper = (ViewFlipper) findViewById(R.id.flipper);

        Log.v(LOG_TAG, "---BEFORE---");
        Amagotchi ama = new Amagotchi(this);
        Log.v(LOG_TAG, "---START---");
        Log.v(LOG_TAG, ama.getSaveString());
        Log.v(LOG_TAG, "---END---");
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
        Log.d(LOG_TAG, "Ih bah Medizin !");
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
}
