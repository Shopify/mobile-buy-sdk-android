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

package com.shopify.buy.ui;

import android.content.res.Resources;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.shopify.buy.R;
import com.shopify.buy.model.OptionValue;
import com.shopify.buy.model.Product;

/**
 * Displays the options available for the {@link Product}.
 */
class ProductDetailsVariantOptionView {

    private final Resources resources;
    private final int index;
    private final TableLayout parentTable;

    private TextView name;
    private TextView value;

    public ProductDetailsVariantOptionView(View rootView, int index, Resources resources, ProductDetailsTheme theme) {
        this.resources = resources;
        this.index = index;
        this.parentTable = (TableLayout) rootView.findViewById(R.id.product_variant_selection_container);

        parentTable.setBackgroundDrawable(theme.getBackgroundSelectorDrawable(resources));

        switch (index) {
            case 0:
                name = (TextView) rootView.findViewById(R.id.product_option_name_0);
                value = (TextView) rootView.findViewById(R.id.product_option_value_0);
                break;
            case 1:
                name = (TextView) rootView.findViewById(R.id.product_option_name_1);
                value = (TextView) rootView.findViewById(R.id.product_option_value_1);
                break;
            case 2:
                name = (TextView) rootView.findViewById(R.id.product_option_name_2);
                value = (TextView) rootView.findViewById(R.id.product_option_value_2);
                break;
        }
    }

    public void setTheme(ProductDetailsTheme theme) {
        if (name == null || value == null) return;

        name.setTextColor(theme.getVariantOptionNameColor(resources));
        value.setTextColor(theme.getAccentColor());
    }

    public void setOptionValue(OptionValue optionValue) {
        if (name == null || value == null) return;

        name.setText(optionValue.getName());
        value.setText(optionValue.getValue());

        parentTable.setColumnShrinkable(index, true);
        parentTable.setColumnCollapsed(index, false);
        parentTable.requestLayout();
    }

    public void hide() {
        if (name == null || value == null) return;

        name.setVisibility(View.GONE);
        value.setVisibility(View.GONE);

        parentTable.setColumnCollapsed(index, true);
    }

}
