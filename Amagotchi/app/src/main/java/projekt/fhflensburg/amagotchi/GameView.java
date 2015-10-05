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
import android.widget.ImageButton;

/**
 * Created by User on 21.09.2015.
 */
public class GameView extends SurfaceView implements Runnable
{

    private static final String LOG_TAG = "GameView";
    //Zugriff-Methode via Singleton

    //Hier müssen die Werte des Amagotchi rein! MAYBE Observer ?
    String state = "egg";
    String amagotchiType = "_3";

    Sprite amagotchi;

    Bitmap spriteSheet;

    SurfaceHolder holder;

    boolean isSick = false;
    //Hier muss noch einmal geschaut werden wie man mehrfache häufchen abfängt
    boolean hasPooped = true;

    boolean isMainView = true;

    Bitmap poopBitmap;


    Thread drawingThread = null;
    boolean isRunning = true;

    AnimationTyp amagotchiEvent = AnimationTyp.NORMAL;

    Context ctx;


    int displayWidth;
    int displayHeight;

    public void calcCanvasSize(String orign)
    {
        // hier wird geschaut das Amagotchi gerade dargestellt wird und entsprechend die Canvas-Größe gewählt

    }


    public GameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        Resources res = context.getResources();
        String spriteSheetName = state + amagotchiType;
        spriteSheet = BitmapFactory.decodeResource(getResources(), res.getIdentifier(spriteSheetName, "drawable", context.getPackageName()));

        holder = getHolder();

        drawingThread = new Thread(this);
        drawingThread.start();

        ctx = context;

        poopBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.poop_2);

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Rect displayDimensions = holder.getSurfaceFrame();
                displayWidth = displayDimensions.width();
                displayHeight = displayDimensions.height();
                //ImageButton btn = (ImageButton)findViewById(R.id.feedBtn);
                //int btnHeight = btn.getHeight();
                holder.setFixedSize(displayWidth,displayHeight- 60);// hier  müssen wir nochmal gucken bekomme  die referen auf den Btn nicht hin denke das es ihn zu diesem Zeitpunkt noch nicht gibt

                Log.d(LOG_TAG, " displayW: " + displayWidth + " displayH: " + displayHeight  +  " rect: " + displayDimensions);
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
            Log.d(LOG_TAG, "canvas width: " +canvas.getWidth()+" canvas height"+ canvas.getHeight());
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
