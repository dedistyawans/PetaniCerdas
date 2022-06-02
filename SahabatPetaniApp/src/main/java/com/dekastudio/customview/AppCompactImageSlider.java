package com.dekastudio.customview;


import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.glide.slider.library.SliderTypes.BaseSliderView;

public class AppCompactImageSlider extends BaseSliderView {
    public AppCompactImageSlider(Context context) {
        super(context);
    }

    public View getView() {
        View v = LayoutInflater.from(this.getContext()).inflate(com.glide.slider.library.R.layout.render_type_default, (ViewGroup)null);
        AppCompatImageView target = (AppCompatImageView)v.findViewById(com.glide.slider.library.R.id.glide_slider_image);
        target.setScaleType(ImageView.ScaleType.FIT_XY);
        this.bindEventAndShow(v, target);
        return v;
    }
}