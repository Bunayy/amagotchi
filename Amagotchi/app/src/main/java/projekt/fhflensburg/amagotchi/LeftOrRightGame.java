package projekt.fhflensburg.amagotchi;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

import java.util.Random;


public class LeftOrRightGame
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


    public LeftOrRightGame(final Activity activity)
    {
        this.activity = activity;

        flipper = (ViewFlipper)activity.findViewById(R.id.flipper);

        leftBtn = (ImageButton)activity.findViewById(R.id.leftBtn);
        rightBtn = (ImageButton)activity.findViewById(R.id.rightBtn);

        leftBtn.setOnClickListener(new View.OnClickListener() {
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
            if(wonRounds > ((int)(MAX_ROUNDS/2)))
            {
                //Amagotchi belohnen
                MainService.wonMiniGame();
                MainActivity.lorgv.setAmagotchiEvent(AnimationTyp.HAPPY);
            }

            // Aus dem UI-Thread behandeln !
            Handler updateUI = new Handler();

            updateUI.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    MainActivity.sugv.setVisibility(View.INVISIBLE);
                    MainActivity.gv.setVisibility(View.VISIBLE);
                    MainActivity.lorgv.setVisibility(View.INVISIBLE);

                    flipper.setDisplayedChild(flipper.indexOfChild(activity.findViewById(R.id.gameView)));

                    MainActivity.lorgv.setAmagotchiEvent(AnimationTyp.NORMAL);
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

        endGame();
    }

}
