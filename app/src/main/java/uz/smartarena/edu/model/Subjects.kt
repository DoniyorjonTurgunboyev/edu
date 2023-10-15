package uz.smartarena.edu.model

import java.io.Serializable

data class Subjects(
    val image: Int,
    val name: String,
    var countOfThemes: Int,
    var doneThemes: Int,
    var acceptance: Double
):Serializable
