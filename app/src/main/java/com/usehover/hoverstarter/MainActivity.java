package com.usehover.hoverstarter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hover.sdk.actions.HoverAction;
import com.hover.sdk.api.Hover;
import com.hover.sdk.api.HoverParameters;
import com.hover.sdk.permissions.PermissionActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Hover.DownloadListener {
	private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Hover.initialize(getApplicationContext(), this);

        Button permissionsButton = findViewById(R.id.permissions_button);
        permissionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PermissionActivity.class);
                startActivityForResult(i, 0);
            }
        });

	    Button button= (Button) findViewById(R.id.action_button);
	    button.setEnabled(true);
	    button.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    Intent i = new HoverParameters.Builder(MainActivity.this)
					.request("YOUR_ACTION_ID") // Add your action ID here
//                    .extra("YOUR_VARIABLE_NAME", "TEST_VALUE") // Uncomment and add your variables if any
					.buildIntent();
			    startActivityForResult(i, 0);
		    }
	    });
    }

	@Override public void onError(String message) {
//		Toast.makeText(this, "Error while attempting to download actions, see logcat for error", Toast.LENGTH_LONG).show();
		Log.e(TAG, "Error: " + message);
	}

	@Override public void onSuccess(ArrayList<HoverAction> actions) {
//		Toast.makeText(this, "Successfully downloaded " + actions.size() + " actions", Toast.LENGTH_LONG).show();
		Log.d(TAG, "Successfully downloaded " + actions.size() + " actions");
	}
}
