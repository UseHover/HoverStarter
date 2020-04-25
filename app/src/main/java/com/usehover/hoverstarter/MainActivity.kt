package com.usehover.hoverstarter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hover.sdk.actions.HoverAction
import com.hover.sdk.api.Hover
import com.hover.sdk.api.HoverParameters
import com.hover.sdk.permissions.PermissionActivity
import java.util.*

class MainActivity : AppCompatActivity(), Hover.DownloadListener {

    private val TAG = "MainActivity"
    lateinit var permissionButton: Button
    lateinit var buttonAction: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Hover.initialize(applicationContext, this)

        permissionButton = findViewById<Button>(R.id.permissions_button)
        permissionButton.setOnClickListener {
            val i = Intent(applicationContext, PermissionActivity::class.java)
            startActivityForResult(i, 0)
        }

        buttonAction = findViewById<View>(R.id.action_button) as Button
        buttonAction.isEnabled = true
        buttonAction.setOnClickListener {
            val i = HoverParameters.Builder(this@MainActivity)
                    .request("YOUR_ACTION_ID") // Add your action ID here
                    .extra("YOUR_VARIABLE_NAME", "TEST_VALUE") // Uncomment and add your variables if any
                    .buildIntent()
            startActivityForResult(i, 0)
        }

    }

    override fun onError(message: String) {
        Toast.makeText(this, "Error while attempting to download actions, see logcat for error", Toast.LENGTH_LONG).show();
        Log.e(TAG, "Error: $message")
    }

    override fun onSuccess(actions: ArrayList<HoverAction>) {
        Toast.makeText(this, "Successfully downloaded " + actions.size + " actions", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Successfully downloaded " + actions.size + " actions")
    }
}