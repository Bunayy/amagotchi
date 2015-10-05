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
 * Created by User on 21.09.2015.
 */
public class GameView extends SurfaceView implements Runnable
{

    //Zugriff-Methode via Singleton
    private static GameView instance;

    //Hier müssen die Werte des Amagotchi rein! MAYBE Observer ?
    String state = "egg";
    String amagotchiType = "_3";

    Sprite amagotchi;

    Bitmap spriteSheet;

    SurfaceHolder holder;

    boolean isSick = false;
    //Hier muss noch einmal geschaut werden wie man mehrfache häufchen abfängt
    boolean hasPooped = false;

    boolean isMainView = true;

    Bitmap poopBitmap;


    Thread drawingThread = null;
    boolean isRunning = true;

    AnimationTyp amagotchiEvent = AnimationTyp.NORMAL;

    Context ctx;



    public GameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        Resources res = context.getResources();
        String spriteSheetName = state + amagotchiType;
        spriteSheet = BitmapFactory.decodeResource(getResources(), res.getIdentifier(spriteSheetName, "drawable", context.getPackageName()));

        holder = getHolder();
        holder.setFixedSize(500,500);

        drawingThread = new Thread(this);
        drawingThread.start();

        ctx = context;

        poopBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.poop_2);

        instance = this;
    }
    public void doDrawings(Canvas canvas)
    {
        if(isMainView)
        {
            canvas.drawColor(Color.rgb(120, 153, 66));
        }
        else
        {
            canvas.drawColor(Color.rgb(250, 50, 0));
        }

        if(hasPooped)
        {
            // hier muss dann eventuell abgefragt werden ob  und wie häufig bisher gekackert wurde, dem entsprechend wird die Bitmap dann plaziert
            Rect srcRect = new Rect(0, 0, poopBitmap.getWidth(), poopBitmap.getHeight());

            // responsive shit !
            Rect destRect = new Rect(50,50,poopBitmap.getWidth(), poopBitmap.getHeight());
            canvas.drawBitmap(poopBitmap, srcRect,destRect, null);
        }
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

    public static GameView getInstance()
    {
        return GameView.instance;
    }

}
