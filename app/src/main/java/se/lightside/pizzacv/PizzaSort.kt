package se.lightside.pizzacv

private val catOrder = mapOf<String, Int>("Pizza" to 1,
        "Dryck" to 2,
        "Tillbehör" to 3)

fun comparePizzaCategory(pizzaCat: String) : Int = catOrder[pizzaCat] ?: Int.MAX_VALUE