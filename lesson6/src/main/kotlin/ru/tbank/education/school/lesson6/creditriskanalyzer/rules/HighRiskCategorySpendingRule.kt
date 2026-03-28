package ru.tbank.education.school.lesson6.creditriskanalyzer.rules

import ru.tbank.education.school.lesson6.creditriskanalyzer.models.Client
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.PaymentRisk
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.ScoringResult
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.TransactionCategory
import ru.tbank.education.school.lesson6.creditriskanalyzer.repositories.TransactionRepository

/**
 * Анализирует долю расходов клиента в рисковых категориях.
 *
 * Идея:
 * - Получить все транзакции клиента.
 * - Исключить доходы (SALARY).
 * - Посчитать общие сумму переводов в категориях GAMBLING, CRYPTO, TRANSFER.
 * - Посчитать общие сумму всех переводов (без SALARY).
 * - Рассчитать долю переводов в категориях GAMBLING, CRYPTO, TRANSFER.
 *
 * Как считать risk:
 * - Если доля > 0.6 → HIGH
 * - Если доля > 0.3 → MEDIUM
 * - Иначе → LOW
 */
class HighRiskCategorySpendingRule(
    private val transactionRepo: TransactionRepository
) : ScoringRule {

    override val ruleName: String = "High-Risk Category Spending"

    override fun evaluate(client: Client): ScoringResult {
        val transactions = transactionRepo.getTransactions(client.id)
        var totalAmount : Long = 0
        var strangeAmount : Long = 0
        for (transaction in transactions) {
            if (transaction.category == TransactionCategory.SALARY) continue
            if (transaction.category == TransactionCategory.CRYPTO) strangeAmount += transaction.amount
            if (transaction.category == TransactionCategory.GAMBLING) strangeAmount += transaction.amount
            if (transaction.category == TransactionCategory.TRANSFER) strangeAmount += transaction.amount
            totalAmount += transaction.amount
        }
        if (totalAmount == 0L) return ScoringResult(ruleName, PaymentRisk.LOW)
        val risk = when {
            strangeAmount.toDouble() / totalAmount.toDouble() > 0.6 -> PaymentRisk.HIGH
            strangeAmount.toDouble() / totalAmount.toDouble() > 0.3 -> PaymentRisk.MEDIUM
            else -> PaymentRisk.LOW
        }
        return ScoringResult(
            ruleName,
            risk
        )

    }
}
