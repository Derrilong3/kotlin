import kotlin.math.roundToInt

class Student(
    var _name: String,
    var _age: Int = 0,
    var _grades: List<Int> = listOf()
) {

    init {
        println("Створено об'єкт студента: ${_name.trim().replaceFirstChar { it.uppercase() }}")
    }

    constructor(name: String) : this(name, 0, listOf())

    var name: String
        get() = _name
        set(value) {
            _name = value.trim().replaceFirstChar { it.uppercase() }
        }

    var age: Int
        get() = _age
        private set(value) {
            if (value >= 0) _age = value
        }

    val isAdult: Boolean
        get() = _age >= 18

    val status: String by lazy {
        if (isAdult) "Adult" else "Minor"
    }

    fun getAverage(): Double {
        return if (_grades.isNotEmpty()) _grades.average() else 0.0
    }

    fun processGrades(operation: (Int) -> Int) {
        _grades = _grades.map(operation)
    }

    fun updateGrades(grades: List<Int>) {
        _grades = grades
    }

    operator fun plus(other: Student): Student {
        val combinedGrades = _grades + other._grades
        return Student(_name = "$name & ${other.name}", _age = maxOf(this.age, other.age), _grades = combinedGrades)
    }

    operator fun times(factor: Int): Student {
        val newGrades = _grades.map { it * factor }
        return Student(_name = name, _age = age, _grades = newGrades)
    }

    override operator fun equals(other: Any?): Boolean {
        return other is Student && this.name == other.name && this.getAverage() == other.getAverage()
    }

    override fun toString(): String {
        return "Ім'я: $name, Вік: $age, Оцінки: $_grades, Середній бал: ${"%.2f".format(getAverage())}, Статус: $status"
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
