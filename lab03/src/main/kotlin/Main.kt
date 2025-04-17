import kotlinx.coroutines.*

suspend fun fetchGradesFromServer(): List<Int> {
    delay(2000)
    return listOf(85, 90, 78, 92, 88)
}

fun main() = runBlocking {
    println("\nFetching grades from server...")
    val deferredGrades = async { fetchGradesFromServer() }

    // Створення студентів
    val student1 = Student("John")
    val student2 = Student(
        _name = "Mary",
        _age = 20,
        _grades = listOf(80, 85, 90)
    )

    println("\nInitial students:")
    println(student1)
    println(student2)

    // Додавання оцінок
    student1.updateGrades(listOf(70, 75, 80))
    println("\nAfter updating ${student1.name}'s grades:")
    println(student1)

    // Використання операторів
    val combinedStudent = student1 + student2
    println("\nCombined grades (+ operator):")
    println(combinedStudent)

    val multipliedStudent = student2 * 2
    println("Multiplied grades (* operator):")
    println(multipliedStudent)

    // Перевірка ==
    println("\nAre students equal? ${student1 == student2}")

    // Lazy властивість
    println("${student1.name}'s status: ${student1.status}")
    println("${student2.name}'s status: ${student2.status}")

    // Група студентів
    val group = Group(student1, student2)
    println("\nGroup top student: ${group.getTopStudent()}")

    // Асинхронне оновлення
    val newGrades = deferredGrades.await()
    student1.updateGrades(newGrades)
    println("Updated ${student1.name}'s grades from server: $student1")
}