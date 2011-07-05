package utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import eu.safefleet.R;

public class Dialogs {
	public static void showExitRety(final Context context, final int messageId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(messageId)
				.setCancelable(false)
				.setPositiveButton(R.string.exit,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								((Activity) context).finish();
							}
						})
				.setNegativeButton(R.string.retry,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
