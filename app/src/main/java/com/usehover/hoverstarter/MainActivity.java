package com.usehover.hoverstarter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hover.sdk.api.Hover;
import com.hover.sdk.api.HoverParameters;
import com.hover.sdk.permissions.PermissionActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Hover.initialize(getApplicationContext());

        Button permissionsButton = findViewById(R.id.permissions_button);
        permissionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PermissionActivity.class);
                startActivityForResult(i, 0);
            }
        });

        Button button= (Button) findViewById(R.id.action_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new HoverParameters.Builder(getApplicationContext())
                        .request("YOUR_ACTION_ID") // <-- Add your action ID here
                        .buildIntent();
                startActivityForResult(i, 0);
            }
        });

    }
}
