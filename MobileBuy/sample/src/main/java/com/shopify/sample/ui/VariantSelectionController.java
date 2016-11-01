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

package com.shopify.sample.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopify.buy.model.Option;
import com.shopify.buy.model.OptionValue;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ProductVariant;
import com.shopify.sample.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Controls the presentation of the array of {@link ProductDetailsVariantOptionView} and manages the dialogs that allow the user to choose a product variant.
 */
class VariantSelectionController {

    public interface OnVariantSelectedListener {
        void onVariantSelected(ProductVariant variant);
    }

    private final Activity activity;
    private final Product product;
    private final ProductDetailsTheme theme;
    private final NumberFormat currencyFormat;
    private final SparseArray<String> checkmarkValues;
    private final ArrayList<ProductVariant> possibleVariants = new ArrayList<>();

    private ProductVariant variant;
    private OnVariantSelectedListener listener;

    public VariantSelectionController(Activity activity, ViewGroup rootView, final Product product, final ProductVariant variant, ProductDetailsTheme theme, NumberFormat currencyFormat) {
        this.activity = activity;
        this.product = product;
        this.variant = variant;
        this.theme = theme;
        this.currencyFormat = currencyFormat;

        View variantContainerView = rootView.findViewById(R.id.product_variant_selection_container);

        checkmarkValues = new SparseArray<>();
        initCheckmarkValues();

        // If the user can't pick a variant, hide the variant selection view
        if (product.getOptions().isEmpty() || product.hasDefaultVariant() || product.getVariants().isEmpty()) {
            variantContainerView.setVisibility(View.GONE);
            rootView.findViewById(R.id.divider1).setVisibility(View.GONE);
            return;
        }

        // only enable the variant selection flow if there is more than 1 variant
        if (product.getVariants().size() > 1) {
            variantContainerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    possibleVariants.addAll(product.getVariants());
                    showVariantSelectionDialog(0, new ArrayList<String>());
                }
            });
        }
    }

    private void initCheckmarkValues() {
        // Build the list of previously selected options (for the checkmarks)
        int i = 0;
        for (OptionValue optionValue : variant.getOptionValues()) {
            checkmarkValues.put(i, optionValue.getValue());
            i++;
        }
    }

    public void setListener(OnVariantSelectedListener listener) {
        this.listener = listener;
    }

    private void showVariantSelectionDialog(final int optionIndex, final ArrayList<String> selectedValues) {
        if (optionIndex >= product.getOptions().size()) {
            // Selection flow is finished, possibleVariants will only have 1 item left in it
            setVariant(possibleVariants.get(0));
            return;
        }

        final Option option = product.getOptions().get(optionIndex);
        final List<String> optionValues = getOptionValues(optionIndex);

        final Map<String, ColoredText> extras = getExtras(optionIndex);
        String checkmarkValue = checkmarkValues.size() > optionIndex ? checkmarkValues.valueAt(optionIndex) : null;
        OptionDialogAdapter adapter = new OptionDialogAdapter(activity, optionValues, checkmarkValue, extras);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(true);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Add the selected option value to the growing array
                String value = optionValues.get(which);
                selectedValues.add(value);

                // If we select a new option, clear the downstream checkmarks
                if (!TextUtils.equals(value, checkmarkValues.get(optionIndex))) {
                    for (int i = checkmarkValues.size() - 1; i >= optionIndex; i--) {
                        checkmarkValues.remove(i);
                    }
                }

                // Add the latest selection to the list of checkmark values
                checkmarkValues.put(optionIndex, value);

                // filter out any variants that have been eliminated by the latest selection
                filterPossibleVariants(selectedValues);

                // recursively show a dialog for the next option to be selected
                showVariantSelectionDialog(optionIndex + 1, selectedValues);
            }
        });

        // If the user is passed the first dialog and they press the back button, go back to the previous dialog
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (optionIndex > 0) {
                    // remove the last selection from the array
                    selectedValues.remove(selectedValues.size() - 1);

                    // re-filter the possible variants
                    possibleVariants.addAll(product.getVariants());
                    filterPossibleVariants(selectedValues);

                    showVariantSelectionDialog(optionIndex - 1, selectedValues);
                } else {
                    // user cancelled the variant selection, reset the checkmark values
                    initCheckmarkValues();
                }
            }
        });

        AlertDialog dialog = builder.create();

        dialog.setCustomTitle(createDialogTitleView(option, dialog, optionIndex));
        dialog.show();

        try {
            // hack to hide the dialog title divider
            // http://stackoverflow.com/questions/15271500/how-to-change-alert-dialog-header-divider-color-android
            int titleDividerId = activity.getResources().getIdentifier("titleDivider", "id", "android");
            View titleDivider = dialog.getWindow().getDecorView().findViewById(titleDividerId);
            titleDivider.setVisibility(View.GONE);
        } catch (Exception ignored) {
        }

        dialog.getListView().setBackgroundColor(theme.getBackgroundColor(activity.getResources()));
        dialog.getListView().setDivider(null);
    }

    private Map<String, ColoredText> getExtras(final int optionIndex) {
        if (optionIndex < product.getOptions().size() - 1) {
            // not on the last dialog yet
            return null;
        }

        // If we are on the last dialog in the flow, we want to show the price (or "SOLD OUT") for each variant
        HashMap<String, ColoredText> extras = new HashMap<>();
        for (ProductVariant variant : possibleVariants) {
            ColoredText coloredText = new ColoredText();
            coloredText.text = variant.isAvailable() ? currencyFormat.format(Double.parseDouble(variant.getPrice())) : activity.getString(R.string.sold_out);
            coloredText.color = variant.isAvailable() ? theme.getCompareAtPriceColor(activity.getResources()) : activity.getResources().getColor(R.color.error_background);
            extras.put(variant.getOptionValues().get(optionIndex).getValue(), coloredText);
        }
        return extras;
    }

    private String getBreadcrumbText(int optionIndex) {
        // Build the list of selected options so users do not have to remember what they picked
        StringBuilder breadcrumbs = new StringBuilder(activity.getString(R.string.selected));
        breadcrumbs.append(": ");
        for (int i = 0; i < optionIndex; i++) {
            breadcrumbs.append(checkmarkValues.get(i));
            if (i < optionIndex - 1) {
                breadcrumbs.append(" • ");
            }
        }
        return breadcrumbs.toString();
    }

    private void setVariant(final ProductVariant variant) {
        this.variant = variant;
        initCheckmarkValues();
        if (listener != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listener.onVariantSelected(variant);
                }
            });
        }
    }

    /**
     * Filter possibleVariants to remove any {@link com.shopify.buy.model.ProductVariant}s that can no longer be accessed,
     * given the options that have already been selected.
     *
     * @param selectedValues The list of {@link OptionValue} values that have been selected so far (e.g. ["Small", "Blue"])
     */
    private void filterPossibleVariants(final List<String> selectedValues) {
        String optionValue;
        ProductVariant variant;
        for (int i = 0; i < selectedValues.size(); i++) {
            optionValue = selectedValues.get(i);
            for (int j = possibleVariants.size() - 1; j >= 0; j--) {
                variant = possibleVariants.get(j);
                if (!TextUtils.equals(variant.getOptionValues().get(i).getValue(), optionValue)) {
                    possibleVariants.remove(j);
                }
            }
        }
    }

    /**
     * Return the list of {@link OptionValue} values that can be selected for the given option index (e.g. ["Small", "Medium", "Large"])
     * To create this list, we need to refer to {@code possibleVariants} instead of {@code product.getVariants()} because some of the
     * {@link com.shopify.buy.model.ProductVariant}s will have been filtered out, which could eliminate possible option values.
     * For example, if there are no Large Blue shirts available, we won't show "Blue" as a color option when the user selects "Large".
     *
     * @param optionIndex The index of the option that the user is currently selecting.
     * @return The list of possible values for the option.
     */
    private List<String> getOptionValues(int optionIndex) {
        Set<String> optionValues = new HashSet<>();

        for (ProductVariant variant : possibleVariants) {
            optionValues.add(variant.getOptionValues().get(optionIndex).getValue());
        }

        return new ArrayList<>(optionValues);
    }

    private View createDialogTitleView(Option option, final AlertDialog dialog, int optionIndex) {
        View titleView = activity.getLayoutInflater().inflate(R.layout.variant_dialog_title_view, null);

        titleView.setBackgroundResource(R.drawable.rounded_top_corners);
        ((GradientDrawable) titleView.getBackground()).setColor(theme.getAccentColor());

        ImageView button = (ImageView) titleView.findViewById(R.id.dialog_title_button);
        button.setBackgroundDrawable(getDialogTitleButtonDrawable(optionIndex));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        TextView titleTextView = (TextView) titleView.findViewById(R.id.dialog_title_text_view);
        titleTextView.setText(option.getName());
        titleTextView.setTextColor(theme.getDialogTitleColor(activity.getResources()));

        TextView breadcrumbTextView = (TextView) titleView.findViewById(R.id.dialog_breadcrumbs_text_view);
        if (optionIndex > 0) {
            breadcrumbTextView.setVisibility(View.VISIBLE);
            breadcrumbTextView.setBackgroundColor(theme.getVariantBreadcrumbBackgroundColor(activity.getResources()));
            breadcrumbTextView.setText(getBreadcrumbText(optionIndex));
        } else {
            breadcrumbTextView.setVisibility(View.GONE);
        }

        return titleView;
    }

    private Drawable getDialogTitleButtonDrawable(int optionIndex) {
        if (theme.getStyle() == ProductDetailsTheme.Style.DARK) {
            return activity.getResources().getDrawable(optionIndex == 0 ? R.drawable.ic_close_black_24dp : R.drawable.ic_arrow_back_black_24dp);
        } else {
            return activity.getResources().getDrawable(optionIndex == 0 ? R.drawable.ic_close_white_24dp : R.drawable.ic_arrow_back_white_24dp);
        }
    }

    private class OptionDialogAdapter extends ArrayAdapter<String> {

        private final LayoutInflater inflater;
        private final String checkmarkValue;
        private final Map<String, ColoredText> extras;

        public OptionDialogAdapter(Context context, List<String> objects, String checkmarkValue, Map<String, ColoredText> extras) {
            super(context, 0, objects);

            this.inflater = activity.getLayoutInflater();
            this.checkmarkValue = checkmarkValue;
            this.extras = extras;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                view = inflater.inflate(R.layout.variant_dialog_list_item, null);
            }

            if (position < getCount() - 1) {
                view.setBackgroundColor(theme.getBackgroundColor(activity.getResources()));
            } else {
                // we need to round the bottom corners of the last view in the list
                view.setBackgroundResource(R.drawable.rounded_bottom_corners);
                ((GradientDrawable) view.getBackground()).setColor(theme.getBackgroundColor(activity.getResources()));
            }

            view.findViewById(R.id.dialog_list_item_container).setBackgroundDrawable(theme.getBackgroundSelectorDrawable(activity.getResources()));

            String value = getItem(position);
            TextView text = (TextView) view.findViewById(R.id.dialog_list_item_name);
            text.setText(value);
            text.setTextColor(theme.getDialogListItemColor(activity.getResources()));

            // Show the price (or "SOLD OUT") if we are on the last dialog
            text = (TextView) view.findViewById(R.id.dialog_list_item_extra);
            if (extras != null && extras.containsKey(value)) {
                text.setText(extras.get(value).text);
                text.setTextColor(extras.get(value).color);
                text.setVisibility(View.VISIBLE);
            } else {
                text.setVisibility(View.GONE);
            }

            // Display a checkmark if necessary to show the user that they had previously selected this option value
            ImageView checkmark = (ImageView) view.findViewById(R.id.dialog_list_item_checkmark);
            if (TextUtils.equals(checkmarkValue, value)) {
                checkmark.setImageDrawable(theme.getCheckmarkDrawable(getContext()));
                checkmark.setVisibility(View.VISIBLE);
            } else {
                checkmark.setVisibility(View.GONE);
            }

            return view;
        }
    }

    private class ColoredText {
        private String text;
        private int color;
    }

}
