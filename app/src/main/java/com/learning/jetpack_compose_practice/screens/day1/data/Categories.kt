package com.learning.jetpack_compose_practice.screens.day1.data

data class Category(
    val id : Int,
    val name : String,
    val emoji : String
)

data class FoodItem(
    val id: Int,
    val name: String,
    val restaurant: String,
    val price: Int,
    val rating: Float,
    val deliveryTime: Int,   // in minutes
    val emoji: String,
    val badge: String? = null  // "Hot", "New", null
)

// ─────────────────────────────────────────
// 2. SAMPLE DATA
// ─────────────────────────────────────────

val categories = listOf(
    Category(1, "All", ""),
    Category(2, "Burger", "🍔"),
    Category(3, "Pizza", "🍕"),
    Category(4, "Sushi", "🍱"),
    Category(5, "Pasta", "🍝"),
    Category(6, "Desserts", "🍰"),
    Category(7, "Drinks", "🥤"),
)

 val popularItems = listOf(
    FoodItem(1, "Smash Burger", "Burger Bros", 299, 4.8f, 25, "🍔", badge = "Hot"),
    FoodItem(2, "Margherita", "Pizza Planet", 349, 4.6f, 30, "🍕"),
    FoodItem(3, "Spicy Tuna Roll", "Tokyo Bites", 449, 4.9f, 40, "🍱", badge = "New"),
    FoodItem(4, "Truffle Pasta", "Pasta House", 399, 4.7f, 35, "🍝"),
)

 val allItems = listOf(
    FoodItem(5, "Chicken Wings", "Wing Street", 249, 4.5f, 20, "🍗"),
    FoodItem(6, "Caesar Salad", "Green Bites", 199, 4.3f, 15, "🥗"),
    FoodItem(7, "Veg Biryani", "Spice Garden", 229, 4.6f, 30, "🍛"),
    FoodItem(8, "Chocolate Lava Cake", "Sweet Dreams", 149, 4.9f, 10, "🍫"),
)