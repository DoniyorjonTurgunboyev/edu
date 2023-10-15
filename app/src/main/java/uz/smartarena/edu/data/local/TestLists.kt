package uz.smartarena.edu.data.local

import uz.smartarena.edu.model.TestData

class TestLists {
    companion object {
        fun getTests(): TestData {
            return TestData(
                "Rasmda ko’rsatilgan to’g’ri tortburchakning perimetrini hisoblash formulasi to’g’ri " +
                        "keltirilgan qatorni toping:",
                arrayListOf("P = 2a+b", "P = a+2b", "P = 2a+2b", "P = a+b"),
                "P = 2a+2b"
            )
        }
    }
}