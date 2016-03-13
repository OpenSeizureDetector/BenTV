package openseizuredetector.org.uk.bentv;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
                implements MediaPlayer.OnPreparedListener,
                            MediaPlayer.OnErrorListener,
                            SurfaceHolder.Callback{

    private SurfaceView sv;
    private SurfaceHolder sh;
    private MediaPlayer mediaPlayer;
    private Timer uiTimer;
    private final String TAG = "MainActivity";
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //showToast("onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sv = (SurfaceView) findViewById(R.id.surfaceView);
        sh = sv.getHolder();
        sh.addCallback(this);
    }

    @Override
    protected void onStart() {
        //showToast("onStart()");
        super.onStart();
        // Prevent screen from sleeping.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // get the media URL from preferences.
        SharedPreferences SP = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        mUrl = SP.getString("url","rtsp://guest:guest@192.168.1.6/play2.sdp");

        showToast("URL="+mUrl);

        // Create the mediaPlayer and set the dataSource
        // Note we do not start mediaPlayer here - that is done in the surfaceCreated
        // callback for the SufaceView component in the UI.
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(mUrl);
        } catch(IOException e) {
            showToast("Error Setting MediaPlayer DataSource to "+mUrl+" - "+e.toString());
        }

        // Start the UI Timer
        uiTimer = new Timer();
        uiTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv = (TextView) findViewById(R.id.infoTV);
                        if (mediaPlayer.isPlaying()) {
                            tv.setText("PLAYING - " + mediaPlayer.getCurrentPosition());
                        } else {
                            tv.setText("STOPPED");
                        }

                    }
                });
            }
        }, 0, 1000);

    }

    @Override
    protected void onDestroy() {
        //showToast("onDestroy()");
        super.onDestroy();
        mediaPlayer.release();
    }

    @Override
    protected void onPause() {
        //showToast("onPause()");
        super.onPause();
        uiTimer.cancel();
        uiTimer.purge();
        mediaPlayer.stop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Respond to menu selections (from action bar or menu button)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "Option " + item.getItemId() + " selected");
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.v(TAG, "Settings menu item selected");
                try {
                    Intent prefsIntent = new Intent(
                            MainActivity.this,
                            SettingsActivity.class);
                    this.startActivity(prefsIntent);
                } catch (Exception ex) {
                    Log.v(TAG, "exception starting settings activity " + ex.toString());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //****************************************************************
    //* SurfaceView Callbacks
    //****************************************************************
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //showToast("surfaceCreated()");
        mediaPlayer.setDisplay(sh);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.prepareAsync();
        // Note - the onPrepared callback is called once the mediaPlayer is ready.
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    //****************************************************************
    //* MediaPlayer Callbacks
    //****************************************************************
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        //showToast("onPrepared()");
        mediaPlayer.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        showToast("MediaPlayer Error "+what+", "+extra);
        return false;
    }

    /**
     * Display a Toast message on screen.
     * @param msg - message to display.
     */
    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg,
                Toast.LENGTH_SHORT).show();
    }

}
