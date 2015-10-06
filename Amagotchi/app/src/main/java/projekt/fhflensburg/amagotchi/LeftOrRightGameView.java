package projekt.fhflensburg.amagotchi;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;

import java.util.Random;

/**
 * Created by User on 06.10.2015.
 */
public class LeftOrRightGameView extends SurfaceView implements Runnable
{
    private static final String LOG_TAG = "LeftOrRightGameView";

    //Hier müssen die Werte des Amagotchi rein! MAYBE Observer ?
    String state = "egg";
    String amagotchiType = "_3";

    Sprite amagotchi;

    Bitmap spriteSheet;

    SurfaceHolder holder;

    Thread drawingThread = null;
    boolean isRunning = true;

    AnimationTyp amagotchiEvent = AnimationTyp.NORMAL;

    Context ctx;


    int displayWidth;
    int displayHeight;

    boolean firstTime;


    boolean amagotchiFacingLeft = true;
    int changeDirectionCounter = 0;
    public boolean userChooseLeft;


    public LeftOrRightGameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        Resources res = context.getResources();
        String spriteSheetName = state + amagotchiType;
        spriteSheet = BitmapFactory.decodeResource(getResources(), res.getIdentifier(spriteSheetName, "drawable", context.getPackageName()));

        holder = getHolder();

        drawingThread = new Thread(this);
        drawingThread.start();

        ctx = context;

        firstTime = true;

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder)
            {
                //ImageButton btn = (ImageButton)findViewById(R.id.feedBtn);
                //int btnHeight = btn.getHeight();

                if(firstTime)
                {
                    Rect displayDimensions = holder.getSurfaceFrame();
                    displayWidth = displayDimensions.width();
                    displayHeight = displayDimensions.height();
                    holder.setFixedSize(displayWidth - displayWidth/4, displayHeight - displayHeight/3);// hier  müssen wir nochmal gucken bekomme  die referen auf den Btn nicht hin denke das es ihn zu diesem Zeitpunkt noch nicht gibt
                    firstTime = false;
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

    }
    public void doDrawings(Canvas canvas)
    {
        canvas.drawColor(Color.rgb(120, 153, 66));
    }


    public void run()
    {
        amagotchi = new Sprite(spriteSheet,amagotchiEvent);

        int amountSprites = 0;
        int amagotchiCounter = 0;

        switch(amagotchiEvent)
        {
            case NORMAL:
                amountSprites = 3;
                break;
            case SLEEPING:
                amountSprites = 1;
                break;
            case DANCING:
                amountSprites = 3;
                break;
            case HAPPY:
                amountSprites = 5;
                break;
            case HATCHING:
                amountSprites = 5;
                break;
            case TURN_LEFT_RIGHT:
                amountSprites = 1;
                break;
            default:
                Log.v(LOG_TAG, "drawingThread- Fehler mit dem amagotchiEvent");
        }

        while(isRunning)
        {
            try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
                Log.e(LOG_TAG, e.getMessage(), e);
            }

            if(!holder.getSurface().isValid())
            {
                continue;
            }

            Canvas canvas = holder.lockCanvas();

            doDrawings(canvas);
            amagotchi.drawAmagotchi(canvas, amagotchiCounter);
            holder.unlockCanvasAndPost(canvas);

            if(amagotchiCounter == amountSprites)
                amagotchiCounter = -1;

            amagotchiCounter++;

            if(changeDirectionCounter > 0 && amagotchiEvent == AnimationTyp.TURN_LEFT_RIGHT)
            {
                amagotchiFacingLeft = !amagotchiFacingLeft;
                changeDirectionCounter--;
            }
            else if(changeDirectionCounter == 0 && amagotchiEvent == AnimationTyp.TURN_LEFT_RIGHT)
            {
                if((userChooseLeft && amagotchiFacingLeft )||(!userChooseLeft && !amagotchiFacingLeft))
                {
                    MainActivity.mSoundService.playSounds("happy");
                    //setAmagotchiEvent(AnimationTyp.NORMAL);
                    amagotchi = new Sprite(spriteSheet, AnimationTyp.NORMAL);
                }
                else
                {
                    MainActivity.mSoundService.playSounds("unhappy");
                    //setAmagotchiEvent(AnimationTyp.NORMAL);
                    amagotchi = new Sprite(spriteSheet, AnimationTyp.NORMAL);
                }
            }

        }

    }

    public void setAmagotchiEvent(AnimationTyp animation)
    {
        this.amagotchiEvent= animation;

        stop();
        resume();
    }

    public void resume()
    {
        isRunning=true;

        drawingThread = new Thread(this);
        drawingThread.start();
    }

    public void stop()
    {
        isRunning = false;

        try
        {
            drawingThread.join();
        }
        catch (InterruptedException e)
        {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    public void startCountdown(boolean turnLeft)
    {
        userChooseLeft = turnLeft;
        changeDirectionCounter = new Random().nextInt(4)+2;
        setAmagotchiEvent(AnimationTyp.TURN_LEFT_RIGHT);
    }
}
