package projekt.fhflensburg.amagotchi;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;


public class Sys {

    private static final String LOG_TAG = "Sys";

    public static Boolean saveGame(Amagotchi ama, Context ctx)
    {
        String saveString = ama.getSaveString();

        return saveGameString(saveString, ctx);
    }

    public static Amagotchi loadGame(Context ctx)
    {
        Amagotchi ama = new Amagotchi("-", "1", ctx);
        Amagotchi.setInstance(ama);

        ama.loadSaveString(loadGameString(ctx));

        return ama;
    }

    public static Boolean saveGameExists(Context ctx)
    {
        String[] hashSplit;
        Boolean hashCorrect = false;

        try
        {
            hashSplit = loadGameString(ctx).split("\\|");
            hashCorrect = String.valueOf(hashSplit[0].hashCode()).equals(hashSplit[1]);

        }
        catch(Exception e)
        {
            Log.v(LOG_TAG, "Checking Game failed while checking hash. Corrupt Gamefile ?");

        }

        return hashCorrect;
    }

    private static Boolean saveGameString(String save, Context ctx) {
        //Log.v(LOG_TAG, "saveGameString(String save, Context ctx) " + save);

        try {

            //FileOutputStream fOut = ctx.openFileOutput("ama", Context.MODE_PRIVATE);

            FileOutputStream fos = ctx.openFileOutput("ama", Context.MODE_PRIVATE);

            fos.write(save.getBytes());
            fos.close();
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
            FileInputStream fin = ctx.openFileInput("ama");

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
