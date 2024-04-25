package com.ide.codekit.celebrityprankvideocall.ads;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

public class BannerView extends CardView {
    public BannerView(@NonNull Context context) {
        super(context);
    }

    public BannerView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);

        this.setRadius(0f);
        this.setCardElevation(0f);
        FirebaseAds.showBannerAd((Activity) context, this);
    }
}