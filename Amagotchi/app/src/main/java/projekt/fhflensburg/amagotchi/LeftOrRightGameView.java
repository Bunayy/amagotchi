package projekt.fhflensburg.amagotchi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class LeftOrRightGameView extends SurfaceView implements Runnable
{
    private static final String LOG_TAG = "LeftOrRightGameView";

    private Amagotchi amaGee;

    //Hier müssen die Werte des Amagotchi rein! MAYBE Observer ?

    String level;
    String mutation;
    String amagotchiType;


    Sprite amagotchi;

    SurfaceHolder holder;

    Thread drawingThread = null;
    boolean isRunning = true;

    AnimationTyp amagotchiEvent;

    Context ctx;


    int displayWidth;
    int displayHeight;

    boolean firstTime;

    public int paintedSprites = 0;

    public LeftOrRightGameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        ctx = context;
    }

    public void init()
    {
        if(MainService.getAma() != null)
        {
            amagotchiEvent = AnimationTyp.NORMAL;

            amaGee = MainService.getAma();

            holder = getHolder();

            drawingThread = new Thread(this);
            drawingThread.start();

            firstTime = true;

            holder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    if (firstTime) {
                        Rect displayDimensions = holder.getSurfaceFrame();
                        displayWidth = displayDimensions.width();
                        displayHeight = displayDimensions.height();
                        holder.setFixedSize(displayWidth - displayWidth / 4, displayHeight - displayHeight / 3);// hier  müssen wir nochmal gucken bekomme  die referen auf den Btn nicht hin denke das es ihn zu diesem Zeitpunkt noch nicht gibt
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
    }


    public void doDrawings(Canvas canvas)
    {
        canvas.drawColor(Color.rgb(120, 153, 66));

        int amountSprites = 0;

        switch(amagotchiEvent)
        {
            case NORMAL:
                amountSprites = 1;
                break;
            case HAPPY:
                amountSprites = 2;
                break;
            case UNHAPPY:
                amountSprites = 2;
                break;
            case TURN_LEFT_RIGHT:
                amountSprites = 1;
                break;
            case DEVELOP:
                amountSprites = 4;
                break;
            default:
                Log.v(LOG_TAG, "drawingThread- Fehler mit dem amagotchiEvent");
        }

        amagotchi = new Sprite(GameView.spriteSheet,amagotchiEvent);
        amagotchi.drawAmagotchi(canvas, paintedSprites);

        if(paintedSprites == amountSprites)
            paintedSprites = -1;

        paintedSprites++;
    }


    public void run()
    {
        while(isRunning)
        {
            try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            if(!holder.getSurface().isValid())
            {
                continue;
            }

            Canvas canvas = holder.lockCanvas();
            doDrawings(canvas);
            holder.unlockCanvasAndPost(canvas);
        }

        if(MainActivity.lorGame.changeDirectionCounter > 0 && amagotchiEvent == AnimationTyp.TURN_LEFT_RIGHT)
        {
            MainActivity.lorGame.amagotchiFacingLeft = !MainActivity.lorGame.amagotchiFacingLeft;
            MainActivity.lorGame.changeDirectionCounter--;
        }
        else if(MainActivity.lorGame.changeDirectionCounter == 0 && amagotchiEvent == AnimationTyp.TURN_LEFT_RIGHT)
        {
            if((MainActivity.lorGame.userChooseLeft && MainActivity.lorGame.amagotchiFacingLeft )||(!MainActivity.lorGame.userChooseLeft && !MainActivity.lorGame.amagotchiFacingLeft))
            {
                MainActivity.mSoundService.playSounds("happy");
                MainActivity.lorgv.setAmagotchiEvent(AnimationTyp.HAPPY);
                Handler timer = new Handler();

                timer.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        MainActivity.lorgv.setAmagotchiEvent(AnimationTyp.NORMAL);
                    }
                }, 1500);

                //amagotchi = new Sprite(spriteSheet, AnimationTyp.NORMAL);
                //amagotchiEvent = AnimationTyp.NORMAL;
            }
            else
            {
                /*
                MainActivity.mSoundService.playSounds("unhappy");
                amagotchi = new Sprite(spriteSheet, AnimationTyp.NORMAL);
                amagotchiEvent = AnimationTyp.NORMAL;
                */
                MainActivity.mSoundService.playSounds("unhappy");
                MainActivity.lorgv.setAmagotchiEvent(AnimationTyp.UNHAPPY);
                Handler timer = new Handler();

                timer.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        MainActivity.lorgv.setAmagotchiEvent(AnimationTyp.NORMAL);
                    }
                }, 1500);
            }
        }
    }

    public void setAmagotchiEvent(AnimationTyp animation)
    {
        paintedSprites = 0;
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

}
