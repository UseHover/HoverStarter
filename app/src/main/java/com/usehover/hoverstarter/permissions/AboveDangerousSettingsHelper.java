package com.usehover.hoverstarter.permissions;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.hover.sdk.utils.Utils;

@SuppressWarnings("ConstantConditions")
final public class AboveDangerousSettingsHelper {
	private final static String TAG = "SpecialSettingsHelper", APP_OP_SYS_OVERLAY = "android:system_alert_window";

	@TargetApi(23)
	public static void goToOverlay(Context c) {
		Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + Utils.getPackage(c)));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
		c.startActivity(intent);
	}

	public static void goToAccessibility(Context c) {
		Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
		c.startActivity(intent);
	}

	public static boolean isOverlayEnabled(Context c) {
		return Build.VERSION.SDK_INT < 23 || (Build.VERSION.SDK_INT >= 23 && Build.VERSION.SDK_INT != 26 && Settings.canDrawOverlays(c.getApplicationContext())) || oOverlayEnabled(c);
	}
	@TargetApi(26)
	private static boolean oOverlayEnabled(Context c) {
		AppOpsManager appOpsMgr = (AppOpsManager) c.getSystemService(Context.APP_OPS_SERVICE);
		int mode = appOpsMgr != null ? appOpsMgr.checkOpNoThrow(APP_OP_SYS_OVERLAY, android.os.Process.myUid(), Utils.getPackage(c)) : -1;
		return Build.VERSION.SDK_INT == 26 && (mode == android.app.AppOpsManager.MODE_ALLOWED || mode == android.app.AppOpsManager.MODE_IGNORED);
	}
//
//	public static void onOpChanged(final Context c) {
//		Log.e(TAG, "setting listener");
//		new AppOpsManager.OnOpChangedListener() {
//			@Override public void onOpChanged(String op, String packageName) {
//				Log.e(TAG, "got even for op:" + op);
////				if (Build.VERSION.SDK_INT == 26 && (AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW.equals(op) && packageName.equals(Utils.getPackage(c)))) {
////				}
//			}
//		};
//	}

	public static boolean isAccessibilityEnabled(Context c) {
		final String OUR_SERVICE_NAME = Utils.getPackage(c) + "/com.hover.sdk.requests.HoverAccessibilityService";
		if (accessibilityExists(c)) {
			String enabledApps = Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
			Log.v(TAG, "Enabled Apps List: " + enabledApps);
			if (enabledApps != null) {
				TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(':');
				splitter.setString(enabledApps);
				while (splitter.hasNext()) {
					if (splitter.next().equalsIgnoreCase(OUR_SERVICE_NAME))
						return true;
				}
			}
		}
		Log.d(TAG, "Our app is not enabled");
		return false;
	}

	private static boolean accessibilityExists(Context c) {
		int sysAccessibilityEnabled = 0;
		try {
			sysAccessibilityEnabled = Settings.Secure.getInt(c.getContentResolver(), android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
			Log.v(TAG, "System Accessibility Possible: " + sysAccessibilityEnabled);
		} catch (Settings.SettingNotFoundException e) { // This can just mean that no app has enabled them yet
			Log.d(TAG, "Accessibility Settings could not be found");
		}
		return sysAccessibilityEnabled == 1;
	}

}
