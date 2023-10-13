package uz.smartarena.edu.model

data class User(
    var name: String? = "",
    var surname: String? = "",
    var number: String? = "",
    var token: String? = "",
    var uid: String? = ""
) : java.io.Serializable