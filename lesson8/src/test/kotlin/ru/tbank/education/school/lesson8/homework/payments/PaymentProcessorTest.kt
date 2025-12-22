package ru.tbank.education.school.lesson8.homework.payments

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Assertions.*

class PaymentProcessorTest {
    private lateinit var processor: PaymentProcessor

    @BeforeEach
    fun setUp() {
        processor = PaymentProcessor()
    }

    @Test
    @DisplayName("Проверка на сумму перевода")
    fun check_amount() {
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(
                amount = 0,
                cardNumber = "4242424242424242",
                expiryMonth = 12,
                expiryYear = 2027,
                currency = "USD",
                customerId = "customer123"
            )
        }
    }

    @Test
    @DisplayName("Проверка длины номера карты")
    fun check_card_number_length() {
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(
                amount = 100,
                cardNumber = "424",
                expiryMonth = 12,
                expiryYear = 2027,
                currency = "USD",
                customerId = "customer123"
            )
        }
    }

    @Test
    @DisplayName("Проверка длины номера карты(2)")
    fun check_card_number_length2() {
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(
                amount = 100,
                cardNumber = "4242442242424242424242424242",
                expiryMonth = 12,
                expiryYear = 2027,
                currency = "USD",
                customerId = "customer123"
            )
        }
    }

    @Test
    @DisplayName("Проверка формата номера карты")
    fun check_card_number_format() {
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(
                amount = 100,
                cardNumber = "4242 4242 4242 4242",
                expiryMonth = 12,
                expiryYear = 2027,
                currency = "USD",
                customerId = "customer123"
            )
        }
    }


    @Test
    @DisplayName("Проверка наличия валюты перевода")
    fun check_currency() {
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(
                amount = 100,
                cardNumber = "4242424242424242",
                expiryMonth = 12,
                expiryYear = 2027,
                currency = "",
                customerId = "customer123"
            )
        }
    }


    @Test
    @DisplayName("Проверка наличия данных пользователя")
    fun check_customer_data() {
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(
                amount = 100,
                cardNumber = "4242424242424242",
                expiryMonth = 12,
                expiryYear = 2027,
                currency = "USD",
                customerId = ""
            )
        }
    }

    @Test
    @DisplayName("Проверка cрока действия карты по году")
    fun check_card_expiry() {
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(
                amount = 100,
                cardNumber = "4242424242424242",
                expiryMonth = 12,
                expiryYear = 2024,
                currency = "USD",
                customerId = "customer123"
            )
        }
    }

    @Test
    @DisplayName("Проверка cрока действия карты по месяцу")
    fun check_card_expiry_month() {
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(
                amount = 100,
                cardNumber = "4242424242424242",
                expiryMonth = 0,
                expiryYear = 2025,
                currency = "USD",
                customerId = "customer123"
            )
        }
    }

    @Test
    @DisplayName("Проверка cрока действия карты по месяцу(2)")
    fun check_card_expiry_month2() {
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(
                amount = 100,
                cardNumber = "4242424242424242",
                expiryMonth = 13,
                expiryYear = 2025,
                currency = "USD",
                customerId = "customer123"
            )
        }
    }

    @Test
    @DisplayName("Проверка cрока действия карты по месяцу(3)")
    fun check_card_expiry_month3() {
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(
                amount = 100,
                cardNumber = "4242424242424242",
                expiryMonth = 14,
                expiryYear = 2026,
                currency = "USD",
                customerId = "customer123"
            )
        }
    }

    @Test
    @DisplayName("Проверка cрока действия карты по месяцу(4)")
    fun check_card_expiry_month4() {
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(
                amount = 100,
                cardNumber = "4242424242424242",
                expiryMonth = 0,
                expiryYear = 2026,
                currency = "USD",
                customerId = "customer123"
            )
        }
    }

    @Test
    @DisplayName("Проверка cрока действия карты по нынешней дате")
    fun check_card_expiry_date() {
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(
                amount = 100,
                cardNumber = "4242424242424242",
                expiryMonth = 10,
                expiryYear = 2025,
                currency = "USD",
                customerId = "customer123"
            )
        }
    }

    @Test
    @DisplayName("Проверка наличия номера карты")
    fun check_card_number_is() {
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(
                amount = 100,
                cardNumber = "",
                expiryMonth = 12,
                expiryYear = 2027,
                currency = "USD",
                customerId = "customer123"
            )
        }
    }

    @Test
    @DisplayName("Проверка  номера карты на подозрительные")
    fun check_card_number_is_strange() {
        assertEquals(
            "REJECTED",
            processor.processPayment(
                amount = 100,
                cardNumber = "44444242424242",
                expiryMonth = 12,
                expiryYear = 2027,
                currency = "USD",
                customerId = "customer123"
            ).status
        )
    }

    @Test
    @DisplayName("Проверка даты действия карты")
    fun check_card_expiry_date_2() {
        assertEquals(
            "SUCCESS",
            processor.processPayment(
                amount = 100,
                cardNumber = "42424242424242",
                expiryMonth = 11,
                expiryYear = 2025,
                currency = "USD",
                customerId = "customer123"
            ).status
        )
    }

    @Test
    @DisplayName("Проверка даты действия карты2")
    fun check_card_expiry_date_3() {
        assertEquals(
            "SUCCESS",
            processor.processPayment(
                amount = 100,
                cardNumber = "42424242424242",
                expiryMonth = 11,
                expiryYear = 2026,
                currency = "USD",
                customerId = "customer123"
            ).status
        )
    }


    @Test
    @DisplayName("Проверка валидного по Луна")
    fun check_luhn_valid() {
        assertEquals(
            "SUCCESS", processor.processPayment(
                amount = 100,
                cardNumber = "4242424242424242",
                expiryMonth = 12,
                expiryYear = 2027,
                currency = "USD",
                customerId = "customer123"
            ).status
        )
    }

    @Test
    @DisplayName("Проверка невалидного по Луна")
    fun check_luhn_invalid() {
        val transaction = processor.processPayment(
            amount = 100,
            cardNumber = "4242424242424241",
            expiryMonth = 12,
            expiryYear = 2027,
            currency = "USD",
            customerId = "customer123"
        )
        assertEquals("REJECTED", transaction.status)
    }

    @Test
    @DisplayName("Проверка на Gateway — timeout")
    fun gateway_timeout_check() {
        val transaction = processor.processPayment(
            amount = 34,
            cardNumber = "4242424242424242",
            expiryMonth = 12,
            expiryYear = 2027,
            currency = "USD",
            customerId = "customer123"
        )
        assertEquals("FAILED", transaction.status)
        assertEquals("Gateway timeout", transaction.message)

    }


    @Test
    @DisplayName("Проверка на 5500")
    fun gateway_insufficient_funds() {
        val transaction = processor.processPayment(
            amount = 100,
            cardNumber = "5500000000004",
            expiryMonth = 12,
            expiryYear = 2027,
            currency = "USD",
            customerId = "customer123"
        )
        assertEquals("FAILED", transaction.status)
        assertEquals("Insufficient funds", transaction.message)
    }




    @Test
    @DisplayName("Проверка лимита операции")
    fun gateway_limit_exceeded() {
        val transaction = processor.processPayment(
            amount = 500000,
            cardNumber = "4242424242424242",
            expiryMonth = 12,
            expiryYear = 2027,
            currency = "EUR",
            customerId = "customer123"
        )
        assertEquals("FAILED", transaction.status)
    }

    @Test
    @DisplayName("Проверка конвертации EUR")
    fun check_currency_eur() {
        val transaction = processor.processPayment(
            amount = 1000,
            cardNumber = "4242424242424242",
            expiryMonth = 12,
            expiryYear = 2027,
            currency = "EUR",
            customerId = "customer123"
        )
        assertEquals("SUCCESS", transaction.status)
    }

    @Test
    @DisplayName("Проверка конвертации JPY")
    fun check_currency_jpy() {
        val transaction = processor.processPayment(
            amount = 100,
            cardNumber = "4242424242424242",
            expiryMonth = 12,
            expiryYear = 2027,
            currency = "JPY",
            customerId = "customer123"
        )
        assertEquals("SUCCESS", transaction.status)
    }

    @Test
    @DisplayName("Проверка конвертации GBP")
    fun check_currency_gbp() {
        val transaction = processor.processPayment(
            amount = 100,
            cardNumber = "4242424242424242",
            expiryMonth = 12,
            expiryYear = 2027,
            currency = "GBP",
            customerId = "customer123"
        )
        assertEquals("SUCCESS", transaction.status)
    }

    @Test
    @DisplayName("Проверка конвертации RUB")
    fun check_currency_rub() {
        val transaction = processor.processPayment(
            amount = 100,
            cardNumber = "4242424242424242",
            expiryMonth = 12,
            expiryYear = 2027,
            currency = "RUB",
            customerId = "customer123"
        )
        assertEquals("SUCCESS", transaction.status)
    }

    @Test
    @DisplayName("Проверка конвертации другой валюты (AUD)")
    fun check_currency_aud() {
        val transaction = processor.processPayment(
            amount = 100,
            cardNumber = "4242424242424242",
            expiryMonth = 12,
            expiryYear = 2027,
            currency = "AUD",
            customerId = "customer123"
        )
        assertEquals("SUCCESS", transaction.status)
    }

    @Test
    @DisplayName("Проверка на пустой список")
    fun bulk_empty_check() {
        val transaction = processor.bulkProcess(emptyList())
        assertEquals(0, transaction.size)
    }

    @Test
    @DisplayName("Проверка уровней скидки")
    fun loyalty_levels_check() {
        assertEquals(5000, processor.calculateLoyaltyDiscount(points = 20000, baseAmount = 50000))
        assertEquals(3000, processor.calculateLoyaltyDiscount(points = 7000, baseAmount = 50000))
        assertEquals(1500, processor.calculateLoyaltyDiscount(points = 3000, baseAmount = 50000))
        assertEquals(500, processor.calculateLoyaltyDiscount(points = 600, baseAmount = 50000))
        assertEquals(0, processor.calculateLoyaltyDiscount(points = 100, baseAmount = 50000))
        assertThrows(IllegalArgumentException::class.java) {
            processor.calculateLoyaltyDiscount(points = 0, baseAmount = 0)
        }
    }

    @Test
    @DisplayName("Проверка пакетных запросов с разными статусами")
    fun bulk_mixed_status() {
        val transactions = listOf(
            PaymentData(340, "4242424242424242", 12, 2027, "USD", "customer1"),
            PaymentData(100, "44444242424242", 12, 2027, "USD", "customer2"),
            PaymentData(150, "4242424242424242", 12, 2027, "USD", "customer3")
        )
        val result = processor.bulkProcess(transactions)
        assertEquals("FAILED", result[0].status)
        assertEquals("REJECTED", result[1].status)
        assertEquals("SUCCESS", result[2].status)
    }


    @Test
    @DisplayName("Ошибка при пакетном запросе с валютой")
    fun bulk_currency_error_failed() {
        val transactions = PaymentData(
            amount = 100,
            cardNumber = "4242424242424242",
            expiryMonth = 12,
            expiryYear = 2027,
            currency = "",
            customerId = "customer123"
        )
        val results = processor.bulkProcess(listOf(transactions))
        assertEquals("REJECTED", results[0].status)
    }
}
