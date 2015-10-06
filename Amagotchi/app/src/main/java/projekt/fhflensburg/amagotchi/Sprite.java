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

    int width, height;

    public Sprite(Bitmap spriteSheet, AnimationTyp event)
    {
        this.spriteSheet = spriteSheet;
        this.event = event;


        //hier müssen wir mal gucken ob das ganze so elegant ist
        width = spriteSheet.getWidth()/8;
        height = spriteSheet.getHeight()/13;

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
            case HAPPY:
                row =1;
                break;
            case UNHAPPY:
                row =2;
                break;
            case SLEEPING:
                row =3;
                break;
            case REFUSE:
                row =4;
                break;
            case EATING:
                row =5;
                break;
            case THINKING:
                row =6;
                break;
            case CLEANING:
                row =7;
                break;
            case WALKING:
                row =8;
                break;
            case DEVELOP:
                row =9;
                break;
            case TURN_LEFT_RIGHT:
                row =10;
                break;
            case DYING:
                row =11;
                break;
            case HATCHING:
                row =12;
                break;
            default:
                Log.e(LOG_TAG, "Error drawAmagotchi()");
        }


        int startX = column * width;
        int startY = row* height;

        Rect srcRect = new Rect(startX +5,startY +5,startX+width-5, startY+height-5);

        // Mit diesem Rect wird der ausgewhlte Bildausschnitt(das Sprite) skaliert, meiner meinung nach schnell verpixelt!

        int smallestSide = Math.min(canvas.getWidth(), canvas.getHeight());
        int destWidth = (int)(smallestSide*.60);
        int destHeight = (int)(smallestSide*.60);

        int destStartX = canvas.getWidth()/2 - destWidth/2;
        int destStartY = canvas.getHeight()/2 - destHeight/2;

        Rect destRect = new Rect(destStartX, destStartY, destStartX + destWidth, destStartY + destHeight);

        canvas.drawBitmap(spriteSheet, srcRect, destRect, null);

      }
}
