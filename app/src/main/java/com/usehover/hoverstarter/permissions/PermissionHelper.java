package com.usehover.hoverstarter.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import com.usehover.hoverstarter.R;


@SuppressWarnings("WeakerAccess")
final public class PermissionHelper {
	public static final String TAG = "PermissionHelper";

	public static void requestAccessPerm(Context c) {
		if (!hasAccessPerm(c))
			AboveDangerousSettingsHelper.goToAccessibility(c);
	}
	public static void requestOverlayPerm(Context c) {
		AboveDangerousSettingsHelper.goToOverlay(c);
	}

	public static boolean hasAllPerms(Context c) { return hasBasicPerms(c) && hasHardPerms(c); }
	public static boolean hasBasicPerms(Context c) {
		return hasPhonePerm(c) && hasSmsPerm(c);
	}
	public static boolean hasHardPerms(Context c) {
		return hasAccessPerm(c) && hasOverlayPerm(c);
	}

	public static boolean hasPhonePerm(Context c) {
		return Build.VERSION.SDK_INT < 23 || (c.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED &&
			                                      c.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);
	}

	public static boolean permissionsGranted(int[] grantResults) {
		return grantResults.length > 1	&& grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED;
	}

	public static boolean hasSmsPerm(Context c) {
		return Build.VERSION.SDK_INT < 23 || c.checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
	}
	public static boolean hasAccessPerm(Context c) {
		return AboveDangerousSettingsHelper.isAccessibilityEnabled(c);
	}
	public static boolean hasOverlayPerm(Context c) {
		return AboveDangerousSettingsHelper.isOverlayEnabled(c);
	}

	public static boolean missingOnlyOne(Context c) {
		return (!hasPhonePerm(c) ^ !hasSmsPerm(c) ^ !hasOverlayPerm(c) ^ !hasAccessPerm(c)) && // has exactly one true OR exactly one false
			       ((hasPhonePerm(c) || hasSmsPerm(c)) && (hasOverlayPerm(c) || hasAccessPerm(c))); // restrict to when 1 permission NOT granted
	}

	@SuppressWarnings("SameParameterValue")
	public static void requestPhone(Activity act, int requestCode) {
		ActivityCompat.requestPermissions(act, new String[]{ Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE }, requestCode);
	}

	public static void requestBasicPerms(Activity act, int requestCode) {
		ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_SMS}, requestCode);
	}

	public static String getPermissionFailureMsg(Context c) {
		String perms = "";
		if (!PermissionHelper.hasPhonePerm(c)) perms = c.getString(R.string.hsdk_perm_name_phone);
		if (!PermissionHelper.hasSmsPerm(c)) perms = perms + ", " + c.getString(R.string.hsdk_perm_name_sms);
		if (!PermissionHelper.hasAccessPerm(c)) perms = perms + ", " + c.getString(R.string.hsdk_perm_name_accessibility);
		if (!PermissionHelper.hasOverlayPerm(c)) perms = perms + ", " + c.getString(R.string.hsdk_perm_name_overlay);
		return c.getString(R.string.hsdk_return_missing_perms_err, perms);
	}
}
