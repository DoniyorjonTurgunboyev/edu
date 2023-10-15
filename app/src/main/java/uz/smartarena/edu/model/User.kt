package uz.smartarena.edu.model

data class User(
    var name: String? = "",
    var surname: String? = "",
    var birthday: String? = "",
    var number: String? = "",
    var address: String? = "",
    var currentLesson: Int = 3,
    var acceptance: DoubleArray = doubleArrayOf(4.2,3.0,.0,.0,.0)
) : java.io.Serializable