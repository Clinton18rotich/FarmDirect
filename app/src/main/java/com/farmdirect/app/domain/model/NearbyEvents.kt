package com.farmdirect.app.domain.model

data class FarmEvent(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val type: EventType = EventType.MARKET,
    val location: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val distanceKm: Double = 0.0,
    val startDate: Long = System.currentTimeMillis(),
    val endDate: Long = System.currentTimeMillis() + 86400000,
    val organizer: String = "",
    val contactPhone: String = "",
    val price: String = "Free",
    val attendees: Int = 0,
    val maxAttendees: Int = 0,
    val imageUrl: String = ""
)

enum class EventType {
    MARKET, TRAINING, EXHIBITION, FIELD_DAY, AUCTION, MEETING, WORKSHOP, CONFERENCE
}

val sampleEvents = listOf(
    FarmEvent("1", "Kitale Farmers Market", "Weekly fresh produce market", EventType.MARKET, "Kitale Showground", 1.0167, 35.0151, 2.3, attendees = 150),
    FarmEvent("2", "Organic Farming Workshop", "Learn organic certification process", EventType.WORKSHOP, "Eldoret Agricultural Center", 0.5204, 35.2699, 45.0, price = "KES 500", attendees = 45, maxAttendees = 100),
    FarmEvent("3", "Dairy Farming Field Day", "Visit model dairy farms", EventType.FIELD_DAY, "Bungoma Dairy Cooperative", 0.5695, 34.5584, 65.0, price = "Free", attendees = 200),
    FarmEvent("4", "Agri-Tech Exhibition 2026", "Latest farming technology showcase", EventType.EXHIBITION, "Kenyatta International Convention Centre, Nairobi", -1.2921, 36.8219, 320.0, price = "KES 200", attendees = 1200),
    FarmEvent("5", "Maize Value Chain Conference", "Connecting maize farmers to markets", EventType.CONFERENCE, "Nakuru Agricultural Society", -0.3031, 36.0800, 180.0, price = "KES 1,000", attendees = 300, maxAttendees = 500)
)
