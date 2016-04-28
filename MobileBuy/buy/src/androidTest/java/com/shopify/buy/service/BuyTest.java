package com.shopify.buy.service;

import android.support.test.runner.AndroidJUnit4;

import com.shopify.buy.data.TestData;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.dataprovider.RetrofitError;
import com.shopify.buy.extensions.CheckoutPrivateAPIs;
import com.shopify.buy.extensions.GiftCardPrivateAPIs;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.CreditCard;
import com.shopify.buy.model.Discount;
import com.shopify.buy.model.GiftCard;
import com.shopify.buy.model.LineItem;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ShippingRate;

import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;


/**
 * The integration level test for the Mobile Buy SDK
 */
@RunWith(AndroidJUnit4.class)
public class BuyTest extends ShopifyAndroidTestCase {

    private Checkout checkout;
    private List<ShippingRate> shippingRates;

    @Test
    public void testApplyingGiftCardToCheckout() throws InterruptedException {
        createValidCheckout();
        applyGiftCardToCheckout(data.getGiftCardCode(TestData.GiftCardType.VALID11), data.getGiftCardValue(TestData.GiftCardType.VALID11));

        updateCheckout();
        assertEquals(1, checkout.getGiftCards().size());
    }

    @Test
    public void testApplyingInvalidGiftCardToCheckout() throws InterruptedException {
        createValidCheckout();
        buyClient.applyGiftCard(data.getGiftCardCode(TestData.GiftCardType.INVALID), checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                fail("Retrofit succeeded. Expected: 422 error");
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, error.getResponse().code());
            }
        });
    }

    @Test
    public void testApplyingExpiredGiftCardToCheckout() throws InterruptedException {
        createValidCheckout();
        buyClient.applyGiftCard(data.getGiftCardCode(TestData.GiftCardType.EXPIRED), checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                fail("Retrofit succeeded. Expected: 422 error");
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, error.getResponse().code());
            }
        });
    }

    @Test
    public void testRemovingGiftCardFromCheckout() throws InterruptedException {
        createValidCheckout();
        applyGiftCardToCheckout(data.getGiftCardCode(TestData.GiftCardType.VALID11), data.getGiftCardValue(TestData.GiftCardType.VALID11));
        removeGiftCardFromCheckout(checkout.getGiftCards().get(0));
    }

    @Test
    public void testRemovingInvalidGiftCardFromCheckout() throws InterruptedException {
        createValidCheckout();
        GiftCardPrivateAPIs invalidGiftCard = new GiftCardPrivateAPIs(data.getGiftCardCode(TestData.GiftCardType.INVALID));
        invalidGiftCard.setId(data.getGiftCardId(TestData.GiftCardType.INVALID));
        buyClient.removeGiftCard(invalidGiftCard, checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                fail("Retrofit succeeded. Expected: 422 error");
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, error.getResponse().code());
            }
        });
    }

    @Test
    public void testRemovingExpiredGiftCardFromCheckout() throws InterruptedException {
        createValidCheckout();
        GiftCardPrivateAPIs expiredGiftCard = new GiftCardPrivateAPIs(data.getGiftCardCode(TestData.GiftCardType.EXPIRED));
        expiredGiftCard.setId(data.getGiftCardId(TestData.GiftCardType.EXPIRED));
        buyClient.removeGiftCard(expiredGiftCard, checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                fail("Retrofit succeeded. Expected: 422 error");
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, error.getResponse().code());
            }
        });
    }

    @Test
    public void testApplyingTwoGiftCardsToCheckout() throws InterruptedException {
        createValidCheckout();
        applyGiftCardToCheckout(data.getGiftCardCode(TestData.GiftCardType.VALID11), data.getGiftCardValue(TestData.GiftCardType.VALID11));
        applyGiftCardToCheckout(data.getGiftCardCode(TestData.GiftCardType.VALID25), data.getGiftCardValue(TestData.GiftCardType.VALID25));
        assertEquals(checkout.getGiftCards().size(), 2);
    }

    @Test
    public void testApplyingThreeGiftCardsToCheckout() throws InterruptedException {
        createValidCheckout();
        applyGiftCardToCheckout(data.getGiftCardCode(TestData.GiftCardType.VALID11), data.getGiftCardValue(TestData.GiftCardType.VALID11));
        applyGiftCardToCheckout(data.getGiftCardCode(TestData.GiftCardType.VALID25), data.getGiftCardValue(TestData.GiftCardType.VALID25));
        applyGiftCardToCheckout(data.getGiftCardCode(TestData.GiftCardType.VALID50), data.getGiftCardValue(TestData.GiftCardType.VALID50));
        assertEquals(checkout.getGiftCards().size(), 3);
    }

    @Test
    public void testRemovingSecondGiftCard() throws InterruptedException {
        testApplyingThreeGiftCardsToCheckout();
        removeGiftCardFromCheckout(checkout.getGiftCards().get(1));
        assertEquals(checkout.getGiftCards().size(), 2);
    }

    @Test
    public void testRemovingFirstGiftCard() throws InterruptedException {
        testRemovingSecondGiftCard();
        removeGiftCardFromCheckout(checkout.getGiftCards().get(0));
        assertEquals(checkout.getGiftCards().size(), 1);
    }

    @Test
    public void testRemovingAllGiftCards() throws InterruptedException {
        testRemovingFirstGiftCard();
        removeGiftCardFromCheckout(checkout.getGiftCards().get(0));
        assertEquals(checkout.getGiftCards().size(), 0);

        assertEquals(checkout.getTotalPrice(), checkout.getPaymentDue());
    }

    @Test
	public void testGetCheckoutWithInvalidToken() throws InterruptedException {
        buyClient.getCheckout("ZZZZZZZZZZZZZZZ", new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                fail("Retrofit succeeded. Expected: 404");
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(HttpStatus.SC_NOT_FOUND, error.getResponse().code());
            }
        });
    }

    @Test
    public void testWithoutShop() {
        try {
            BuyClientFactory.getBuyClient("", getApiKey(), getAppId(), data.getApplicationName());
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testWithoutAuthToken() {
        try {
            BuyClientFactory.getBuyClient(getShopDomain(), "", getAppId(), data.getApplicationName());
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testWithoutApplicationName() {
        try {
            BuyClientFactory.getBuyClient(getShopDomain(), getApiKey(), getAppId(), "");
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testFetchingShippingRatesWithoutShippingAddress() throws InterruptedException {
        createCheckoutWithNoShippingAddress();
        fetchShippingRates(HttpStatus.SC_PRECONDITION_FAILED);
    }

    @Test
    public void testFetchingShippingRatesWithInvalidCheckout() throws InterruptedException {
        CheckoutPrivateAPIs privateCheckout = new CheckoutPrivateAPIs(createCart());
        privateCheckout.setToken("bananaaaa");
        checkout = privateCheckout;
        fetchShippingRates(HttpStatus.SC_NOT_FOUND);
    }

    @Test
	public void testCreateCheckoutWithVariantID() throws InterruptedException {
        Checkout checkout = new Checkout(new LineItem(getVariantID(), true, 1));
        checkout.setShippingAddress(getShippingAddress());
        checkout.setBillingAddress(checkout.getShippingAddress());
        checkout.setEmail("test@test.com");

        buyClient.createCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                validateCheckoutCreatedWithVariantID(checkout);
                BuyTest.this.checkout = checkout;
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
    }

    @Test
    public void testCheckoutFlowUsingCreditCard() throws InterruptedException {
        createValidCheckout();
        fetchShippingRates(HttpStatus.SC_OK);
        setShippingRate();
        addCreditCardToCheckout();
        completeCheckout();
        pollCheckoutCompletionStatus();
        getCheckout();
    }

    @Test
    public void testChangedShippingAddress() throws InterruptedException {
        createValidCheckout();
        fetchShippingRates(HttpStatus.SC_OK);
        setShippingRate();

        Address address = checkout.getShippingAddress();
        address.setCity("Toronto");

        checkout.setShippingRate(null);
        updateCheckout();

        fetchShippingRates(HttpStatus.SC_OK);
        setShippingRate();

        assertEquals(checkout.getShippingAddress().getCity(), "Toronto");
    }

    @Test
    public void testCreateCheckoutWithValidDiscount() throws InterruptedException {
        final String discountCode = data.getDiscountCode(TestData.DiscountType.VALID);

        Checkout checkout = new Checkout(createCart());
        checkout.setShippingAddress(getShippingAddress());
        checkout.setBillingAddress(checkout.getShippingAddress());
        checkout.setDiscountCode(discountCode);

        buyClient.createCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                assertNotNull(checkout.getDiscount());

                Discount discount = checkout.getDiscount();
                assertEquals(discountCode, discount.getCode());
                assertTrue(discount.isApplicable());
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
    }

    @Test
	public void testCreateCheckoutWithExpiredDiscount() throws InterruptedException {
        Checkout checkout = new Checkout(createCart());
        checkout.setShippingAddress(getShippingAddress());
        checkout.setBillingAddress(checkout.getShippingAddress());
        checkout.setDiscountCode(data.getDiscountCode(TestData.DiscountType.EXPIRED));
        buyClient.createCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                fail("Retrofit succeeded. Expected: 422 error");
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, error.getResponse().code());
            }
        });
    }

    @Test
    public void testCreateCheckoutWithNonExistentDiscount() throws InterruptedException {
        Checkout checkout = new Checkout(createCart());
        checkout.setShippingAddress(getShippingAddress());
        checkout.setBillingAddress(checkout.getShippingAddress());
        checkout.setDiscountCode("notarealdiscountasdasfsafasda");
        buyClient.createCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                fail("Retrofit succeeded. Expected: 422 error");
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, error.getResponse().code());
            }
        });
    }

    @Test
    public void testUpdateCheckoutWithValidDiscount() throws InterruptedException {
        final String discountCode = data.getDiscountCode(TestData.DiscountType.VALID);

        createValidCheckout();
        final float initialPaymentDue = Float.valueOf(checkout.getPaymentDue());
        checkout.setDiscountCode(discountCode);

        buyClient.updateCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                assertNotNull(checkout.getDiscount());

                Discount discount = checkout.getDiscount();
                assertEquals(discountCode, discount.getCode());
                assertTrue(discount.isApplicable());

                // make sure the payment due amount was adjusted properly and the gift card value is correct
                assertEquals(initialPaymentDue - data.getDiscountValue(TestData.DiscountType.VALID), Float.valueOf(checkout.getPaymentDue()));
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
    }

    @Test
    public void testExpiringCheckout() throws InterruptedException {
        createValidCheckout();

        assertEquals(checkout.getReservationTime().longValue(), 300);

        // Create a copy of the checkout before we do the update so we can ensure that only the reservation time changed
        final CheckoutPrivateAPIs before = CheckoutPrivateAPIs.fromCheckout(checkout);
        before.setReservationTime(0);
        before.setReservationTimeLeft(0l);

        buyClient.removeProductReservationsFromCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                assertEquals(checkout.getReservationTime().longValue(), 0);

                // make sure that only the reservation time changed.
                assertEquals(before.toJsonString(), checkout.toJsonString());
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
    }

    /**
     * ****************************
     * ***** Helper functions *******
     * *****************************
     */

    private Cart createCart() throws InterruptedException {
        final AtomicReference<Product> productRef = new AtomicReference<>();
        buyClient.getProduct(data.getProductId(), new Callback<Product>() {
            @Override
            public void success(Product product) {
                productRef.set(product);
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });

        Cart cart = new Cart();
        cart.addVariant(productRef.get().getVariants().get(0));

        // add some custom properties
        LineItem lineItem = cart.getLineItems().get(0);
        Map<String, String> properties = lineItem.getProperties();
        properties.put("color", "red");
        properties.put("size", "large");

        return cart;
    }

    private Long getVariantID() throws InterruptedException {
        final AtomicReference<Product> productRef = new AtomicReference<>();
        buyClient.getProduct(data.getProductId(), new Callback<Product>() {
            @Override
            public void success(Product product) {
                productRef.set(product);
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });

        return productRef.get().getVariants().get(0).getId();
    }

    private void createValidCheckout() throws InterruptedException {
        Checkout checkout = new Checkout(createCart());
        checkout.setShippingAddress(getShippingAddress());
        checkout.setBillingAddress(checkout.getShippingAddress());
        checkout.setEmail("test@test.com");

        buyClient.createCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                validateCreatedCheckout(checkout);
                BuyTest.this.checkout = checkout;
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
    }

    private void createCheckoutWithNoShippingAddress() throws InterruptedException {
        Checkout checkout = new Checkout(createCart());
        checkout.setBillingAddress(checkout.getShippingAddress());
        buyClient.createCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                validateCreatedCheckout(checkout);
                BuyTest.this.checkout = checkout;
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
    }

    private void validateCreatedCheckout(Checkout checkout) {
        assertNotNull(checkout.getLineItems());
        assertEquals(1, checkout.getLineItems().size());
        assertNotNull(checkout.getLineItems().get(0).getProperties());
        assertEquals(2, checkout.getLineItems().get(0).getProperties().size());
        assertEquals(checkout.getSourceName(), "mobile_app");
    }

    private void validateCheckoutCreatedWithVariantID(Checkout checkout) {
        assertNotNull(checkout.getLineItems());
        assertEquals(1, checkout.getLineItems().size());
        assertEquals(checkout.getSourceName(), "mobile_app");
    }

    private Address getShippingAddress() {
        Address shippingAddress = new Address();
        shippingAddress.setAddress1("150 Elgin Street");
        shippingAddress.setAddress2("8th Floor");
        shippingAddress.setCity("Ottawa");
        shippingAddress.setProvinceCode("ON");
        shippingAddress.setCompany("Shopify Inc.");
        shippingAddress.setFirstName("MobileBuy");
        shippingAddress.setLastName("TestBot");
        shippingAddress.setPhone("1-555-555-5555");
        shippingAddress.setCountryCode("CA");
        shippingAddress.setZip("K1N5T5");
        return shippingAddress;
    }

    private void applyGiftCardToCheckout(final String code, final float value) throws InterruptedException {
        final int initialGiftCardCount;
        if (checkout.getGiftCards() != null) {
            initialGiftCardCount = checkout.getGiftCards().size();
        } else {
            initialGiftCardCount = 0;
        }

        final float initialPaymentDue = Float.valueOf(checkout.getPaymentDue());

        buyClient.applyGiftCard(code, checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {

                BuyTest.this.checkout = checkout;

                int size = checkout.getGiftCards().size();

                GiftCard giftCard = checkout.getGiftCards().get(size - 1);
                float paymentDue = Float.valueOf(checkout.getPaymentDue());
                float giftCardValue = Float.valueOf(giftCard.getBalance());

                // make sure the code is the same
                int startIndex = code.length() - giftCard.getLastCharacters().length();
                int endIndex = code.length();
                String lastCharacters = code.substring(startIndex, endIndex);
                assertEquals(lastCharacters.toUpperCase(), giftCard.getLastCharacters().toUpperCase());

                // check that the count actually went up
                assertEquals(size, initialGiftCardCount + 1);

                // make sure the payment due amount was adjusted properly and the gift card value is correct
                assertEquals(initialPaymentDue - giftCardValue, paymentDue);
                assertEquals(value, giftCardValue);
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
    }

    private void removeGiftCardFromCheckout(final GiftCard giftCard) throws InterruptedException {
        final int initialGiftCardCount = checkout.getGiftCards().size();
        final float initialPaymentDue = Float.valueOf(checkout.getPaymentDue());

        buyClient.removeGiftCard(giftCard, checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                assertEquals(initialGiftCardCount - 1, checkout.getGiftCards().size());

                float paymentDue = Float.valueOf(checkout.getPaymentDue());
                float giftCardValue = Float.valueOf(giftCard.getBalance());

                assertEquals(initialPaymentDue + giftCardValue, paymentDue);
            }

            @Override
            public void failure(RetrofitError error) {
                fail("Retrofit failed. Expected success.");
            }
        });
    }

    private void fetchShippingRates(int expectedStatus) throws InterruptedException {
        pollShippingRates(expectedStatus);
    }

    private void pollShippingRates(final int expectedStatus) {
        assertNotNull(checkout);
        Callback<List<ShippingRate>> callback = new Callback<List<ShippingRate>>() {
            @Override
            public void success(List<ShippingRate> shippingRates) {
                assertNotNull(shippingRates);
                BuyTest.this.shippingRates = shippingRates;
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(error.getResponse().code(), expectedStatus);
            }
        };
        buyClient.getShippingRates(checkout.getToken(), callback);
    }

    private void setShippingRate() throws InterruptedException {
        checkout.setShippingRate(shippingRates.get(0));
        updateCheckout();
    }

    private void updateCheckout() throws InterruptedException {
        buyClient.updateCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                assertNotNull(checkout);
                assertEquals("test@test.com", checkout.getEmail());
                BuyTest.this.checkout = checkout;
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
    }

    private void addCreditCardToCheckout() throws InterruptedException {
        CreditCard card = new CreditCard();
        card.setFirstName("John");
        card.setLastName("Smith");
        card.setMonth("5");
        card.setYear("20");
        card.setVerificationValue("123");
        card.setNumber("4242424242424242");

        buyClient.storeCreditCard(card, checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                BuyTest.this.checkout = checkout;
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
    }

    private void completeCheckout() throws InterruptedException {
        buyClient.completeCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
    }

    private void pollCheckoutCompletionStatus() throws InterruptedException {
        recurseCheckoutCompletionPoll();
    }

    private void recurseCheckoutCompletionPoll() {
        final Callback<Boolean> callback = new Callback<Boolean>() {
            @Override
            public void success(Boolean complete) {
                if (complete) {
                } else {
                    recurseCheckoutCompletionPoll();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        };
        buyClient.getCheckoutCompletionStatus(checkout, callback);
    }

    private void getCheckout() throws InterruptedException {
        buyClient.getCheckout(checkout.getToken(), new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout) {
                assertNotNull(checkout);
                assertNotNull(checkout.getOrder().getId());
                assertNotNull(checkout.getOrder().getStatusUrl());
                assertNotNull(checkout.getOrder().getName());
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
    }

}
