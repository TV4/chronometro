package se.tv4.chronometro;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import se.tv4.chronometro.annotation.LogUILoadingTime;


public class MainActivity extends Activity {

    private TextView mainTextview;

    @LogUILoadingTime(state = LogUILoadingTime.START, name = "MainActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainTextview = (TextView)findViewById(R.id.main_textview);
        mainTextview.setText("Start");
        startDoing();
    }

    @LogUILoadingTime(state = LogUILoadingTime.START, name = "MainActivity")
    private void startDoing() {
        startDo1();
    }

    @LogUILoadingTime(state = LogUILoadingTime.END, name = "MainActivity")
    private void endDo() {
//        try {
//
//            Thread.sleep(1000, 0);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        mainTextview.setText("Finished!");
    }

    @LogUILoadingTime(state = LogUILoadingTime.CHECKPOINT_END, name = "MainActivity")
    private void startDo2() {
//        try {
//
//            Thread.sleep(2000, 0);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        mainTextview.setText("Loading more...");
        endDo();
    }

    @LogUILoadingTime(state = LogUILoadingTime.CHECKPOINT_START, name = "MainActivity")
    private void startDo1() {
//        try {
//
//            Thread.sleep(1500, 0);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        mainTextview.setText("Loading...");
        startDo2();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
