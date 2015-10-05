package projekt.fhflensburg.amagotchi;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by User on 22.09.2015.
 */
public class Sprite
{

    private static final String LOG_TAG = "Sprite";
    Bitmap spriteSheet;
    AnimationTyp event;

    int x,y;
    int width, height;

    public Sprite(Bitmap spriteSheet, AnimationTyp event)
    {
        this.spriteSheet = spriteSheet;
        this.event = event;

        x = y = 0;

        //hier müssen wir mal gucken ob das ganze so elegant ist
        width = spriteSheet.getWidth()/8;
        height = spriteSheet.getHeight()/8;

    }

    public void drawAmagotchi(Canvas canvas, int index)
    {
        int row= 0;
        int column = index;

        switch(event)
        {
            case NORMAL:
                row = 0;
                break;
            case SLEEPING:
                row =1;
                break;
            case HAPPY:
                row =2;
                break;
            case HATCHING:
                row =5;
                break;
            default:
                Log.v("inf", "Error drawAmagotchi()");
        }


        int startX = column * width;
        int startY = row* height;

        Rect srcRect = new Rect(startX +5,startY +5,startX+width-5, startY+height-5);

        // Mit diesem Rect wird der ausgewhlte Bildausschnitt(das Sprite) skaliert, meiner meinung nach schnell verpixelt!
        //Rect destRect = new Rect(canvas.getWidth()/4,canvas.getHeight()/8,canvas.getWidth() - canvas.getWidth()/4,canvas.getHeight()-canvas.getHeight()/3);

        int destWidth = (int)(canvas.getWidth()*.60);
        int destHeight = (int)(canvas.getWidth()*.60);

        int destStartX = canvas.getWidth()/2 - destWidth/2;
        int destStartY = canvas.getHeight()/2 - destHeight/2;

        Rect destRect = new Rect(destStartX, destStartY - destHeight/2, destStartX + destWidth, destStartY + destHeight);

        canvas.drawBitmap(spriteSheet, srcRect, destRect, null);

        Log.d(LOG_TAG, "canvas width: " +canvas.getWidth()+" canvas height"+ canvas.getHeight() + " breite des Sprite: " +width +  " höhe des Sprite: " + height
                + " die SpriteSheet breite: " + spriteSheet.getWidth() + " spriteSheetHöhe: " + spriteSheet.getHeight()+ " destRect: " + destRect);
    }
}
