package com.farmdirect.app.util

object USSDSimulator {
    
    val mainMenu = """
        🌾 FARMDIRECT *384*800#
        
        1. Check Prices
        2. Buy Produce
        3. Sell Produce
        4. Weather Update
        5. Track Delivery
        6. My Orders
        7. Help
        #. Exit
    """.trimIndent()
    
    val priceMenu = """
        📊 PRICES (per 90kg bag)
        
        1. Maize - KES 3,500
        2. Beans - KES 8,000
        3. Potatoes - KES 2,500
        4. Rice - KES 6,500
        5. Wheat - KES 4,200
        
        0. Back
    """.trimIndent()
    
    val buyMenu = """
        🛒 BUY PRODUCE
        
        1. Find by crop
        2. Find by location
        3. Find by price
        
        Enter crop name:
    """.trimIndent()
    
    val sellMenu = """
        💰 SELL PRODUCE
        
        Format: [Quantity] [Crop] [Price]
        Example: 500 MAIZE 3500
        
        Enter your listing:
    """.trimIndent()
    
    fun processUSSDInput(input: String, currentMenu: String): String {
        val trimmed = input.trim()
        
        return when (currentMenu) {
            "main" -> when (trimmed) {
                "1" -> priceMenu
                "2" -> buyMenu
                "3" -> sellMenu
                "4" -> "🌤️ Weather for Kitale:\n☀️ 24°C, Sunny\n💧 Humidity: 65%"
                "5" -> "📦 Enter your delivery code:"
                "6" -> "📋 Recent orders:\n1. #1234 - Maize - Delivered\n2. #1235 - Beans - In transit"
                "7" -> helpText()
                else -> "Invalid choice. Press 1-7 or # to exit."
            }
            else -> mainMenu
        }
    }
    
    private fun helpText(): String {
        return """
            📞 FARMDIRECT HELP
            
            Commands:
            FD PRICE [crop] - Check prices
            FD BUY [crop] - Find sellers
            FD SELL [qty] [crop] [price] - Post
            FD WEATHER - Get forecast
            FD ORDERS - Check orders
            
            Support: +254 800 123 456
            📱 Download the app for more!
        """.trimIndent()
    }
}
