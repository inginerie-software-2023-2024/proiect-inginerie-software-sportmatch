// Generated by view binder compiler. Do not edit!
package com.example.sportmatch.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.sportmatch.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class MappopupBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView popupMessage;

  @NonNull
  public final TextView popupTitle;

  @NonNull
  public final ImageView star1;

  @NonNull
  public final ImageView star2;

  @NonNull
  public final ImageView star3;

  @NonNull
  public final ImageView star4;

  @NonNull
  public final ImageView star5;

  private MappopupBinding(@NonNull LinearLayout rootView, @NonNull TextView popupMessage,
      @NonNull TextView popupTitle, @NonNull ImageView star1, @NonNull ImageView star2,
      @NonNull ImageView star3, @NonNull ImageView star4, @NonNull ImageView star5) {
    this.rootView = rootView;
    this.popupMessage = popupMessage;
    this.popupTitle = popupTitle;
    this.star1 = star1;
    this.star2 = star2;
    this.star3 = star3;
    this.star4 = star4;
    this.star5 = star5;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static MappopupBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static MappopupBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.mappopup, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static MappopupBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.popupMessage;
      TextView popupMessage = ViewBindings.findChildViewById(rootView, id);
      if (popupMessage == null) {
        break missingId;
      }

      id = R.id.popupTitle;
      TextView popupTitle = ViewBindings.findChildViewById(rootView, id);
      if (popupTitle == null) {
        break missingId;
      }

      id = R.id.star1;
      ImageView star1 = ViewBindings.findChildViewById(rootView, id);
      if (star1 == null) {
        break missingId;
      }

      id = R.id.star2;
      ImageView star2 = ViewBindings.findChildViewById(rootView, id);
      if (star2 == null) {
        break missingId;
      }

      id = R.id.star3;
      ImageView star3 = ViewBindings.findChildViewById(rootView, id);
      if (star3 == null) {
        break missingId;
      }

      id = R.id.star4;
      ImageView star4 = ViewBindings.findChildViewById(rootView, id);
      if (star4 == null) {
        break missingId;
      }

      id = R.id.star5;
      ImageView star5 = ViewBindings.findChildViewById(rootView, id);
      if (star5 == null) {
        break missingId;
      }

      return new MappopupBinding((LinearLayout) rootView, popupMessage, popupTitle, star1, star2,
          star3, star4, star5);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
