package ru.tbank.education.school.lesson6.creditriskanalyzer.rules

import ru.tbank.education.school.lesson6.creditriskanalyzer.models.Client
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.PaymentRisk
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.ScoringResult
import ru.tbank.education.school.lesson6.creditriskanalyzer.repositories.AccountRepository

/**
 *
 * Идея:
 * - Проверить возраст клиента
 *
 * Как считать score:
 * - Если от 14 до 18  лет → HIGH
 * - Если от 18 (включительно) до 23 лет → MEDIUM
 * - Иначе → LOW
 *
 */
class AgeRule(
    private val accountRepo: AccountRepository
) : ScoringRule {

    override val ruleName: String = "Age check"
    override fun evaluate(client: Client): ScoringResult {
        val risk = when {
            client.age < 18 -> PaymentRisk.HIGH
            client.age < 23 -> PaymentRisk.MEDIUM
            else -> PaymentRisk.LOW
        }
        return ScoringResult(
            ruleName,
            risk
        )

    }
}