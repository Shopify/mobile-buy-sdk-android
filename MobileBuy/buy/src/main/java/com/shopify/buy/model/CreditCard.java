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

package com.shopify.buy.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents raw credit card data that the user is posting.
 * You MUST discard this object as soon as it has been posted to Shopify's secure environment.
 */
public class CreditCard {

    private String number;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    private String month;

    private String year;

    @SerializedName("verification_value")
    private String verificationValue;

    /**
     * @return The full credit card number.
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number The full credit card number as a numerical value without spaces, dashes or any other special characters.
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return The first name of the card holder.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName The first name of the card holder.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return The last name of the card holder.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName The last name of the card holder.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return The expiry month of the credit card.
     */
    public String getMonth() {
        return month;
    }

    /**
     * @param month The expiry month of the credit card.
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * @return The last two digits of the year in which the credit card expires (i.e. 18 for 2018).
     */
    public String getYear() {
        return year;
    }

    /**
     * @param year The last two digits of the year in which the credit card expires (i.e. 18 for 2018).
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * @return The Card Verification Value number (or whichever card security code should be used for the card type).
     */
    public String getVerificationValue() {
        return verificationValue;
    }

    /**
     * @param verificationValue The Card Verification Value number (or whichever card security code should be used for the card type).
     */
    public void setVerificationValue(String verificationValue) {
        this.verificationValue = verificationValue;
    }
}
