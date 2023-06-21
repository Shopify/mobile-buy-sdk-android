package com.shopify.checkout.fixtures.models

import com.shopify.checkout.models.*
import com.shopify.checkout.models.errors.CheckoutErrorPayload
import com.shopify.checkout.models.errors.CheckoutErrorType
import com.shopify.checkout.models.errors.ErrorGroup
import com.shopify.checkout.models.errors.violations.*
import io.github.serpro69.kfaker.Faker
import java.net.URL
import java.time.LocalDate

val faker = Faker()

internal fun createUrl(path: String = "${faker.random.randomString()}/${faker.random.randomString()}"): URL {
    return URL("https://${faker.internet.domain()}/${path}")
}

internal fun createMoney(
    amount: Float = faker.random.nextFloat(),
    currencyCode: String = faker.currency.code()
): Money {
    return Money(amount, currencyCode)
}

internal fun createCartLine(
    merchandiseId: String = faker.random.nextUUID(),
    productId: String = faker.random.nextUUID(),
    image: CartLineImage = createCartLineImage(altText = null),
    quantity: Int = faker.random.nextInt(),
    title: String = faker.random.randomString(),
    price: Money = createMoney()
): CartLine {
    return CartLine(merchandiseId, productId, image, quantity, title, price)
}

internal fun createCartLineImage(
    sm: String = createUrl("sm").toString(),
    md: String = createUrl("md").toString(),
    lg: String = createUrl("lg").toString(),
    altText: String?
): CartLineImage {
    return CartLineImage(sm, md, lg, altText)
}

internal fun createCartInfo(
    lines: List<CartLine> = listOf(
        createCartLine(),
        createCartLine()
    ),
    price: Money = createMoney()
): CartInfo {
    return CartInfo(lines, price)
}

internal fun createAddress(
    name: String = faker.name.name(),
    firstName: String = faker.name.firstName(),
    lastName: String = faker.name.lastName(),
    postalCode: String = faker.address.postcode(),
    address1: String = faker.address.streetAddress(),
    address2: String = faker.address.secondaryAddress(),
    city: String = faker.address.city(),
    countryCode: String = faker.address.countryCode(),
    zoneCode: String = faker.address.stateAbbr(),
    phone: String = faker.phoneNumber.phoneNumber()
): Address {
    return Address(
        name,
        firstName,
        lastName,
        postalCode,
        address1,
        address2,
        city,
        countryCode,
        zoneCode,
        phone
    )
}

internal fun createPaymentMethod(): PaymentMethod {
    val cardNumber = faker.finance.creditCard("")
    val validExpirationDate = LocalDate.now().plusYears(1)

    return PaymentMethod(
        referenceId = faker.random.randomString(),
        cardType = "visa",
        lastFourDigits =  cardNumber.subSequence(cardNumber.length -4, cardNumber.length).toString(),
        expirationMonth = validExpirationDate.monthValue.toString(),
        expirationYear = validExpirationDate.year.toString(),
        billingAddress = createAddress()
    )
}

internal fun createBuyerInfo(
    email: String = faker.internet.email(),
    selectedPaymentMethod: PaymentMethod = createPaymentMethod(),
    shippingInfo: Address = createAddress()
): BuyerInfo {
    return BuyerInfo(email, selectedPaymentMethod, shippingInfo)
}

internal fun createCheckoutStatePayload(
    cartInfo: CartInfo = createCartInfo(),
    buyerInfo: BuyerInfo = createBuyerInfo()
): CheckoutStatePayload {
    return CheckoutStatePayload(cartInfo, buyerInfo)
}

internal fun createVaultedPaymentErrorPayload(
    code: VaultedPaymentErrorCode = VaultedPaymentErrorCode.InvalidPaymentInfo,
    reason: String = faker.random.randomString(),
    flowType: String = faker.random.randomString()
): VaultedPaymentErrorPayload {
    return VaultedPaymentErrorPayload(flowType, code, reason, group = ErrorGroup.VaultedPayment)
}

internal fun createDeliveryErrorPayload(
    flowType: String = faker.random.randomString(),
    code: DeliveryErrorCode = DeliveryErrorCode.UnshippableProduct,
    reason: String = faker.random.randomString(),
): DeliveryErrorPayload {
    return DeliveryErrorPayload(flowType, code, reason, group = ErrorGroup.Violation, type = ViolationErrorType.Delivery)
}

internal fun createInventoryErrorPayload(
    flowType: String = faker.random.randomString(),
    code: InventoryErrorCode = InventoryErrorCode.InsufficientQuantity,
    reason: String = faker.random.randomString(),
): InventoryErrorPayload {
    return InventoryErrorPayload(flowType, code, reason, group = ErrorGroup.Violation, type = ViolationErrorType.Inventory)
}

internal fun createCheckoutErrorPayload(
    flowType: String = faker.random.randomString(),
    type: CheckoutErrorType = CheckoutErrorType.CustomerPersistence
): CheckoutErrorPayload {
    return CheckoutErrorPayload(flowType, type, group = ErrorGroup.Checkout)
}

internal fun createCheckoutCompletedPayload(
    flowType: String = faker.random.randomString(),
    orderId: String = faker.random.nextUUID(),
    cart: CartInfo = createCartInfo(),
    thankYouPageUrl: String = createUrl("thank_you").toString()
): CheckoutCompletedPayload {
    return CheckoutCompletedPayload(flowType, orderId, cart, thankYouPageUrl)
}

internal fun createCheckoutDefaults(
    email: String? = null,
    shippingAddress: List<Address>? = null,
    paymentMethods: List<PaymentMethod>? = null
): Defaults {
    return Defaults(email, shippingAddress, paymentMethods);
}

internal fun createInitPayload(paymentUrl: String = createUrl("payment_url").toString()): InitPayload {
    return InitPayload(paymentUrl)
}

internal val CHECKOUT_STATE_FAKE: CheckoutStatePayload = createCheckoutStatePayload()
internal val PAYMENT_ERROR_PAYLOAD_FAKE: VaultedPaymentErrorPayload = createVaultedPaymentErrorPayload()
internal val DELIVERY_ERROR_PAYLOAD_FAKE: DeliveryErrorPayload = createDeliveryErrorPayload()
internal val INVENTORY_ERROR_PAYLOAD_FAKE: InventoryErrorPayload = createInventoryErrorPayload()
internal val CHECKOUT_ERROR_PAYLOAD_FAKE: CheckoutErrorPayload = createCheckoutErrorPayload()
internal val CHECKOUT_COMPLETED_PAYLOAD_FAKE: CheckoutCompletedPayload =
    createCheckoutCompletedPayload()
internal val INIT_PAYLOAD_FAKE: InitPayload = createInitPayload()
internal val CHECKOUT_DEFAULTS_FAKE: Defaults = createCheckoutDefaults(
    email = faker.internet.email(),
    shippingAddress = listOf(createAddress()),
    paymentMethods = listOf(createPaymentMethod())
)
