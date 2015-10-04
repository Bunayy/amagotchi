package projekt.fhflensburg.amagotchi;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by User on 21.09.2015.
 */
public class GameView extends SurfaceView implements Runnable
{
    private Amagotchi amaGee = Amagotchi.getInstance();

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



    public GameView(Context context)
    {
        super(context);

        Resources res = context.getResources();
        String spriteSheetName = state + amagotchiType;
        spriteSheet = BitmapFactory.decodeResource(getResources(), res.getIdentifier(spriteSheetName, "drawable", context.getPackageName()));


        holder = getHolder();

        drawingThread = new Thread(this);
        drawingThread.start();

        ctx = context;
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
            default:
                Log.v("inf", "drawingThread- Fehler mit dem amagotchiEvent");
        }

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
            amagotchi.drawAmagotchi(canvas, amagotchiCounter);
            holder.unlockCanvasAndPost(canvas);

            if(amagotchiCounter == amountSprites)
                amagotchiCounter = -1;

            amagotchiCounter++;

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
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
