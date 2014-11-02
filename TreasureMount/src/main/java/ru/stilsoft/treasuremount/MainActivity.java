package ru.stilsoft.treasuremount;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import ru.stilsoft.treasuremount.databasesupport.DatabaseInitializer;
import ru.stilsoft.treasuremount.databasesupport.TreasureDatabaseHelper;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Button btnMap = (Button) findViewById(R.id.main_button_map);
		Button btnExit = (Button) findViewById(R.id.main_button_exit);
		btnMap.setEnabled(false);
		btnExit.setEnabled(false);
		
		DatabaseInitializer.initializeDatabases(this.getApplicationContext());

		btnMap.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, MapActivity.class));
			}
		});
		btnExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btnMap.setEnabled(true);
		btnExit.setEnabled(true);

	}

	@Override
	protected void onDestroy() {
		DatabaseInitializer.closeDatabases();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_clean_config) {
			DatabaseInitializer.treasureDatabaseHelper.onUpgrade(DatabaseInitializer.sqLiteDatabase, 100, 0);
			Toast.makeText(getApplicationContext(), "Cleaned successfully.", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
