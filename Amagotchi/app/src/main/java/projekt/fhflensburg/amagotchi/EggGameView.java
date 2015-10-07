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

/**
 * Created by bunay_000 on 07.10.2015.
 */
public class EggGameView extends SurfaceView implements Runnable
{
    private static final String LOG_TAG = "EggGameView";

    private Amagotchi amaGee;

    String level;
    String mutation;
    String amagotchiType;

    Sprite amagotchi;

    SurfaceHolder holder;

    Bitmap poopBitmap;

    Thread drawingThread = null;
    boolean isRunning = true;

    AnimationTyp amagotchiEvent;

    Context ctx;

    int displayWidth;
    int displayHeight;

    boolean firstTime;


    public int paintedSprites = 0;

    public EggGameView(Context context, AttributeSet attrs)
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
                        holder.setFixedSize(displayWidth, displayHeight - ((int) (ctx.getResources().getDimension(R.dimen.game_view_btn))) * 2);
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
        if(canvas != null) {
            canvas.drawColor(Color.rgb(120, 153, 66));

            int amountSprites = 0;

            switch (amagotchiEvent) {
                case NORMAL:
                    amountSprites = 3;
                    break;
                case HAPPY:
                    amountSprites = 5;
                    break;
                case HATCHING:
                    amountSprites = 5;
                    break;
                default:
                    Log.e(LOG_TAG, "Fehler beim Setzen der amountSprites");
            }


            amagotchi = new Sprite(GameView.spriteSheet, amagotchiEvent);
            amagotchi.drawAmagotchi(canvas, paintedSprites);

            if (paintedSprites == amountSprites)
                paintedSprites = -1;

            paintedSprites++;
        }
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
            e.printStackTrace();
        }
    }
}
