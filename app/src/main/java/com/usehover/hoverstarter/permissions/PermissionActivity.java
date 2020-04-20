package com.usehover.hoverstarter.permissions;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hover.sdk.api.Hover;
import com.usehover.hoverstarter.R;

import java.util.Arrays;

@SuppressWarnings("WeakerAccess")
public class PermissionActivity extends AppCompatActivity {
	private final static String TAG = "PermissionActivity";
	public final static String CMD = "cmd";
	public final static int PHONE = 0, SMS = 1, OVER = 2, ACCESS = 3;
	public final static String PRIMARY_COLOR = "primary_color";
	public final static String SECONDARY_COLOR = "secondary_color";

	private int cmd, current = PHONE;
	private int primaryColor;
	private int secondaryColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (PermissionHelper.hasAllPerms(this))
			returnSuccess();
		createView();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (current != PHONE)
			requestNext();
	}

	private void createView() {
		setContentView(R.layout.permission_activity);
		getCustomization(getIntent());
		setUpToolbar();
		personalize();
	}

	private void getCustomization(Intent i) {
		primaryColor = getResources().getColor(i.getIntExtra(PRIMARY_COLOR, R.color.hsdk_HoverBlue));
		secondaryColor = getResources().getColor(i.getIntExtra(SECONDARY_COLOR, R.color.hsdk_white));
	}

	private void setUpToolbar() {
		Toolbar toolbar = findViewById(R.id.integration_toolbar);
		setSupportActionBar(toolbar);
		toolbar.setBackgroundColor(primaryColor);
		toolbar.setTitleTextColor(secondaryColor);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void personalize() {
		((TextView) findViewById(R.id.app_name_header)).setText(getString(R.string.hsdk_want_perms, "Starter"));
		((TextView) findViewById(R.id.continue_btn)).setTextColor(primaryColor);
		if (PermissionHelper.hasPhonePerm(this))	findViewById(R.id.overview_phone).setVisibility(View.GONE);
		if (PermissionHelper.hasSmsPerm(this))		findViewById(R.id.overview_sms).setVisibility(View.GONE);
		if (PermissionHelper.hasOverlayPerm(this))	findViewById(R.id.overview_over).setVisibility(View.GONE);
		if (PermissionHelper.hasAccessPerm(this))	findViewById(R.id.overview_access).setVisibility(View.GONE);
		if (PermissionHelper.missingOnlyOne(this))
			continueOn(null);
	}

	void setTitle(String title) {
		if (getSupportActionBar() == null) setUpToolbar();
		getSupportActionBar().setTitle(title);
	}

	public void continueOn(View view) {
		Log.i(TAG, "continuing to next permission");
		requestNext();
	}

	private void requestNext() {
		hideOverview();
		if (!PermissionHelper.hasBasicPerms(this))
			requestBasic(cmd);
		else if (!PermissionHelper.hasOverlayPerm(this))
			requestOverlay();
		else if (!PermissionHelper.hasAccessPerm(this))
			requestAccess();
		else
			returnSuccess();
	}

	private void hideOverview() {
		findViewById(R.id.overview).setVisibility(View.GONE);
		findViewById(R.id.integration_toolbar).setVisibility(View.GONE);
	}

	private void requestBasic(int requestCode) {
		if (!PermissionHelper.hasBasicPerms(this)) {
			current = PHONE;
			PermissionHelper.requestBasicPerms(this, requestCode);
		} else
			requestNext();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (PermissionHelper.permissionsGranted(grantResults) && PermissionHelper.hasBasicPerms(getApplicationContext())) {

			Hover.updateSimInfo(this);
			requestNext();
		} else {

			finish();
		}
	}

	private void requestOverlay() {

		current = OVER;
		changeFrag(PermissionFragment.newInstance(PermissionFragment.OVER));
	}

	private void requestAccess() {

		current = ACCESS;
		changeFrag(PermissionFragment.newInstance(PermissionFragment.ACCESS));
	}

	private void changeFrag(DialogFragment frag) {
		try {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			Fragment fragmentToRemove = getSupportFragmentManager().findFragmentByTag("dialog");
			if (fragmentToRemove != null)
				ft.remove(fragmentToRemove);
			ft.commitAllowingStateLoss();
			frag.show(getSupportFragmentManager(), "dialog");
		} catch (IllegalStateException | NullPointerException e) { Log.d(TAG, "Fragment Transaction Failed", e); }
	}

	private void returnSuccess() {

		setResult(RESULT_OK);
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
