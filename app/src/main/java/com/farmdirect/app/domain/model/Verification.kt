package com.farmdirect.app.domain.model

data class Verification(
    val id: String = "", val userId: String = "", val level: String = "UNVERIFIED",
    val idDocument: IdDocument = IdDocument(), val selfieVerified: Boolean = false
)

data class IdDocument(val type: String = "NATIONAL_ID", val documentNumber: String = "", val fullName: String = "", val verified: Boolean = false)

data class VerificationRequest(val documentType: String, val documentNumber: String, val fullName: String, val documentImage: String)
