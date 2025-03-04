import java.sql.Date

data class Transaction(
    val id: Int,
    val name: String,
    val amount: Double,
    val date: Date,
    val description: String
)