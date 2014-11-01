package ru.stilsoft.treasuremount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import ru.stilsoft.treasuremount.databasesupport.DatabaseInitializer;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        DatabaseInitializer.initializeDatabases(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();

        DatabaseInitializer.closeDatabases();
    }

}
