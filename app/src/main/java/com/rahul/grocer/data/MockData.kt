package com.rahul.grocer.data

import com.rahul.grocer.model.Product
import com.rahul.grocer.model.ProductCategory
import com.rahul.grocer.model.StockStatus
import java.util.UUID
import kotlin.random.Random

object MockData {

    private val fruits = listOf("Apple", "Banana", "Orange", "Strawberry", "Blueberry", "Mango", "Pineapple", "Watermelon", "Grapes", "Kiwi", "Peach", "Pear", "Cherry", "Plum", "Raspberry")
    private val vegetables = listOf("Carrot", "Broccoli", "Spinach", "Potato", "Tomato", "Onion", "Cucumber", "Bell Pepper", "Lettuce", "Zucchini", "Cauliflower", "Kale", "Mushroom", "Asparagus", "Eggplant")
    private val dairy = listOf("Milk", "Cheddar Cheese", "Yogurt", "Butter", "Cream", "Cottage Cheese", "Mozzarella", "Parmesan", "Sour Cream", "Whipped Cream", "Almond Milk", "Soy Milk", "Oat Milk", "Greek Yogurt", "Kefir")
    private val bakery = listOf("Sourdough Bread", "Bagel", "Croissant", "Muffin", "Donut", "Baguette", "Whole Wheat Bread", "Rye Bread", "Ciabatta", "Pita", "Tortilla", "Bun", "Scone", "Danish", "Cake Slice")
    private val snacks = listOf("Potato Chips", "Popcorn", "Pretzels", "Almonds", "Cashews", "Trail Mix", "Granola Bar", "Chocolate Bar", "Cookies", "Crackers", "Beef Jerky", "Rice Cakes", "Peanuts", "Pistachios", "Sunflower Seeds")
    private val beverages = listOf("Orange Juice", "Apple Juice", "Cola", "Lemonade", "Iced Tea", "Coffee", "Green Tea", "Sparkling Water", "Energy Drink", "Smoothie", "Coconut Water", "Root Beer", "Ginger Ale", "Hot Chocolate", "Milkshake")

    // High quality food images from Unsplash (Direct links for reliability)
    private val fruitImages = listOf(
        "https://images.unsplash.com/photo-1610832958506-aa56368176cf?w=500&auto=format&fit=crop&q=60", // Orange
        "https://images.unsplash.com/photo-1518843875459-f738682238a6?w=500&auto=format&fit=crop&q=60", // Veggies
        "https://images.unsplash.com/photo-1563636619-e9143da7973b?w=500&auto=format&fit=crop&q=60", // Apple
        "https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=500&auto=format&fit=crop&q=60", // Banana
        "https://images.unsplash.com/photo-1537578528073-59d863b8626e?w=500&auto=format&fit=crop&q=60"  // Strawberry
    )

    private val dairyImages = listOf(
        "https://images.unsplash.com/photo-1563636619-e9143da7973b?w=500&auto=format&fit=crop&q=60", // Placeholder (Milk)
        "https://images.unsplash.com/photo-1628088062854-d1870b4553da?w=500&auto=format&fit=crop&q=60", // Cheese
        "https://images.unsplash.com/photo-1550583724-b2692b85b150?w=500&auto=format&fit=crop&q=60"  // Yogurt
    )
    
    private val bakeryImages = listOf(
        "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=500&auto=format&fit=crop&q=60", // Bread
        "https://images.unsplash.com/photo-1555507036-ab1f4038808a?w=500&auto=format&fit=crop&q=60"  // Croissant
    )

    fun getProducts(): List<Product> {
        val products = mutableListOf<Product>()
        
        // Generate 100 items
        val allCategories = listOf(
            Pair(ProductCategory.FRUITS, fruits),
            Pair(ProductCategory.VEGETABLES, vegetables),
            Pair(ProductCategory.DAIRY, dairy),
            Pair(ProductCategory.BAKERY, bakery),
            Pair(ProductCategory.SNACKS, snacks),
            Pair(ProductCategory.BEVERAGES, beverages)
        )

        var count = 0
        while (count < 100) {
            val categoryPair = allCategories.random()
            val category = categoryPair.first
            val name = categoryPair.second.random()
            
            // Add some variety to names to avoid duplicates
            val variety = listOf("Organic", "Premium", "Fresh", "Classic", "Family Size", "Small").random()
            val fullName = "$variety $name"
            
            val price = Random.nextDouble(1.99, 25.99)
            val hasDiscount = Random.nextBoolean()
            val discountPercent = if (hasDiscount) Random.nextInt(5, 30) else 0
            val originalPrice = if (hasDiscount) price * (1 + discountPercent / 100.0) else null
            
            val image = when(category) {
                ProductCategory.FRUITS -> fruitImages.random()
                ProductCategory.VEGETABLES -> fruitImages.random() // Reusing for now or add more
                ProductCategory.DAIRY -> dairyImages.random()
                ProductCategory.BAKERY -> bakeryImages.random()
                else -> "https://images.unsplash.com/photo-1542838132-92c53300491e?w=500&auto=format&fit=crop&q=60" // Generic grocery
            }

            products.add(
                Product(
                    id = UUID.randomUUID().toString(),
                    name = fullName,
                    description = "Fresh and delicious $name sourced from the best local farms. Perfect for your daily needs.",
                    price = price,
                    originalPrice = originalPrice,
                    discountPercent = discountPercent,
                    rating = Random.nextDouble(3.5, 5.0),
                    reviewCount = Random.nextInt(10, 500),
                    imageUrl = image,
                    calories = Random.nextInt(50, 500),
                    expiryDate = "Exp: ${Random.nextInt(1, 14)} days",
                    stockStatus = StockStatus.values().random(),
                    category = category
                )
            )
            count++
        }
        return products
    }
}
