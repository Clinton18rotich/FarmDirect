package com.farmdirect.app.util

data class CropSeason(
    val cropName: String,
    val emoji: String,
    val plantingMonths: List<String>,
    val growingPeriod: String,
    val harvestingMonths: List<String>,
    val bestRegions: List<String>,
    val waterNeeds: String,
    val tips: List<String>
)

object SeasonalCalendar {
    
    val cropSeasons = listOf(
        CropSeason(
            cropName = "Maize",
            emoji = "🌽",
            plantingMonths = listOf("February", "March", "April"),
            growingPeriod = "3-4 months",
            harvestingMonths = listOf("July", "August", "September"),
            bestRegions = listOf("Trans Nzoia", "Uasin Gishu", "Nakuru"),
            waterNeeds = "Moderate-High",
            tips = listOf(
                "Plant at onset of long rains",
                "Space rows 75cm apart",
                "Apply DAP fertilizer at planting",
                "Top-dress with CAN at knee height"
            )
        ),
        CropSeason(
            cropName = "Beans",
            emoji = "🫘",
            plantingMonths = listOf("March", "April", "October"),
            growingPeriod = "2-3 months",
            harvestingMonths = listOf("June", "July", "December", "January"),
            bestRegions = listOf("Nakuru", "Bomet", "Kericho", "Kisii"),
            waterNeeds = "Moderate",
            tips = listOf(
                "Plant 2-3 seeds per hole",
                "Space rows 45cm apart",
                "Harvest when pods turn yellow",
                "Dry thoroughly before storage"
            )
        ),
        CropSeason(
            cropName = "Potatoes",
            emoji = "🥔",
            plantingMonths = listOf("March", "April", "August", "September"),
            growingPeriod = "3-4 months",
            harvestingMonths = listOf("July", "August", "December", "January"),
            bestRegions = listOf("Nyandarua", "Nakuru", "Elgeyo Marakwet"),
            waterNeeds = "High",
            tips = listOf(
                "Use certified seed potatoes",
                "Plant in well-drained soil",
                "Earth up when plants are 15cm tall",
                "Harvest when foliage turns yellow"
            )
        ),
        CropSeason(
            cropName = "Rice",
            emoji = "🍚",
            plantingMonths = listOf("April", "May", "August"),
            growingPeriod = "4-5 months",
            harvestingMonths = listOf("September", "October", "January"),
            bestRegions = listOf("Mwea", "Ahero", "Bunyala"),
            waterNeeds = "Very High (flooded)",
            tips = listOf(
                "Prepare paddy fields 2-3 weeks before",
                "Transplant seedlings at 21 days",
                "Maintain 5-10cm water level",
                "Drain field 2 weeks before harvest"
            )
        ),
        CropSeason(
            cropName = "Wheat",
            emoji = "🌾",
            plantingMonths = listOf("April", "May"),
            growingPeriod = "4-5 months",
            harvestingMonths = listOf("September", "October"),
            bestRegions = listOf("Narok", "Nakuru", "Uasin Gishu"),
            waterNeeds = "Moderate",
            tips = listOf(
                "Plant early in the rainy season",
                "Use certified seeds for better yield",
                "Apply nitrogen fertilizer at tillering",
                "Harvest when grain moisture is 13-14%"
            )
        ),
        CropSeason(
            cropName = "Tomatoes",
            emoji = "🍅",
            plantingMonths = listOf("March", "April", "September", "October"),
            growingPeriod = "2-3 months",
            harvestingMonths = listOf("June", "July", "December", "January"),
            bestRegions = listOf("Kiambu", "Kirinyaga", "Nakuru"),
            waterNeeds = "High",
            tips = listOf(
                "Start seeds in nursery beds",
                "Transplant when 4-6 weeks old",
                "Stake plants for support",
                "Harvest when fruits start turning red"
            )
        )
    )
    
    fun getCurrentSeason(): String {
        val month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH)
        val months = listOf("January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December")
        return months[month]
    }
    
    fun getCropsForPlantingNow(): List<CropSeason> {
        val currentMonth = getCurrentSeason()
        return cropSeasons.filter { currentMonth in it.plantingMonths }
    }
    
    fun getCropsForHarvestingNow(): List<CropSeason> {
        val currentMonth = getCurrentSeason()
        return cropSeasons.filter { currentMonth in it.harvestingMonths }
    }
    
    fun getCropCalendar(cropName: String): CropSeason? {
        return cropSeasons.find { it.cropName.equals(cropName, ignoreCase = true) }
    }
}
