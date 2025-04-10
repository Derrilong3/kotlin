package train

// 5. Перевірити, чи рік високосний.
fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

fun main() {
    print("Введіть рік: ")
    val input = readlnOrNull()

    val year = input?.toIntOrNull()
    if (year != null) {
        if (isLeapYear(year)) {
            println("$year — високосний рік")
        } else {
            println("$year — не високосний рік")
        }
    } else {
        println("Некоректне значення. Введіть ціле число.")
    }
}