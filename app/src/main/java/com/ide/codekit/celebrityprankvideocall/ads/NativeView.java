package com.ide.codekit.celebrityprankvideocall.ads;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.card.MaterialCardView;

public class NativeView extends CardView {

    public NativeView(@NonNull Context context) {
        super(context);
    }

    public NativeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.setRadius(30f);
        FirebaseAds.showNativeAd((Activity) context, this);
    }


}
