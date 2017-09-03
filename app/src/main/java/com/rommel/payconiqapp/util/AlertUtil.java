package com.rommel.payconiqapp.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.rommel.payconiqapp.interfaces.ISimpleCallback;

/**
 * Used to encapsulate alert functionality.
 */
public class AlertUtil {

    public static void showSimpleAlert(Context context, String message, final ISimpleCallback<Object> clickHandler) {

        AlertDialog dialog;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clickHandler.executeCallback(null);
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();
    }
}
