class Group(vararg val students: Student) {
    operator fun get(index: Int): Student = students[index]

    fun getTopStudent(): Student? = students.maxByOrNull { it.getAverage() }
}