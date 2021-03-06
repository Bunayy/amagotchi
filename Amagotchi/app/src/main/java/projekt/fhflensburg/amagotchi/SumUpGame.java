package projekt.fhflensburg.amagotchi;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.Random;

public class SumUpGame
{
    private final String LOG_TAG= "SumUpGame";

    private Calculation calculationOne;
    private Calculation calculationTwo;
    private Calculation calculationThree;
    private Calculation equationNow;

    private int wResultOne;
    private int wResultTwo;

    private TextView calculationTV,resultTV_1, resultTV_2, resultTV_3;


    private Activity activity;

    TextView tempTV;

    ViewFlipper flipper;

    private  final int  MAX_ROUNDS = 3;
    private int wonGames = 0;
    private int solvedEquations = 0;



    private Amagotchi amagotchi;

    /*
        Hier muss auch noch der Bonus auf die Stats bei Erfolg geladen werden
     */

    public SumUpGame(Activity _activity)
    {
        amagotchi = Amagotchi.getState();
        Log.d(LOG_TAG, "Konstruktor");
        activity = _activity;
        init();
        provideTask();
    }


    public void init()
    {
        calculationOne = new Calculation();
        calculationTwo = new Calculation();
        calculationThree = new Calculation();

        Button nextTaskBtn;

        calculationTV = (TextView)activity.findViewById(R.id.calculation);
        resultTV_1 = (TextView)activity.findViewById(R.id.result_1);
        resultTV_2 = (TextView)activity.findViewById(R.id.result_2);
        resultTV_3 = (TextView)activity.findViewById(R.id.result_3);

        Log.d(LOG_TAG, "tvs...");

        nextTaskBtn = (Button)activity.findViewById(R.id.nextTaskBtn);

        flipper = (ViewFlipper)activity.findViewById(R.id.flipper);


        nextTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                solveEquation();
                if (solvedEquations < 2)
                {
                    solvedEquations++;
                    provideTask();
                }
                else
                {
                    Handler timer = new Handler();

                    timer.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            endGame();
                        }
                    }, 500);
                }
            }
        });
    }


    public void endGame()
    {

        MainActivity.sugv.setVisibility(View.INVISIBLE);
        MainActivity.gv.setVisibility(View.VISIBLE);
        flipper.setDisplayedChild(flipper.indexOfChild(activity.findViewById(R.id.gameView)));

        if(wonGames >((int)(MAX_ROUNDS/2)))
        {
            MainService.wonMiniGame();
            MainActivity.gv.setAmagotchiEvent(AnimationTyp.HAPPY);
        }
        else
        {
            MainService.lostMiniGame();
            MainActivity.gv.setAmagotchiEvent(AnimationTyp.UNHAPPY);
        }

        //hier soll eig die Rückleitung auf die Main-Ansicht des Amagotchi geschehen das klappt aber leider nicht ...
        Handler reDo = new Handler();

        reDo.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                MainActivity.gv.setAmagotchiEvent(AnimationTyp.NORMAL);
                MainActivity.sugv.setAmagotchiEvent(AnimationTyp.NORMAL);
            }
        }, 1500);
    }

    public void provideTask()
    {
        Log.d(LOG_TAG, "provideTask()");

        wResultOne = new Random().nextInt(28);
        wResultTwo = new Random().nextInt(28);


        switch (solvedEquations)
        {
            case 0:
                equationNow = calculationOne;
                break;
            case 1:
                equationNow = calculationTwo;
                break;
            case 2:
                equationNow = calculationThree;
                break;
            default:
                Log.e(LOG_TAG, "provideTask() - Fehler beim auswählen der Frage!");
        }


        calculationTV.setText(equationNow.getCalculation());
        resultTV_1.setText(String.valueOf(equationNow.getResult()));
        resultTV_2.setText(String.valueOf((wResultOne)));
        resultTV_3.setText(String.valueOf((wResultTwo)));

    }

    public void solveEquation()
    {

        int amagochtiChoise = new Random().nextInt(3)+1;
        boolean equationSolvedValid = false;

        if(amagochtiChoise == 1)
        {
            equationSolvedValid = true;
        }


        switch (amagochtiChoise)
        {
            case 1:
                tempTV =(TextView)activity.findViewById(R.id.result_1);
                break;
            case 2:
                tempTV =(TextView)activity.findViewById(R.id.result_2);
                break;
            case 3:
                tempTV =(TextView)activity.findViewById(R.id.result_3);
                break;
            default:
                Log.e(LOG_TAG, "solveEquation() - Es kam beim Lösen der Gleichung zu einem Fehler");
        }

        if(equationSolvedValid)
        {

            MainActivity.mSoundService.playSounds("happy");
            MainActivity.sugv.setAmagotchiEvent(AnimationTyp.HAPPY);

            Handler timer = new Handler();

            timer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.sugv.setAmagotchiEvent(AnimationTyp.THINKING);
                }
            }, 1500);


            tempTV.setBackgroundColor(Color.parseColor("#32CD32"));
            wonGames++;

        }
        else
        {
            MainActivity.mSoundService.playSounds("unhappy");
            MainActivity.sugv.setAmagotchiEvent(AnimationTyp.UNHAPPY);

            Handler timer = new Handler();

            timer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.sugv.setAmagotchiEvent(AnimationTyp.THINKING);
                }
            }, 1500);

            tempTV.setBackgroundColor(Color.parseColor("#cc0000"));
        }

        //Zurücksetzen der "Farbe"
        Handler reDo = new Handler();

        reDo.postDelayed(new Runnable() {

            @Override
            public void run() {
                tempTV.setBackgroundColor(Color.parseColor("#cccccc"));
            }
        }, 500);
    }
}
