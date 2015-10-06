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

    private static final String LOG_TAG = "GameView";

    private Amagotchi amagotchiInstance;

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

    boolean firstTime;

    public GameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        amagotchiInstance = MainService.getAma();

        Resources res = context.getResources();
        String spriteSheetName = state + amagotchiType;
        spriteSheet = BitmapFactory.decodeResource(getResources(), res.getIdentifier(spriteSheetName, "drawable", context.getPackageName()));

        holder = getHolder();

        drawingThread = new Thread(this);
        drawingThread.start();

        ctx = context;

        poopBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.poop_2);


        firstTime = true;

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder)
            {
                //ImageButton btn = (ImageButton)findViewById(R.id.feedBtn);
                //int btnHeight = btn.getHeight();

                if(firstTime) {


                    Rect displayDimensions = holder.getSurfaceFrame();
                    displayWidth = displayDimensions.width();
                    displayHeight = displayDimensions.height();
                    holder.setFixedSize(displayWidth, displayHeight - ((int) (ctx.getResources().getDimension(R.dimen.game_view_btn))) * 2);// hier  müssen wir nochmal gucken bekomme  die referen auf den Btn nicht hin denke das es ihn zu diesem Zeitpunkt noch nicht gibt
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

        if(Amagotchi.getState().getFeces())
        {
            Rect srcRect = new Rect(0, 0, poopBitmap.getWidth(), poopBitmap.getHeight());

            int destStartX = canvas.getWidth() - poopBitmap.getWidth();
            int destStartY = canvas.getHeight() - poopBitmap.getHeight();

            Rect destRect = new Rect(destStartX, destStartY,destStartX + poopBitmap.getWidth(),destStartY + poopBitmap.getHeight());
            canvas.drawBitmap(poopBitmap, srcRect, destRect, null);
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
                amountSprites = 1;
                break;
            case HAPPY:
                amountSprites = 2;
                break;
            case UNHAPPY:
                amountSprites = 2;
                break;
            case SLEEPING:
                amountSprites = 2;
                break;
            case REFUSE:
                amountSprites = 1;
                break;
            case EATING:
                amountSprites = 1;
                break;
            case THINKING:
                amountSprites = 1;
                break;
            case CLEANING:
                amountSprites = 4;
                break;
            case DEVELOP:
                amountSprites = 4;
            case TURN_LEFT_RIGHT:
                amountSprites = 1;
            case DYING:
                amountSprites = 2;
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
