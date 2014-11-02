package ru.stilsoft.treasuremount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import ru.stilsoft.treasuremount.databasesupport.DatabaseInitializer;
import ru.stilsoft.treasuremount.databasesupport.TreasureDatabaseHelper;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btnMap = (Button) findViewById(R.id.main_button_map);
		btnMap.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, MapActivity.class));
			}
		});
		Button btnClean = (Button) findViewById(R.id.cleanDatabaseButton);
		btnClean.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DatabaseInitializer.treasureDatabaseHelper.onUpgrade(DatabaseInitializer.sqLiteDatabase, 100, 0);
				Toast.makeText(getApplicationContext(), "Cleaned successfully.", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		DatabaseInitializer.initializeDatabases(this.getApplicationContext());
	}

	@Override
	protected void onPause() {
		super.onPause();

		DatabaseInitializer.closeDatabases();
	}

}
