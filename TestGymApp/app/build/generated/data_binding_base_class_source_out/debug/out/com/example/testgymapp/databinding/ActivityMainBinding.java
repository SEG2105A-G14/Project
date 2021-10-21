// Generated by view binder compiler. Do not edit!
package com.example.testgymapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.testgymapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView createAccountText;

  @NonNull
  public final EditText emailText;

  @NonNull
  public final Button loginButton;

  @NonNull
  public final EditText loginPassword;

  @NonNull
  public final TextView textView2;

  @NonNull
  public final TextView textView4;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView createAccountText, @NonNull EditText emailText, @NonNull Button loginButton,
      @NonNull EditText loginPassword, @NonNull TextView textView2, @NonNull TextView textView4) {
    this.rootView = rootView;
    this.createAccountText = createAccountText;
    this.emailText = emailText;
    this.loginButton = loginButton;
    this.loginPassword = loginPassword;
    this.textView2 = textView2;
    this.textView4 = textView4;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.createAccountText;
      TextView createAccountText = ViewBindings.findChildViewById(rootView, id);
      if (createAccountText == null) {
        break missingId;
      }

      id = R.id.emailText;
      EditText emailText = ViewBindings.findChildViewById(rootView, id);
      if (emailText == null) {
        break missingId;
      }

      id = R.id.loginButton;
      Button loginButton = ViewBindings.findChildViewById(rootView, id);
      if (loginButton == null) {
        break missingId;
      }

      id = R.id.loginPassword;
      EditText loginPassword = ViewBindings.findChildViewById(rootView, id);
      if (loginPassword == null) {
        break missingId;
      }

      id = R.id.textView2;
      TextView textView2 = ViewBindings.findChildViewById(rootView, id);
      if (textView2 == null) {
        break missingId;
      }

      id = R.id.textView4;
      TextView textView4 = ViewBindings.findChildViewById(rootView, id);
      if (textView4 == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, createAccountText, emailText,
          loginButton, loginPassword, textView2, textView4);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}