package com.adsmodule.api.adsModule.interfaces;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adsmodule.api.R;
import com.adsmodule.api.adsModule.AdUtils;
import com.adsmodule.api.adsModule.utils.Constants;
import com.google.android.material.card.MaterialCardView;

public class CustomView extends MaterialCardView {


    private String adType;
    private String placementType = null;
    private int borderColor;
    private int adsElevation;
    private Drawable adPlaceHolder;

    public CustomView(@NonNull Context context) {
        super(context);
    }

    public CustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0);
        try {
            placementType = array.getString(R.styleable.CustomView_placementType);
            adType = array.getString(R.styleable.CustomView_adType);
            borderColor = array.getColor(R.styleable.CustomView_borderColor, Color.TRANSPARENT);
            adPlaceHolder = array.getDrawable(R.styleable.CustomView_placeholder);
            adsElevation = array.getInt(R.styleable.CustomView_adsElevation, 0);
        } finally {
            array.recycle();
        }

        this.setRadius(32f);
        this.setStrokeWidth(3);
        this.setStrokeColor(borderColor);
        this.setCardElevation(adsElevation);
        AdUtils.showNativeAd((Activity) context, Constants.adsResponseModel.getNative_ads().getAdx(), this, adType, adPlaceHolder == null ? null : adPlaceHolder);
    }

    @Override
    public void onViewAdded(View child) {
        if (placementType == null) {
            throw new IllegalArgumentException("Element " + getClass().getSimpleName() + " doesn't have required attribute android:layout_height");
        }
        super.onViewAdded(child);
    }
}
