package train

// Замінити всі від’ємні числа у масиві на 0.

fun replaceNegativesWithZero(arr: IntArray): IntArray {
    return arr.map { if (it < 0) 0 else it }.toIntArray()
}

fun main() {
    print("Введіть числа через пробіл: ")
    val input = readLine()

    if (input != null) {
        val numbers = input.split(" ")
            .mapNotNull { it.toIntOrNull() }
            .toIntArray()

        val replaced = replaceNegativesWithZero(numbers)

        println("Результат: ${replaced.joinToString(" ")}")
    } else {
        println("Некоректне введення.")
    }
}