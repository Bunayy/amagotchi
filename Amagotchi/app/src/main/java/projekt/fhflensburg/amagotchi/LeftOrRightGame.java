package projekt.fhflensburg.amagotchi;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

import java.util.Random;


public class LeftOrRightGame implements Runnable
{
    private final static String LOG_TAG = "LeftOrRightGame";

    Activity activity;

    Amagotchi amaGee = MainService.getAma();

    boolean amagotchiFacingLeft = true;
    int changeDirectionCounter = 0;
    public boolean userChooseLeft;


    final int MAX_ROUNDS = 5;
    int playedRounds= 0;
    int wonRounds= 0;

    ViewFlipper flipper;

    ImageButton leftBtn, rightBtn;


    Thread gameThread;

    public LeftOrRightGame(final Activity activity)
    {
        this.activity = activity;

        flipper = (ViewFlipper)activity.findViewById(R.id.flipper);

        leftBtn = (ImageButton)activity.findViewById(R.id.leftBtn);
        rightBtn = (ImageButton)activity.findViewById(R.id.rightBtn);

        leftBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
               startCountdown(true);
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCountdown(false);
            }
        });
    }

    public void endGame()
    {
        if(playedRounds == MAX_ROUNDS)
        {

            MainActivity.lorgv.setVisibility(View.INVISIBLE);
            MainActivity.gv.setVisibility(View.VISIBLE);
            flipper.setDisplayedChild(flipper.indexOfChild(activity.findViewById(R.id.gameView)));

            if(wonRounds > ((int)(MAX_ROUNDS/2)))
            {
                //Amagotchi belohnen
                MainService.wonMiniGame();
                MainActivity.gv.setAmagotchiEvent(AnimationTyp.HAPPY);
            }
            else
            {
                MainService.lostMiniGame();
                MainActivity.gv.setAmagotchiEvent(AnimationTyp.UNHAPPY);
            }

            // Aus dem UI-Thread behandeln !
            Handler updateUI = new Handler();

            updateUI.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.lorgv.setAmagotchiEvent(AnimationTyp.NORMAL);
                    MainActivity.gv.setAmagotchiEvent(AnimationTyp.NORMAL);
                }
            }, 1500); // 1 second delay (takes millis)
        }

    }

    public void startCountdown(boolean turnLeft)
    {
        MainActivity.mSoundService.playSounds("selection");
        userChooseLeft = turnLeft;
        changeDirectionCounter = new Random().nextInt(4)+2;
        MainActivity.lorgv.setAmagotchiEvent(AnimationTyp.TURN_LEFT_RIGHT);
        playedRounds++;

        resume();
    }
    public void rightDirection()
    {
        if(userChooseLeft && amagotchiFacingLeft || !userChooseLeft && !amagotchiFacingLeft)
        {
            MainActivity.mSoundService.playSounds("happy");
            MainActivity.lorgv.setAmagotchiEvent(AnimationTyp.HAPPY);

            Handler timer = new Handler();

            timer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.lorgv.setAmagotchiEvent(AnimationTyp.NORMAL);
                }
            }, 1500);
        }
        else
        {
            MainActivity.mSoundService.playSounds("unhappy");
            MainActivity.lorgv.setAmagotchiEvent(AnimationTyp.UNHAPPY);

            Handler timer = new Handler();

            timer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.lorgv.setAmagotchiEvent(AnimationTyp.NORMAL);
                }
            }, 1500);
        }
        stop();
        endGame();
    }

    public void run()
    {
        Looper.prepare();
        while(changeDirectionCounter >= 0)
        {
            try {
                Thread.sleep(500);

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                Log.e(LOG_TAG, e.getMessage(),e);
            }

            if(MainActivity.lorGame.changeDirectionCounter > 0 && MainActivity.lorgv.amagotchiEvent == AnimationTyp.TURN_LEFT_RIGHT)
            {
                MainActivity.lorGame.amagotchiFacingLeft = !MainActivity.lorGame.amagotchiFacingLeft;
            }
            else if(changeDirectionCounter == 0)
            {
                rightDirection();
            }
            changeDirectionCounter--;
        }
    }

    public void resume()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void stop()
    {
        try
        {
            gameThread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
