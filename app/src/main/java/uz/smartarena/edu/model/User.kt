package uz.smartarena.edu.model

data class User(
    var name: String? = "",
    var surname: String? = "",
    var birthday: String? = "",
    var number: String? = "",
    var address: String? = "",
) : java.io.Serializable