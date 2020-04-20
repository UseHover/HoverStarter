package com.usehover.hoverstarter.permissions;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.text.Html;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hover.sdk.utils.Utils;
import com.usehover.hoverstarter.R;


public class PermissionFragment extends DialogFragment {
	private final static String TAG = "OverlayPermFragment";
	private final static String CMD = "cmd";
	final static int OVER = 0, ACCESS = 1;

	public static PermissionFragment newInstance(int cmd) {
		PermissionFragment frag = new PermissionFragment();
		Bundle args = new Bundle();
		args.putInt(CMD, cmd);
		frag.setArguments(args);
		return frag;
	}

	private void setUp(View view) {
		((TextView) view.findViewById(R.id.app_name)).setText("Starter");
	}

	private void setIcon(View v, int drawable) {
		Drawable d = getResources().getDrawable(drawable);
		if (Build.VERSION.SDK_INT >= 25)
			d.setTint(getResources().getColor(R.color.hsdk_android_settings_color_v25));
		else if (Build.VERSION.SDK_INT >= 21)
			d.setTint(getResources().getColor(R.color.hsdk_android_settings_color_v21));
		((ImageView) v.findViewById(R.id.icon)).setImageDrawable(d);
	}

	private void setMsg(View v, String descript) {
		((TextView) v.findViewById(R.id.description)).setText(Html.fromHtml(descript));
	}

	private void setImgs(View v, boolean isOverlay) {
		int visible = !isOverlay ? View.VISIBLE : View.GONE;
		v.findViewById(R.id.empty_start).setVisibility(isOverlay ? View.VISIBLE : View.GONE);
		v.findViewById(R.id.access_list).setVisibility(visible);
		v.findViewById(R.id.arrow).setVisibility(visible);
		v.findViewById(R.id.step_no_two).setVisibility(visible);
		v.findViewById(R.id.help_text_two).setVisibility(visible);
		v.findViewById(R.id.empty_end).setVisibility(isOverlay ? View.VISIBLE : View.GONE);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final int cmd = getArguments().getInt(CMD);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		@SuppressLint("InflateParams") View view = inflater.inflate(R.layout.permission_dialog, null);
		setUp(view);
		setIcon(view, cmd == OVER ? R.drawable.hsdk_ic_overlay : R.drawable.hsdk_ic_accessibility);
		setImgs(view, cmd == OVER);
		setMsg(view, getString(cmd == OVER ? R.string.hsdk_overlay_msg : R.string.hsdk_access_msg, Utils.getAppName(getContext())));

		AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.HoverSystemMatchDialogTheme))
			.setView(view)
//				.setIcon(cmd == OVER ? R.drawable.hsdk_ic_overlay : R.drawable.hsdk_ic_accessibility)
//				.setTitle(cmd == OVER ? R.string.hsdk_overlay_header : R.string.hsdk_access_header)
//				.setMessage(Html.fromHtml(getString(cmd == OVER ? R.string.hsdk_overlay_msg : R.string.hsdk_access_msg, Utils.getAppName(getContext()))))
			.setPositiveButton(R.string.hsdk_access_btn, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					if (cmd == OVER)
						PermissionHelper.requestOverlayPerm(getActivity());
					else
						PermissionHelper.requestAccessPerm(getActivity());
				}}
			).create();
		fixCancelBehavior(alertDialog);
		return alertDialog;
	}

	private void fixCancelBehavior(AlertDialog dialog) {
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
					if (getActivity() != null) {
						getActivity().finish();
						return true;
					}
				}
				return false;
			}
		});
	}
}
