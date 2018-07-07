package com.hanuor.puzzly.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.hanuor.puzzly.R;

public class ProgressDialog {

    public static Dialog progressDialog(Context context) {
        Dialog dialog = new Dialog(context);
        View inflate = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null);
        dialog.setContentView(inflate);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }
}
