package se.tv4.chronometro;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import se.tv4.chronometro.annotation.Chronometro;
import se.tv4.chronometro.example.R;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class MainActivity extends Activity {

    private TextView mainTextview;

    @Chronometro(state = Chronometro.START, name = "MainActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainTextview = (TextView) findViewById(R.id.main_textview);
        mainTextview.setText("Start");
        startDo1();
    }

    @Chronometro(state = Chronometro.END, name = "MainActivity")
    private void endDo() {
        new AsyncTask<Object, Object, Void>() {
            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mainTextview.setText("Finished!");
            }
        }.execute();

    }

    @Chronometro(state = Chronometro.CHECKPOINT_END, name = "MainActivity")
    private void startDo2() {
        new AsyncTask<Object, Object, Void>() {
            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    Thread.sleep(1000);
                    endDo();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mainTextview.setText("Loading more...");
            }
        }.execute();
    }


    @Chronometro(state = Chronometro.CHECKPOINT_START, name = "MainActivity")
    private void startDo1() {
        new AsyncTask<Object, Object, Void>() {
            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    Thread.sleep(1000);
                    startDo2();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mainTextview.setText("Loading...");
            }
        }.execute();

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
