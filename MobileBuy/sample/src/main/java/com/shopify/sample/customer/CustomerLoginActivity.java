/*
 *   The MIT License (MIT)
 *
 *   Copyright (c) 2015 Shopify Inc.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package com.shopify.sample.customer;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.shopify.buy.dataprovider.BuyClientError;
import com.shopify.sample.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public final class CustomerLoginActivity extends AppCompatActivity implements CustomerLoginViewPresenter.View {

    public static final String EXTRAS_PENDING_ACTIVITY_INTENT = "pending_activity_intent";

    private static final String LOG_TAG = CustomerLoginActivity.class.getSimpleName();

    @BindView(R.id.login_layout)
    View loginLayoutView;

    @BindView(R.id.login_email_input_layout)
    TextInputLayout loginEmailInputLayoutView;

    @BindView(R.id.login_email_input)
    AppCompatEditText loginEmailInputView;

    @BindView(R.id.login_password_input_layout)
    TextInputLayout loginPasswordInputLayoutView;

    @BindView(R.id.login_password_input)
    AppCompatEditText loginPasswordInputView;

    @BindView(R.id.registration_layout)
    View registerLayoutView;

    @BindView(R.id.registration_email_input_layout)
    TextInputLayout registrationEmailInputLayoutView;

    @BindView(R.id.registration_email_input)
    AppCompatEditText registrationEmailInputView;

    @BindView(R.id.registration_password_input_layout)
    TextInputLayout registrationPasswordInputLayoutView;

    @BindView(R.id.registration_password_input)
    AppCompatEditText registrationPasswordInputView;

    @BindView(R.id.registration_first_name_input_layout)
    TextInputLayout registrationFirstNameInputLayoutView;

    @BindView(R.id.registration_first_name_input)
    AppCompatEditText registrationFirstNameInputView;

    @BindView(R.id.registration_last_name_input_layout)
    TextInputLayout registrationLastNameInputLayoutView;

    @BindView(R.id.registration_last_name_input)
    AppCompatEditText registrationLastNameInputView;

    private ProgressDialog progressDialog;

    private final CustomerLoginViewPresenter presenter = new CustomerLoginViewPresenter();

    @Override
    public void showError(final Throwable t) {
        if (t instanceof BuyClientError) {
            Log.e(LOG_TAG, "Error: " + ((BuyClientError) t).getRetrofitErrorBody(), t);
        } else {
            Log.e(LOG_TAG, t.getMessage(), t);
        }
        Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void onLoginCustomerSuccess() {
        final PendingIntent pendingIntent = getIntent().getParcelableExtra(EXTRAS_PENDING_ACTIVITY_INTENT);
        if (pendingIntent != null) {
            try {
                pendingIntent.send();
            } catch (Exception e) {
                Log.e(LOG_TAG, "Failed to send pending intent", e);
            }
        }
        finish();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.customer_login_activity);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle(getString(R.string.please_wait));
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                CustomerLoginActivity.this.finish();
            }
        });
        progressDialog.setMessage(getString(R.string.please_wait));

        presenter.attach(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @OnClick(R.id.login_button)
    protected void onLogInClick() {
        final String email = loginEmailInputView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            loginEmailInputLayoutView.setError(getString(R.string.customer_login_email_error_hint));
            return;
        }

        final String password = loginPasswordInputView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            loginPasswordInputLayoutView.setError(getString(R.string.customer_login_password_error_hint));
            return;
        }

        presenter.loginCustomer(email, password);
    }

    @OnClick(R.id.sign_up_button)
    protected void onSignUpClick() {
        loginLayoutView.setVisibility(View.GONE);
        registerLayoutView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.registration_sign_up_button)
    protected void onRegistrationSignUpClick() {
        final String email = registrationEmailInputView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            loginEmailInputLayoutView.setError(getString(R.string.customer_login_email_error_hint));
            return;
        }

        final String password = registrationPasswordInputView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            registrationPasswordInputLayoutView.setError(getString(R.string.customer_login_password_error_hint));
            return;
        }

        final String firstName = registrationFirstNameInputView.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            registrationFirstNameInputLayoutView.setError(getString(R.string.customer_login_first_name_error_hint));
            return;
        }

        final String lastName = registrationLastNameInputView.getText().toString();
        if (TextUtils.isEmpty(lastName)) {
            registrationLastNameInputLayoutView.setError(getString(R.string.customer_login_last_name_error_hint));
            return;
        }

        presenter.createCustomer(email, password, firstName, lastName);
    }

    @OnClick(R.id.registration_cancel_button)
    protected void onCancelRegistrationClick() {
        registerLayoutView.setVisibility(View.GONE);
        loginLayoutView.setVisibility(View.VISIBLE);
    }

}
