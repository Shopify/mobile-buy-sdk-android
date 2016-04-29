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

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a shipping or billing address on an order. This will be associated with the customer upon completion.
 */

public class Address extends ShopifyObject {

    private String address1;

    private String address2;

    private String city;

    private String company;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    private String phone;

    private String country;

    @SerializedName("country_code")
    private String countryCode;

    private String province;

    @SerializedName("province_code")
    private String provinceCode;

    private String zip;

    /**
     * @return The unique identifier of this object within the Shopify platform.
     */
    public String getAddressId() {
        return String.valueOf(id);
    }

    /**
     * @return The street of the address.
     */
    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * @return An optional additional field for the street address of the address.
     */
    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * @return The city of the address.
     */
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return The company of the person associated with the address, or {@code null} if no company has been set.
     */
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return The first name of the person associated with the payment method.
     */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return The last name of the person associated with the payment method.
     */
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return The phone number at the address.
     */
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return The name of the country of the address.
     */
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return The two-letter code (ISO 3166-1 alpha-2 two-letter country code) for the country of the address.
     */
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * @return The name of the state or province of the address.
     */
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * @return The two-letter abbreviation of the state or province of the address.
     */
    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    /**
     * @return The zip or postal code of the address.
     */
    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * Compares the location portion the address for equality
     *
     * @param otherAddress The address to compare to
     * @return true if the address lines, city, country code, province code, and zip are equivalent
     */
    public boolean locationsAreEqual(Address otherAddress) {

        if (this == otherAddress) {
            return true;
        }

        if (!TextUtils.equals(address1, otherAddress.address1)) {
            return false;
        }

        if (!TextUtils.equals(address2, otherAddress.address2)) {
            return false;
        }

        if (!TextUtils.equals(city, otherAddress.city)) {
            return false;
        }

        if (!TextUtils.equals(countryCode, otherAddress.countryCode)) {
            return false;
        }

        if (!TextUtils.equals(provinceCode, otherAddress.provinceCode)) {
            return false;
        }

        if (!TextUtils.equals(zip, otherAddress.zip)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof Address)) return false;

        Address address = (Address) o;

        if (!locationsAreEqual(address)) {
            return false;
        }

        if (!TextUtils.equals(company, address.company)) {
            return false;
        }

        if (!TextUtils.equals(firstName, address.firstName)) {
            return false;
        }

        if (!TextUtils.equals(lastName, address.lastName)) {
            return false;
        }

        if (!TextUtils.equals(phone, address.phone)) {
            return false;
        }

        return true;
    }

}
