package train

// Написати функцію, яка приймає ціле число, кидає виняток, якщо воно не з діапазону 1–100.

fun checkInRange(number: Int) {
    if (number !in 1..100) {
        throw IllegalArgumentException("Число $number не входить у діапазон 1–100.")
    }
    println("Число $number в межах діапазону.")
}
fun main() {
    print("Введіть ціле число від 1 до 100: ")
    val input = readlnOrNull()
    val number = input?.toIntOrNull()

    try {
        if (number != null) {
            checkInRange(number)
        } else {
            println("Некоректне введення.")
        }
    } catch (e: IllegalArgumentException) {
        println("Помилка: ${e.message}")
    }
}