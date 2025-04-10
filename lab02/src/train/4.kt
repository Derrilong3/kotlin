package train

// Написати функцію, яка повертає довжину рядка або 0, якщо null.

fun getStringLength(str: String?): Int {
    return str?.length ?: 0
}

fun main() {
    print("Введіть рядок: ")
    val input = readlnOrNull()

    val length = getStringLength(input)
    println("Довжина рядка: $length")
}