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

    private ViewFlipper flipper;
    private TextView settView;

    private GameView gameView;
    private RelativeLayout canvasContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flipper = (ViewFlipper) findViewById(R.id.flipper);

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
        flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.gameView)));

        //canvasContainer = (RelativeLayout)findViewById(R.id.canvasContainer);
        gameView = (GameView)findViewById(R.id.canvasContainer);
        //gameView = new GameView(this);
        //canvasContainer.addView(gameView);
    }

    public void onContGame(View view)
    {

    }

    public void onSettings(View view)
    {
        Log.v("inf", "onSettings():");
        flipper.setDisplayedChild(flipper.indexOfChild(findViewById(R.id.settingsView)));
    }

    public void onGameHistory(View view)
    {

    }
    public void onFeedingPressed(View v)
    {
        Log.v("test", "Gib Gib GIb !");
    }

    public void onLightPressed(View v)
    {
        Log.v("test", "Licht An/Aus");
    }

    public void onCleaningPressed(View v)
    {
        Log.v("test", "Reinigen");
    }

    public void onStatsPressed(View v)
    {
        Log.v("test", "Hier sind deine Stats");
    }

    public void onMedicinePressed(View v)
    {
        Log.v("test", "Ih bah Medizin !");
    }

    public void onDisciplinePressed(View v)
    {
        Log.v("test", "Böses amagee");
    }

    public void onMinigamesPressed(View v)
    {
        Log.v("test", "Juhu Spiele!");
    }

    public void onWalkingPressed(View v)
    {
        Log.v("test", "Wenns denn sein muss !");
    }

    public void test_1(View v)
    {
        gameView.setAmagotchiEvent(AnimationTyp.NORMAL);
        gameView.hasPooped = true;
    }

    public void test_2(View v)
    {
        gameView.setAmagotchiEvent(AnimationTyp.SLEEPING);
    }

    public void foodSelection(View v)
    {
        Log.v("test", "Es wurde " + v.getId() + " ausgewählt");
    }

}
