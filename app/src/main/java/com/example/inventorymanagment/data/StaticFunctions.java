package com.example.inventorymanagment.data;

import android.app.ProgressDialog;
import android.content.Context;

public class StaticFunctions {

    /* renamed from: pd */
    static ProgressDialog pd;

    public static void showProgress(Context context) {
        pd = new ProgressDialog(context);
        pd.setMessage("loading");
        pd.show();
    }

    public static void dismiss() {
        pd.dismiss();
    }
}
