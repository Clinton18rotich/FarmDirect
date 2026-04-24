package com.farmdirect.app.domain.model

data class FinanceSummary(
    val savingsBalance: Double = 0.0,
    val interestEarned: Double = 0.0,
    val creditScore: Int = 0,
    val loanEligibility: Double = 0.0,
    val activeLoans: List<FarmLoan> = emptyList()
)

data class FarmLoan(
    val id: String = "",
    val type: String = "",
    val amount: Double = 0.0,
    val interestRate: Double = 5.0,
    val repaidAmount: Double = 0.0,
    val nextPaymentAmount: Double = 0.0,
    val status: String = "PENDING"
)

data class LoanApplicationRequest(val loanType: String, val amount: Double, val purpose: String, val durationMonths: Int)
