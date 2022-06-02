package com.dekastudio.customview;


import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.glide.slider.library.SliderTypes.BaseSliderView;

public class AppCompactImageTextSlider extends BaseSliderView {
    public AppCompactImageTextSlider(Context context) {
        super(context);
    }

    public View getView() {
        View v = LayoutInflater.from(this.getContext()).inflate(com.glide.slider.library.R.layout.render_type_text, null);
        AppCompatImageView target = v.findViewById(com.glide.slider.library.R.id.glide_slider_image);
        target.setScaleType(ImageView.ScaleType.FIT_XY);
        AppCompatTextView description = v.findViewById(com.glide.slider.library.R.id.description);
        description.setText(this.getDescription());
        this.bindEventAndShow(v, target);
        return v;
    }
}
