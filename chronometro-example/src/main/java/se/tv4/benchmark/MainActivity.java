package se.tv4.benchmark;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import se.tv4.benchmark.annotation.LogUILoadingTime;

public class MainActivity extends Activity {

    @LogUILoadingTime(state = LogUILoadingTime.START, name = "MainActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startDoing();
    }


    private void startDoing() {
        startDo1();
        startDo2();
        endDo();
    }

    @LogUILoadingTime(state = LogUILoadingTime.END, name = "MainActivity")
    private void endDo() {
        try {
            Thread.sleep(100, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @LogUILoadingTime(state = LogUILoadingTime.CHECKPOINT_END, name = "MainActivity")
    private void startDo2() {
        try {
            Thread.sleep(200, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @LogUILoadingTime(state = LogUILoadingTime.CHECKPOINT_START, name = "MainActivity")
    private void startDo1() {
        try {
            Thread.sleep(150, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
