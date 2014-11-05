package ru.stilsoft.treasuremount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import ru.stilsoft.treasuremount.databasesupport.DatabaseInitializer;
import ru.stilsoft.treasuremount.databasesupport.TreasureDatabaseHelper;

public class MainActivity extends Activity {

	Button btnMap;
	Button btnExit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		btnMap = (Button) findViewById(R.id.main_button_map);
		btnExit = (Button) findViewById(R.id.main_button_exit);
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
			btnMap.setEnabled(false);
			btnExit.setEnabled(false);
			TreasureDatabaseHelper.rebuild();
			Toast.makeText(getApplicationContext(), "Cleaned successfully.", Toast.LENGTH_SHORT).show();
			btnMap.setEnabled(true);
			btnExit.setEnabled(true);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
