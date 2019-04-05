package wl.granjex2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wesley on 06/09/2016.
 */
public class Carregamentoespera extends Activity {
    private long atraso = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_carregamentoespera);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Carregamentoespera.this.finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, atraso);
    }
    @Override
    public void onBackPressed() {
        //Finaliza o processo do SplashScreen
        onDestroy();
    }
}
