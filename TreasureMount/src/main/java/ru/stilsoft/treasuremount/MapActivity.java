package ru.stilsoft.treasuremount;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.ViewAnimator;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;
import ru.stilsoft.treasuremount.databasesupport.DatabaseSupporter;
import ru.stilsoft.treasuremount.map.MapFragment;
import ru.stilsoft.treasuremount.model.Statistics;


public class MapActivity extends Activity {

    private static final int DIALOG_ABOUT_ID = 1;
    private static final String MAP_FRAGMENT_TAG = "org.osmdroid.MAP_FRAGMENT_TAG";

	private static int CUR_MODE = 0;

    public MapActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FragmentManager fm = this.getFragmentManager();

        if (fm.findFragmentByTag(MAP_FRAGMENT_TAG) == null) {
            MapFragment mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_container, mapFragment, MAP_FRAGMENT_TAG).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_statistics) {
            Statistics statistics = DatabaseSupporter.getStatistics();
            Toast.makeText(this, "Деньги: " + statistics.getMoney(), Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_change_to_treasures) {
			ViewAnimator viewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
			viewAnimator.showNext();
			Toast.makeText(this, "Mode changed to: " + (CUR_MODE == 1 ? "Treasures" : "Map"), Toast.LENGTH_SHORT);
			return true;
		}
		return super.onOptionsItemSelected(item);
    }
}
