package projekt.fhflensburg.amagotchi;


import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;


/**
 * Created by User on 30.09.2015.
 */
public class SoundService extends Service
{

    private static String LOG_TAG = "SoundService";

    private final IBinder mBinder = new SoundBinder();
    private MediaPlayer mediaPlayer;


    public class SoundBinder extends Binder
    {
        SoundService getService()
        {
            return SoundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        Log.v(LOG_TAG, "onBind()");
        return mBinder;
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.v(LOG_TAG, "onDestroy()");
        //Hier muss der player released werden, der Thread gestoppt werden und eventuell andere Ressourcen freigegeben werden


        if(mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
            Log.v(LOG_TAG, "Ressourcen wurden freigegeben");
        }
    }
    public void makeNoise()
    {
        if(mediaPlayer == null) mediaPlayer= new MediaPlayer();

        Uri mediaUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.happy);

        try {
            mediaPlayer.setDataSource(getApplicationContext(), mediaUri);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(),e);
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.prepareAsync();

        mediaPlayer.setLooping(true);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.v(LOG_TAG, "onPrepared()");
                mp.start();
            }
        });

    }

}
