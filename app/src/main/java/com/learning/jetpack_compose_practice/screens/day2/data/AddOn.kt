package com.learning.jetpack_compose_practice.screens.day2.data

data class AddOn(
    val id: Int,
    val name: String,
    val price: Int
)

data class ProductDetail(
    val id: Int,
    val name: String,
    val restaurant: String,
    val price: Int,
    val rating: Float,
    val reviewCount: Int,
    val deliveryTime: Int,
    val emoji: String,
    val description: String,
    val badge: String? = null,
    val sizes: List<String>,
    val addOns: List<AddOn>
)

val sampleProduct = ProductDetail(
    id = 1,
    name = "Double Smash Burger",
    restaurant = "Burger Bros · Koramangala",
    price = 299,
    rating = 4.8f,
    reviewCount = 240,
    deliveryTime = 25,
    emoji = "🍔",
    description = "Two smashed veg patties with American cheese, caramelised onions, pickles and our secret house sauce — stacked in a toasted brioche bun.",
    badge = "Bestseller",
    sizes = listOf("Regular", "Large", "XL"),
    addOns = listOf(
        AddOn(1, "Extra Cheese", 40),
        AddOn(2, "Bacon Strip", 60),
        AddOn(3, "Extra Sauce", 20),
    )
)
