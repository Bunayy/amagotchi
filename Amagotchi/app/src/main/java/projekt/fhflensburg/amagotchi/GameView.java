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


public class GameView extends SurfaceView implements Runnable
{

    private static final String LOG_TAG = "GameView";

    private Amagotchi amaGee;

    //Hier müssen die Werte des Amagotchi rein! MAYBE Observer ?

    String level;
    String mutation;
    String amagotchiType;


    Sprite amagotchi;
    public static Bitmap spriteSheet;
    Bitmap poopBitmap;
    Bitmap sicknessBitmap;

    SurfaceHolder holder;


    Thread drawingThread = null;
    boolean isRunning = true;

    AnimationTyp amagotchiEvent;

    Context ctx;

    int displayWidth;
    int displayHeight;

    boolean firstTime;


    public int paintedSprites = 0;

    public GameView(Context context, AttributeSet attrs)
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
            updateAmagotchiInformation();

            holder = getHolder();

            drawingThread = new Thread(this);
            drawingThread.start();

            poopBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.poop_2);
            sicknessBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sick);

            firstTime = true;

            holder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    if (firstTime) {
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
    }


    public void updateAmagotchiInformation()
    {
        level = "level" + amaGee.getLevel();
        mutation = "_mutation" +  amaGee.getMutation();
        amagotchiType = "_type" + amaGee.getType();

        Resources res = ctx.getResources();
        String spriteSheetName = level + mutation + amagotchiType;
        spriteSheet = BitmapFactory.decodeResource(getResources(), res.getIdentifier(spriteSheetName, "drawable", ctx.getPackageName()));
    }

    public void doDrawings(Canvas canvas)
    {
        if(canvas != null) {

            if(amaGee.isLightOff())
            {
                canvas.drawColor(Color.rgb(25,25,25));
            }
            else {
                canvas.drawColor(Color.rgb(120, 153, 66));
            }


            int amountSprites = 0;

            if (amaGee.getFeces()) {
                Rect srcRect = new Rect(0, 0, poopBitmap.getWidth(), poopBitmap.getHeight());

                int destStartX = canvas.getWidth() - poopBitmap.getWidth();
                int destStartY = canvas.getHeight() - poopBitmap.getHeight();

                Rect destRect = new Rect(destStartX, destStartY, destStartX + poopBitmap.getWidth(), destStartY + poopBitmap.getHeight());
                canvas.drawBitmap(poopBitmap, srcRect, destRect, null);
            }

            if (amaGee.getIsSickInfection() || amaGee.getIsSickOveraeting())
            {
                Rect srcRect = new Rect(0, 0, sicknessBitmap.getWidth(), sicknessBitmap.getHeight());

                int destStartX = canvas.getWidth() - sicknessBitmap.getWidth();
                int destStartY = sicknessBitmap.getHeight();

                Rect destRect = new Rect(destStartX, destStartY, destStartX + sicknessBitmap.getWidth(), destStartY + sicknessBitmap.getHeight());
                canvas.drawBitmap(sicknessBitmap, srcRect, destRect, null);
            }

            switch (amagotchiEvent)
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
                    break;
                case TURN_LEFT_RIGHT:
                    amountSprites = 1;
                    break;
                case DYING:
                    amountSprites = 2;
                    break;
                default:
                    Log.v(LOG_TAG, "drawingThread- Fehler mit dem amagotchiEvent");

            }

            amagotchi = new Sprite(spriteSheet, amagotchiEvent);
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
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
