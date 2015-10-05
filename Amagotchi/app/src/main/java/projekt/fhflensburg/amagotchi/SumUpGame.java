package com.example.pschoe.surfaceviewtestapp;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by User on 03.10.2015.
 */
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
    private int solvedCalculations;

    /*
        Hier muss auch noch der Bonus auf die Stats bei Erfolg geladen werden
     */

    public SumUpGame(Activity _activity)
    {
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

        solvedCalculations = 0;

        Button nextTaskBtn;

        calculationTV = (TextView)activity.findViewById(R.id.calculation);
        resultTV_1 = (TextView)activity.findViewById(R.id.result_1);
        resultTV_2 = (TextView)activity.findViewById(R.id.result_2);
        resultTV_3 = (TextView)activity.findViewById(R.id.result_3);

        Log.d(LOG_TAG, "tvs...");

        nextTaskBtn = (Button)activity.findViewById(R.id.nextTaskBtn);

        nextTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (solvedCalculations < 2)
                {
                    solveEquation();
                    solvedCalculations++;
                    provideTask();
                }
                else
                {
                    Log.v(LOG_TAG, "onClick nextTask - setContentView");
                    //hier soll eig die Rückleitung auf die Main-Ansicht des Amagotchi geschehen das klappt aber leider nicht ...
                }

            }
        });
    }

    public void provideTask()
    {
        Log.v(LOG_TAG, "provideTask()");

        wResultOne = new Random().nextInt(28);
        wResultTwo = new Random().nextInt(28);


        switch (solvedCalculations)
        {
            case 0:
                //calc = calculationOne.getCalculation();
                //rResult = calculationOne.getResult();
                equationNow = calculationOne;
                break;
            case 1:
                //calc = calculationTwo.getCalculation();
                //rResult = calculationTwo.getResult();
                equationNow = calculationTwo;
                break;
            case 2:
                //calc = calculationThree.getCalculation();
                //rResult = calculationThree.getResult();
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

        if(amagochtiChoise == 1) equationSolvedValid = true;

        if(equationSolvedValid)GameView.getInstance().isMainView = false;

    }

}