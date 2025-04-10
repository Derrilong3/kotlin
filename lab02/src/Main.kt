//Задача 10. «Конвертер валют»

import java.lang.IllegalArgumentException
import kotlin.math.round

fun convertCurrency(amount: Double, currency: String): Double {
    val rate = when (currency.lowercase().trim()) {
        "usd" -> 0.025
        "eur" -> 0.023
        "pln" -> 0.11
        else -> throw IllegalArgumentException("Невідома валюта: $currency")
    }

    return round(amount * rate * 100) / 100  // округлення до 2 знаків
}

fun main() {
    println("Конвертер валют (грн → usd, eur, pln). Введіть 'exit' щоб завершити.\n")

    while (true) {
        print("Введіть суму в гривнях: ")
        val amountInput = readlnOrNull()

        if (amountInput.equals("exit", ignoreCase = true)) break

        val amount = amountInput?.toDoubleOrNull()
        if (amount == null || amount < 0) {
            println("Некоректна сума. Спробуйте ще раз.\n")
            continue
        }

        print("Оберіть валюту (usd, eur, pln): ")
        val currencyInput = readLine()

        if (currencyInput.equals("exit", ignoreCase = true)) break

        try {
            val result = convertCurrency(amount, currencyInput ?: "")
            println("Результат: $result ${currencyInput?.uppercase()}\n")
        } catch (e: IllegalArgumentException) {
            println("Помилка: ${e.message}\n")
        }
    }

    println("Програма завершена.")
}