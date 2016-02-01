package com.shopify.buy.service;

import com.shopify.buy.data.TestData;
import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.extensions.CheckoutPrivateAPIs;
import com.shopify.buy.extensions.GiftCardPrivateAPIs;
import com.shopify.buy.extensions.ShopifyAndroidTestCase;
import com.shopify.buy.model.Address;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.Checkout;
import com.shopify.buy.model.CheckoutAttribute;
import com.shopify.buy.model.CreditCard;
import com.shopify.buy.model.Discount;
import com.shopify.buy.model.GiftCard;
import com.shopify.buy.model.LineItem;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ShippingRate;

import org.apache.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * The integration level test for the Mobile Buy SDK
 */
public class BuyTest extends ShopifyAndroidTestCase {

    private Checkout checkout;
    private List<ShippingRate> shippingRates;

    public void testApplyingGiftCardToCheckout() throws InterruptedException {
        createValidCheckout();
        applyGiftCardToCheckout(data.getGiftCardCode(TestData.GiftCardType.VALID11), data.getGiftCardValue(TestData.GiftCardType.VALID11));
    }

    public void testApplyingInvalidGiftCardToCheckout() throws InterruptedException {
        createValidCheckout();
        final CountDownLatch latch = new CountDownLatch(1);

        buyClient.applyGiftCard(data.getGiftCardCode(TestData.GiftCardType.INVALID), checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                fail("Retrofit succeeded. Expected: 422 error");
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, error.getResponse().getStatus());
                latch.countDown();
            }
        });
        latch.await();
    }

    public void testApplyingExpiredGiftCardToCheckout() throws InterruptedException {
        createValidCheckout();
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.applyGiftCard(data.getGiftCardCode(TestData.GiftCardType.EXPIRED), checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                fail("Retrofit succeeded. Expected: 422 error");
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, error.getResponse().getStatus());
                latch.countDown();
            }
        });
        latch.await();
    }

    public void testRemovingGiftCardFromCheckout() throws InterruptedException {
        createValidCheckout();
        applyGiftCardToCheckout(data.getGiftCardCode(TestData.GiftCardType.VALID11), data.getGiftCardValue(TestData.GiftCardType.VALID11));
        removeGiftCardFromCheckout(checkout.getGiftCards().get(0));
    }

    public void testRemovingInvalidGiftCardFromCheckout() throws InterruptedException {
        createValidCheckout();
        final CountDownLatch latch = new CountDownLatch(1);
        GiftCardPrivateAPIs invalidGiftCard = new GiftCardPrivateAPIs(data.getGiftCardCode(TestData.GiftCardType.INVALID));
        invalidGiftCard.setId(data.getGiftCardId(TestData.GiftCardType.INVALID));
        buyClient.removeGiftCard(invalidGiftCard, checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                fail("Retrofit succeeded. Expected: 422 error");
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, error.getResponse().getStatus());
                latch.countDown();
            }
        });
        latch.await();
    }

    public void testRemovingExpiredGiftCardFromCheckout() throws InterruptedException {
        createValidCheckout();
        final CountDownLatch latch = new CountDownLatch(1);
        GiftCardPrivateAPIs expiredGiftCard = new GiftCardPrivateAPIs(data.getGiftCardCode(TestData.GiftCardType.EXPIRED));
        expiredGiftCard.setId(data.getGiftCardId(TestData.GiftCardType.EXPIRED));
        buyClient.removeGiftCard(expiredGiftCard, checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                fail("Retrofit succeeded. Expected: 422 error");
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, error.getResponse().getStatus());
                latch.countDown();
            }
        });
        latch.await();
    }

    public void testApplyingTwoGiftCardsToCheckout() throws InterruptedException {
        createValidCheckout();
        applyGiftCardToCheckout(data.getGiftCardCode(TestData.GiftCardType.VALID11), data.getGiftCardValue(TestData.GiftCardType.VALID11));
        applyGiftCardToCheckout(data.getGiftCardCode(TestData.GiftCardType.VALID25), data.getGiftCardValue(TestData.GiftCardType.VALID25));
        assertEquals(checkout.getGiftCards().size(), 2);
    }

    public void testApplyingThreeGiftCardsToCheckout() throws InterruptedException {
        createValidCheckout();
        applyGiftCardToCheckout(data.getGiftCardCode(TestData.GiftCardType.VALID11), data.getGiftCardValue(TestData.GiftCardType.VALID11));
        applyGiftCardToCheckout(data.getGiftCardCode(TestData.GiftCardType.VALID25), data.getGiftCardValue(TestData.GiftCardType.VALID25));
        applyGiftCardToCheckout(data.getGiftCardCode(TestData.GiftCardType.VALID50), data.getGiftCardValue(TestData.GiftCardType.VALID50));
        assertEquals(checkout.getGiftCards().size(), 3);
    }

    public void testRemovingSecondGiftCard() throws InterruptedException {
        testApplyingThreeGiftCardsToCheckout();
        removeGiftCardFromCheckout(checkout.getGiftCards().get(1));
        assertEquals(checkout.getGiftCards().size(), 2);
    }

    public void testRemovingFirstGiftCard() throws InterruptedException {
        testRemovingSecondGiftCard();
        removeGiftCardFromCheckout(checkout.getGiftCards().get(0));
        assertEquals(checkout.getGiftCards().size(), 1);
    }

    public void testRemovingAllGiftCards() throws InterruptedException {
        testRemovingFirstGiftCard();
        removeGiftCardFromCheckout(checkout.getGiftCards().get(0));
        assertEquals(checkout.getGiftCards().size(), 0);

        assertEquals(checkout.getTotalPrice(), checkout.getPaymentDue());
    }

    public void testGetCheckoutWithInvalidToken() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.getCheckout("ZZZZZZZZZZZZZZZ", new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                fail("Retrofit succeeded. Expected: 404");
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(HttpStatus.SC_NOT_FOUND, error.getResponse().getStatus());
                latch.countDown();
            }
        });
        latch.await();
    }

    public void testWithoutShop() {
        try {
            BuyClientFactory.getBuyClient("", getApiKey(), getChannelId(), data.getApplicationName());
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    public void testWithoutAuthToken() {
        try {
            BuyClientFactory.getBuyClient(getShopDomain(), "", getChannelId(), data.getApplicationName());
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    public void testWithoutChannelId() {
        try {
            BuyClientFactory.getBuyClient(getShopDomain(), getApiKey(), "", data.getApplicationName());
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    public void testWithoutApplicationName() {
        try {
            BuyClientFactory.getBuyClient(getShopDomain(), getApiKey(), getChannelId(), "");
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    public void testFetchingShippingRatesWithoutShippingAddress() throws InterruptedException {
        createCheckoutWithNoShippingAddress();
        fetchShippingRates(HttpStatus.SC_PRECONDITION_FAILED);
    }

    public void testFetchingShippingRatesWithInvalidCheckout() throws InterruptedException {
        CheckoutPrivateAPIs privateCheckout = new CheckoutPrivateAPIs(createCart());
        privateCheckout.setToken("bananaaaa");
        checkout = privateCheckout;
        fetchShippingRates(HttpStatus.SC_NOT_FOUND);
    }

    public void testCheckoutFlowUsingCreditCard() throws InterruptedException {
        createValidCheckout();
        fetchShippingRates(HttpStatus.SC_OK);
        setShippingRate();
        addCreditCardToCheckout();
        completeCheckout();
        pollCheckoutCompletionStatus();
        getCheckout();
    }

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

    public void testCreateCheckoutWithValidDiscount() throws InterruptedException {
        final String discountCode = data.getDiscountCode(TestData.DiscountType.VALID);

        final CountDownLatch latch = new CountDownLatch(1);
        Checkout checkout = new Checkout(createCart());
        checkout.setShippingAddress(getShippingAddress());
        checkout.setBillingAddress(checkout.getShippingAddress());
        checkout.setDiscountCode(discountCode);

        buyClient.createCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                assertNotNull(checkout.getDiscount());

                Discount discount = checkout.getDiscount();
                assertEquals(discountCode, discount.getCode());
                assertTrue(discount.isApplicable());

                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
        latch.await();
    }

    public void testCreateCheckoutWithExpiredDiscount() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        Checkout checkout = new Checkout(createCart());
        checkout.setShippingAddress(getShippingAddress());
        checkout.setBillingAddress(checkout.getShippingAddress());
        checkout.setDiscountCode(data.getDiscountCode(TestData.DiscountType.EXPIRED));
        buyClient.createCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                fail("Retrofit succeeded. Expected: 422 error");
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, error.getResponse().getStatus());
                latch.countDown();
            }
        });
        latch.await();
    }

    public void testCreateCheckoutWithNonExistentDiscount() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        Checkout checkout = new Checkout(createCart());
        checkout.setShippingAddress(getShippingAddress());
        checkout.setBillingAddress(checkout.getShippingAddress());
        checkout.setDiscountCode("notarealdiscountasdasfsafasda");
        buyClient.createCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                fail("Retrofit succeeded. Expected: 422 error");
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, error.getResponse().getStatus());
                latch.countDown();
            }
        });
        latch.await();
    }

    public void testUpdateCheckoutWithValidDiscount() throws InterruptedException {
        final String discountCode = data.getDiscountCode(TestData.DiscountType.VALID);

        createValidCheckout();
        final float initialPaymentDue = Float.valueOf(checkout.getPaymentDue());
        checkout.setDiscountCode(discountCode);

        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.updateCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                assertNotNull(checkout.getDiscount());

                Discount discount = checkout.getDiscount();
                assertEquals(discountCode, discount.getCode());
                assertTrue(discount.isApplicable());

                // make sure the payment due amount was adjusted properly and the gift card value is correct
                assertEquals(initialPaymentDue - data.getDiscountValue(TestData.DiscountType.VALID), Float.valueOf(checkout.getPaymentDue()));

                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
        latch.await();
    }

    public void testValidIntegration() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.testIntegration(new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                assertEquals(response.getStatus(), HttpStatus.SC_OK);
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail("Retrofit failed. Expected: success");
            }
        });
        latch.await();
    }

    public void testInvalidIntegrationBadShopUrl() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        BuyClient badBuyClient = getBuyClient("notmyshop.myshopify.com", getApiKey(), getChannelId(), data.getApplicationName());
        badBuyClient.testIntegration(new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                fail("Retrofit succeeded. Expected: 404");
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(error.getResponse().getStatus(), HttpStatus.SC_NOT_FOUND);
                latch.countDown();
            }
        });
        latch.await();
    }

    public void testInvalidIntegrationBadApiKey() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        BuyClient badBuyClient = getBuyClient(getShopDomain(), "12345", getChannelId(), data.getApplicationName());
        badBuyClient.testIntegration(new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                fail("Retrofit succeeded. Expected: 400");
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(error.getResponse().getStatus(), HttpStatus.SC_BAD_REQUEST);
                latch.countDown();
            }
        });
        latch.await();
    }

    public void testInvalidIntegrationBadChannelId() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        BuyClient badBuyClient = getBuyClient(getShopDomain(), getApiKey(), "12345", data.getApplicationName());
        badBuyClient.testIntegration(new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                fail("Retrofit succeeded. Expected: 400");
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(error.getResponse().getStatus(), HttpStatus.SC_BAD_REQUEST);
                latch.countDown();
            }
        });
        latch.await();
    }

    public void testExpiringCheckout() throws InterruptedException {
        createValidCheckout();
        assertEquals(checkout.getReservationTime().longValue(), 300);

        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.removeProductReservationsFromCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                assertEquals(checkout.getReservationTime().longValue(), 0);
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });

        latch.await();
    }

    /**
     * ****************************
     * ***** Helper functions *******
     * *****************************
     */

    private Cart createCart() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        final AtomicReference<Product> productRef = new AtomicReference<>();
        buyClient.getProduct(data.getProductId(), new Callback<Product>() {
            @Override
            public void success(Product product, Response response) {
                productRef.set(product);
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });

        latch.await();

        Cart cart = new Cart();
        cart.addVariant(productRef.get().getVariants().get(0));

        // add some custom properties
        LineItem lineItem = cart.getLineItems().get(0);
        Map<String, String> properties = lineItem.getProperties();
        properties.put("color", "red");
        properties.put("size", "large");

        return cart;
    }

    private void createValidCheckout() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        Checkout checkout = new Checkout(createCart());
        checkout.setShippingAddress(getShippingAddress());
        checkout.setBillingAddress(checkout.getShippingAddress());
        checkout.setEmail("test@test.com");

        buyClient.createCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                validateCreatedCheckout(checkout, response);
                BuyTest.this.checkout = checkout;
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
        latch.await();
    }

    private void createCheckoutWithNoShippingAddress() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        Checkout checkout = new Checkout(createCart());
        checkout.setBillingAddress(checkout.getShippingAddress());
        buyClient.createCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                validateCreatedCheckout(checkout, response);
                BuyTest.this.checkout = checkout;
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
        latch.await();
    }

    private void validateCreatedCheckout(Checkout checkout, Response response) {
        assertEquals(HttpStatus.SC_CREATED, response.getStatus());
        assertNotNull(checkout.getLineItems());
        assertEquals(1, checkout.getLineItems().size());
        assertNotNull(checkout.getLineItems().get(0).getProperties());
        assertEquals(2, checkout.getLineItems().get(0).getProperties().size());
        assertEquals(checkout.getSourceName(), "mobile_app");

        if (!USE_MOCK_RESPONSES) {
            assertEquals(checkout.getSourceIdentifier(), buyClient.getChannelId());
        }
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
        final CountDownLatch latch = new CountDownLatch(1);

        final int initialGiftCardCount;
        if (checkout.getGiftCards() != null) {
            initialGiftCardCount = checkout.getGiftCards().size();
        } else {
            initialGiftCardCount = 0;
        }

        final float initialPaymentDue = Float.valueOf(checkout.getPaymentDue());

        buyClient.applyGiftCard(code, checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {

                int size = checkout.getGiftCards().size();

                GiftCard giftCard = checkout.getGiftCards().get(size - 1);
                float paymentDue = Float.valueOf(checkout.getPaymentDue());
                float giftCardValue = Float.valueOf(giftCard.getBalance());

                assertEquals(HttpStatus.SC_CREATED, response.getStatus());

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
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
        latch.await();
    }

    private void removeGiftCardFromCheckout(final GiftCard giftCard) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final int initialGiftCardCount = checkout.getGiftCards().size();
        final float initialPaymentDue = Float.valueOf(checkout.getPaymentDue());

        buyClient.removeGiftCard(giftCard, checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                assertEquals(HttpStatus.SC_OK, response.getStatus());
                assertEquals(initialGiftCardCount - 1, checkout.getGiftCards().size());

                float paymentDue = Float.valueOf(checkout.getPaymentDue());
                float giftCardValue = Float.valueOf(giftCard.getBalance());

                assertEquals(initialPaymentDue + giftCardValue, paymentDue);
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail("Retrofit failed. Expected success.");
            }
        });
        latch.await();
    }

    private void fetchShippingRates(int expectedStatus) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        pollShippingRates(expectedStatus, latch);
        latch.await();
    }

    private void pollShippingRates(final int expectedStatus, final CountDownLatch latch) {
        assertNotNull(checkout);
        Callback<List<ShippingRate>> callback = new Callback<List<ShippingRate>>() {
            @Override
            public void success(List<ShippingRate> shippingRates, Response response) {
                if (response.getStatus() != expectedStatus) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pollShippingRates(expectedStatus, latch);
                } else {
                    assertNotNull(shippingRates);
                    BuyTest.this.shippingRates = shippingRates;
                    latch.countDown();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                assertEquals(error.getResponse().getStatus(), expectedStatus);
                latch.countDown();
            }
        };
        buyClient.getShippingRates(checkout.getToken(), callback);
    }

    private void setShippingRate() throws InterruptedException {
        checkout.setShippingRate(shippingRates.get(0));
        updateCheckout();
    }

    private void updateCheckout() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.updateCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                assertEquals(HttpStatus.SC_OK, response.getStatus());
                assertNotNull(checkout);
                assertEquals("test@test.com", checkout.getEmail());
                BuyTest.this.checkout = checkout;
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
        latch.await();
    }

    private void addCreditCardToCheckout() throws InterruptedException {
        CreditCard card = new CreditCard();
        card.setFirstName("John");
        card.setLastName("Smith");
        card.setMonth("5");
        card.setYear("20");
        card.setVerificationValue("123");
        card.setNumber("4242424242424242");

        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.storeCreditCard(card, checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                assertEquals(HttpStatus.SC_OK, response.getStatus());
                BuyTest.this.checkout = checkout;
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
        latch.await();
    }

    private void completeCheckout() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.completeCheckout(checkout, new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                assertEquals(HttpStatus.SC_OK, response.getStatus());
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
        latch.await();
    }

    private void pollCheckoutCompletionStatus() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        recurseCheckoutCompletionPoll(latch);
        latch.await();
    }

    private void recurseCheckoutCompletionPoll(final CountDownLatch latch) {
        final Callback<Boolean> callback = new Callback<Boolean>() {
            @Override
            public void success(Boolean complete, Response response) {
                if (complete) {
                    latch.countDown();
                } else {
                    recurseCheckoutCompletionPoll(latch);
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
        final CountDownLatch latch = new CountDownLatch(1);
        buyClient.getCheckout(checkout.getToken(), new Callback<Checkout>() {
            @Override
            public void success(Checkout checkout, Response response) {
                assertNotNull(checkout);
                assertNotNull(checkout.getOrder().getId());
                assertNotNull(checkout.getOrder().getStatusUrl());
                assertNotNull(checkout.getOrder().getName());
                latch.countDown();
            }

            @Override
            public void failure(RetrofitError error) {
                fail(BuyClient.getErrorBody(error));
            }
        });
        latch.await();
    }

}
