package com.example.ecommerceapp.model

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    var isInWishlist: Boolean = false,
    var isInCart: Boolean = false,
    var quantity: Int = 0
)

object SampleProducts {
    val products = listOf(
        Product(
            1,
            "White TShirt",
            "Premium cotton basic white t-shirt",
            29.99,
            "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab"
        ),
        Product(
            2,
            "Slim Fit Jeans",
            "Dark blue slim fit denim jeans",
            79.99,
            "https://images.unsplash.com/photo-1542272454315-4c01d7abdf4a"
        ),
        Product(
            3,
            "Leather Jacket",
            "Black leather biker jacket",
            199.99,
            "https://images.unsplash.com/photo-1551028719-00167b16eac5"
        ),
        Product(
            4,
            "Casual Sneakers",
            "White casual sneakers",
            89.99,
            "https://images.unsplash.com/photo-1549298916-b41d501d3772"
        ),
        Product(
            5,
            "Summer Dress",
            "Floral print summer dress",
            69.99,
            "https://images.unsplash.com/photo-1572804013309-59a88b7e92f1"
        ),
        Product(
            6,
            "Denim Jacket",
            "Classic blue denim jacket",
            99.99,
            "https://images.unsplash.com/photo-1576871337632-b9aef4c17ab9"
        )
    )
} 