package wl.granjex2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wesley on 18/08/2016.
 */
public class Splash extends Activity {
    private long atraso = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent NovoLayout;
                NovoLayout = new Intent(Splash.this,Login.class);
                startActivity(NovoLayout);

                Splash.this.finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task,atraso);
    }
    // Atributo da classe
    private AlertDialog alerta;
    @Override
    public void onBackPressed() {
        //Finaliza o processo do SplashScreen
        onDestroy();
    }
}
