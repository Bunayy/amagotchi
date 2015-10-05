package projekt.fhflensburg.amagotchi;

import android.util.Log;

import java.util.Random;

/**
 * Created by User on 03.10.2015.
 */
public class Calculation
{
    private final String LOG_TAG = "Calculation";
    private int summandOne;
    private int summandTwo;
    private int summandThree;

    private final int minValue = 0;
    private final int maxValue= 9;

    private int result;

    private String calculation = "";

    public Calculation()
    {
        Log.d(LOG_TAG, "Calculation Constructor");
        //Es gibt immer maximal 3 und minimal 2 Summanden!
        int rndAmountSummand = new Random().nextInt(( 3 - 2 ) + 1) + 2;

        summandOne = new Random().nextInt((maxValue - minValue)+1) + minValue;
        summandTwo = new Random().nextInt((maxValue - minValue)+1) + minValue;

        calculation += summandOne + " + " +summandTwo;

        result = summandOne + summandTwo;

        if(rndAmountSummand > 2)
        {
            summandThree = new Random().nextInt((maxValue - minValue)+1) + minValue;
            calculation += " + " + summandThree + " = ?";

            result += summandThree;
        }
        else
        {
            calculation += " = ?";
        }

    }

    public String getCalculation()
    {
        return calculation;
    }

    public int getResult()
    {
        return result;
    }
}
