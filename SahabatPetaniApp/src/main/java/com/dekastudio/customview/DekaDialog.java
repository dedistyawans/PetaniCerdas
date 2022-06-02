package com.dekastudio.customview;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

public class DekaDialog extends Dialog {

    private View view;
    public DekaDialog(@NonNull Context context, @LayoutRes int customLayout) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        view = inflater.inflate(customLayout, null);
        setContentView(view);
    }

    public View getView(){
        return  view;
    }

    public void setDapatDiBatal(boolean dapatDiBatal){
        setCancelable(dapatDiBatal);
    }

    public void setBatalPadaSisiLuar(boolean batalSisiLuar){
        setCanceledOnTouchOutside(batalSisiLuar);
    }

    public void tampilkanDialog(){
        show();
    }

    public void tutupDialog(){
        dismiss();
    }
}
