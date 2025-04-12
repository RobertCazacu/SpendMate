package com.example.readwhritenotification

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class NotionRepository {
    private val TAG = "NotionRepository"
    private val DATABASE_ID = "13ba266c8e618044b7bdee3b3f7f93df"

    fun addTransaction(name: String, amount: Double, location: String, callback: (Boolean, String?) -> Unit) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())


        val transactionRequest = NotionTransactionRequest(
            parent = Parent(database_id = DATABASE_ID),
            properties = NotionProperties(
                Name = TitleProperty(title = listOf(TextContent(Text(name)))),
                Amount = NumberProperty(number = amount),
                Date = DateProperty(date = DateContent(start = dateFormat.format(today))),
                Description = TextProperty(rich_text = listOf(TextContent(Text("Tranzacție adăugată din SpendMate")))),
                Location = TextProperty(rich_text = listOf(TextContent(Text(location))))
            )
        )
        RetrofitClient.instance.createTransaction(transactionRequest)
            .enqueue(object : Callback<NotionResponse> {
                override fun onResponse(call: Call<NotionResponse>, response: Response<NotionResponse>) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "Transaction added successfully: ${response.body()}")
                        callback(true, "Tranzacție adăugată cu succes")
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "Eroare necunoscută"
                        Log.e(TAG, "Error adding transaction: ${response.code()} - $errorBody")
                        callback(false, "Eroare: ${response.code()} - $errorBody")
                    }
                }

                override fun onFailure(call: Call<NotionResponse>, t: Throwable) {
                    Log.e(TAG, "API call failed", t)
                    callback(false, "Eroare de rețea: ${t.message}")
                }
            })
    }

    fun getRecentTransactions(callback: (List<Transaction>?, String?) -> Unit) {
        // Aici poți implementa ulterior o metodă pentru a prelua tranzacțiile recente
        // din baza de date Notion folosind endpoint-ul de query
        callback(emptyList(), null)
    }
}
