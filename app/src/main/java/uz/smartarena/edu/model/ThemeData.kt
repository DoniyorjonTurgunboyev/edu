package uz.smartarena.edu.model

import java.io.Serializable

data class ThemeData(
    var info: String = "",
    var theme: String = "",
    var url: String,
    var acceptance:Double = 0.0
):Serializable
