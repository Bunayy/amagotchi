package projekt.fhflensburg.amagotchi;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Howie on 05.10.2015.
 */
public class Sys {

    private static final String LOG_TAG = "Sys";

    public static Boolean saveGame(Amagotchi ama, Context ctx)
    {
        String saveString = ama.getSaveString();

        return saveGameString(saveString, ctx);
    }


    private static Boolean saveGameString(String save, Context ctx) {
        Log.v(LOG_TAG, "saveGameString(String save, Context ctx) " + save);

        try {

            FileOutputStream fOut = ctx.openFileOutput("ama.sav", Context.MODE_PRIVATE);
            fOut.write(save.getBytes());
            fOut.close();
            return true;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            Log.v(LOG_TAG, "saveGameString Fail: " + e.getMessage());
            return false;
        }

    }

    private static String loadGameString(Context ctx) {
        Log.v(LOG_TAG, "loadGameString(Context ctx)");

        String ret = "";

        try {
            FileInputStream fin = ctx.openFileInput("ama.sav");

            //Read all Data
            int c;
            while( (c = fin.read()) != -1){
                ret = ret + Character.toString((char)c);
            }

            //Close and Return
            fin.close();
            return ret;

        } catch (java.io.IOException e) {
            e.printStackTrace();
            Log.v(LOG_TAG, "loadGameString Fail: " + e.getMessage());
            return "fail";
        }
    }

}
