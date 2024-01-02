// Generated by view binder compiler. Do not edit!
package com.adsmodule.api.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.adsmodule.api.R;
import com.airbnb.lottie.LottieAnimationView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LoadingDialogBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final LottieAnimationView introlottie;

  private LoadingDialogBinding(@NonNull LinearLayout rootView,
      @NonNull LottieAnimationView introlottie) {
    this.rootView = rootView;
    this.introlottie = introlottie;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LoadingDialogBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LoadingDialogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.loading_dialog, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LoadingDialogBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.introlottie;
      LottieAnimationView introlottie = ViewBindings.findChildViewById(rootView, id);
      if (introlottie == null) {
        break missingId;
      }

      return new LoadingDialogBinding((LinearLayout) rootView, introlottie);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
