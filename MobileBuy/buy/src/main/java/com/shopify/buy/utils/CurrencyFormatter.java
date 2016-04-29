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

package com.shopify.buy.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Formats monetary values for display.
 * Money values are usually accompanied by a currency code. Java's number formatting classes
 * can format numbers for display as currency values, but expect a {@link java.util.Locale}.
 */
public class CurrencyFormatter {

    private static final String DEFAULT_CURRENCY = "USD";

    private static final Map<FormatterAttributes, NumberFormat> cache = new HashMap<>();

    /**
     * The returned formatter instance is a shared object. Callers should ensure they do not use the formatter simultaneously from multiple threads.
     */
    public static NumberFormat getFormatter(Locale displayLocale, String currencyToFormat) {
        return CurrencyFormatter.getFormatter(displayLocale, currencyToFormat, true, true, true);
    }

    /**
     * The returned formatter instance is a shared object. Callers should ensure they do not use the formatter simultaneously from multiple threads.
     */
    public static NumberFormat getFormatter(Locale displayLocale, String currencyToFormat, boolean withSymbol, boolean includeGroupingSeparator, boolean includeFractionDigits) {
        FormatterAttributes cacheKey = new FormatterAttributes(displayLocale.toString(), currencyToFormat, withSymbol, includeGroupingSeparator, includeFractionDigits);
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        DecimalFormat formatter;
        Currency currency;
        try {
            currency = Currency.getInstance(currencyToFormat);
        } catch (IllegalArgumentException iae) {
            currency = Currency.getInstance(DEFAULT_CURRENCY);
        }

        if (!withSymbol) {
            formatter = getNoSymbolFormatter(displayLocale);
        } else {
            formatter = getFormatterWithSymbol(displayLocale, currency);
        }

        // Here, we want to ensure that if the current locale uses a different number
        // of decimal digits than the currency we're formatting, we use the currency's.
        // See http://stackoverflow.com/a/19072112/369480 for details.
        //
        // Setting the minimum digits ensures that we preserve trailing zeroes.
        //
        // Calling these setters also has a side effect (desirable) on the rounding mode.
        // Specifically, Galaxy Note 2 with Android 4.3, using de_CH's currency formatter,
        // will format CAD to round to nearest 0.05 while leaving USD values alone.
        // It seems to be using the locale's rounding behaviour (search Google
        // for "Swedish rounding").
        formatter.setMinimumFractionDigits(currency.getDefaultFractionDigits());
        formatter.setMaximumFractionDigits(includeFractionDigits ? currency.getDefaultFractionDigits() : 0);

        formatter.setMinimumIntegerDigits(1);
        formatter.setGroupingUsed(includeGroupingSeparator);

        // Shopify iOS sets the negative format for all locales/currencies as minus sign in front
        // of the positive format. Java's DecimalFormat says that "If there is no explicit negative
        // subpattern, the negative subpattern is the localized minus sign prefixed to the positive
        // subpattern." So by removing any explicit negative subpattern, we get the desired result.
        String pattern = formatter.toPattern();
        int subpatternBoundaryPos = pattern.indexOf(';');
        if (subpatternBoundaryPos != -1) {
            formatter.applyPattern(pattern.substring(0, subpatternBoundaryPos));
        }

        cache.put(cacheKey, formatter);
        return formatter;
    }

    private static DecimalFormat getFormatterWithSymbol(Locale displayLocale, Currency currency) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(displayLocale);
        formatter.setCurrency(currency);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        String currencySymbol = getCountryCodeFreeCurrencySymbol(currency);
        symbols.setCurrencySymbol(currencySymbol);
        formatter.setDecimalFormatSymbols(symbols);
        return formatter;
    }

    private static DecimalFormat getNoSymbolFormatter(Locale displayLocale) {
        // We don't want to use a currency formatter and replace the currency symbol
        // with an empty string, as that can leave behind whitespace between the
        // symbol and the value. Unfortunately, neither String.trim() nor
        // Character.isWhitespace() is helpful in removing it.
        // Instead, let's just use a (non-currency) decimal formatter instance and not
        // produce any whitespace in the first place.
        return (DecimalFormat) DecimalFormat.getInstance(displayLocale);
    }

    public static String getCurrencySymbol(NumberFormat formatter) {
        DecimalFormatSymbols symbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
        return symbols.getCurrencySymbol();
    }

    public static String getCountryCodeFreeCurrencySymbol(Currency currency) {
        String currencySymbol = currency.getSymbol();
        if (currencySymbol == null) {
            currencySymbol = "";
        } else if (currencySymbol.indexOf('$') != -1) {
            //For $ based currencies (USD, CAD, HKD, etc.) we only display the currency symbol without any form of the international code. This is a hack! This revolves
            //around the fact that not all devices have the ability to specify all locales. For example, Android docs state that some phones may have en_US while others
            //may not. This is a terrible design flaw on the Android platform side; these formats are standards, not opinions. A very 'close to home' example of this is
            //that 'Canada' is not available on Nexus devices. You can specify that you speak english, but not that you're in Canada. Therefore, en_CA is unavailable
            //and en_US formats CAD as 'CA$5.00'
            currencySymbol = "$";
        }
        return currencySymbol;
    }

    private static class FormatterAttributes {

        private final String locale;
        private final String currency;
        private final boolean withSymbol;
        private final boolean includeGroupingSeparator;
        private final boolean includeFractionDigits;

        public FormatterAttributes(String locale, String currency, boolean withSymbol, boolean includeGroupingSeparator, boolean includeFractionDigits) {
            this.locale = locale;
            this.currency = currency;
            this.withSymbol = withSymbol;
            this.includeGroupingSeparator = includeGroupingSeparator;
            this.includeFractionDigits = includeFractionDigits;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FormatterAttributes that = (FormatterAttributes) o;

            if (includeFractionDigits != that.includeFractionDigits) return false;
            if (includeGroupingSeparator != that.includeGroupingSeparator) return false;
            if (withSymbol != that.withSymbol) return false;
            if (!currency.equals(that.currency)) return false;
            if (!locale.equals(that.locale)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = locale.hashCode();
            result = 31 * result + currency.hashCode();
            result = 31 * result + (withSymbol ? 1 : 0);
            result = 31 * result + (includeGroupingSeparator ? 1 : 0);
            result = 31 * result + (includeFractionDigits ? 1 : 0);
            return result;
        }
    }
}


