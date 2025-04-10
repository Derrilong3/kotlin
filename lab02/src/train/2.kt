package train

import kotlin.math.sqrt

// Перевірити, чи є задане число простим.
fun isPrime(number: Int): Boolean {
    if (number <= 1) return false
    for (i in 2..sqrt(number.toDouble()).toInt()) {
        if (number % i == 0) return false
    }
    return true
}

fun main() {
    print("Введіть число: ")
    val input = readlnOrNull()

    val number = input?.toIntOrNull()
    if (number != null) {
        if (isPrime(number)) {
            println("$number — просте число")
        } else {
            println("$number — не є простим числом")
        }
    } else {
        println("Некоректне значення. Введіть ціле число.")
    }
}