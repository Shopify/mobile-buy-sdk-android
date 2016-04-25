/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Shopify Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.shopify.sample.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.dataprovider.RetrofitError;
import com.shopify.sample.R;
import com.shopify.sample.activity.base.SampleActivity;
import com.shopify.buy.model.Checkout;

import retrofit2.Response;

/**
 * After a shipping rate is selected, this activity allows the user to add discount codes or gift card codes to the order.
 * It also shows a summary of the order, including the line item price, any discounts or gift cards used, the shipping charge, the taxes, and the total price.
 */
public class DiscountActivity extends SampleActivity {

    private interface TextEntryDialogListener {
        void onTextSubmitted(String text);
    }

    private TextView priceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.apply_discounts);
        setContentView(R.layout.discount_activity);

        Button discountButton = (Button) findViewById(R.id.discount_button);
        discountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDiscountButtonClicked();
            }
        });

        Button giftCardButton = (Button) findViewById(R.id.gift_card_button);
        giftCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGiftCardButtonClicked();
            }
        });

        Button checkoutButton = (Button) findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckoutButtonClicked();
            }
        });

        updateOrderSummary();
    }

    private void onDiscountButtonClicked() {
        showTextEntryDialog(R.string.discount_code, R.string.enter_discount_code, R.string.apply_discount, new TextEntryDialogListener() {
            @Override
            public void onTextSubmitted(String text) {
                setDiscountCode(text);
            }
        });
    }

    private void onGiftCardButtonClicked() {
        showTextEntryDialog(R.string.gift_card_code, R.string.enter_gift_card_code, R.string.apply_gift_card, new TextEntryDialogListener() {
            @Override
            public void onTextSubmitted(String text) {
                addGiftCardCode(text);
            }
        });
    }

    /**
     * Displays a simple dialog with an EditText field and a single button, allowing the user to enter either a gift card code or a discount code.
     *
     * @param hint
     * @param title
     * @param button
     * @param listener
     */
    private void showTextEntryDialog(final int hint, final int title, final int button, final TextEntryDialogListener listener) {
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_code_entry, null);
        final EditText editText = (EditText) dialogView.findViewById(R.id.edit_text);
        editText.setHint(hint);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onTextSubmitted(editText.getText().toString());
            }
        });
        builder.create().show();
    }

    /**
     * Add the discount code to the checkout and update the order summary when the request completes.
     *
     * @param discountCode
     */
    private void setDiscountCode(final String discountCode) {
        showLoadingDialog(R.string.syncing_data);
        getSampleApplication().setDiscountCode(discountCode, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                dismissLoadingDialog();
                updateOrderSummary();
            }

            @Override
            public void failure(RetrofitError error) {
                dismissLoadingDialog();
                Toast.makeText(DiscountActivity.this, getString(R.string.discount_error, discountCode), Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Add the gift card code to the checkout and update the order summary when the request completes.
     *
     * @param giftCardCode
     */
    private void addGiftCardCode(final String giftCardCode) {
        showLoadingDialog(R.string.syncing_data);
        getSampleApplication().addGiftCard(giftCardCode, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                dismissLoadingDialog();
                updateOrderSummary();
            }

            @Override
            public void failure(RetrofitError error) {
                dismissLoadingDialog();
                Toast.makeText(DiscountActivity.this, getString(R.string.gift_card_error, giftCardCode), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * When the checkout button is clicked, proceed to the checkout activity (final activity in the app flow).
     */
    private void onCheckoutButtonClicked() {
        startActivity(new Intent(this, CheckoutActivity.class));
    }

}
