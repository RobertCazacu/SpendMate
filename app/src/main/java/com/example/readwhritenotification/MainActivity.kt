package com.example.readwhritenotification

import TransactionAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors




class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter
    private lateinit var historyButton: Button
    private lateinit var addExpenseButton: Button
    private var transactionList: MutableList<Transaction> = mutableListOf()
    private val executor = Executors.newFixedThreadPool(4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        historyButton = findViewById(R.id.btnHistory)
        addExpenseButton = findViewById(R.id.btnAddExpense)

        historyButton.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        addExpenseButton.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        transactionList = getTransactionHistory().toMutableList()
        adapter = TransactionAdapter(transactionList)
        recyclerView.adapter = adapter
    }

    private fun getTransactionHistory(): List<Transaction> {
        // TODO: Preia tranzacțiile din baza de date sau altă sursă
        return listOf()
    }

    private fun showLastFiveDaysTransactions() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -5)
        val fiveDaysAgo = calendar.time

        val lastFiveDaysTransactions = transactionList.filter { it.date.after(fiveDaysAgo) }
        adapter.updateList(lastFiveDaysTransactions)
        sendTransactionsToNotion(lastFiveDaysTransactions)
    }

    private fun sendTransactionsToNotion(transactions: List<Transaction>) {
        transactions.forEach { transaction ->
            executor.execute {
                try {
                    val url = URL("https://api.notion.com/v1/pages")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setRequestProperty(
                        "Authorization",
                        "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"
                    )
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("Notion-Version", "2022-02-22")
                    connection.doOutput = true

                    val json = JSONObject().apply {
                        put(
                            "parent",
                            JSONObject().put("database_id", "13ba266c8e618044b7bdee3b3f7f93df")
                        )
                        put("properties", JSONObject().apply {
                            put(
                                "Name",
                                JSONObject().put(
                                    "title",
                                    JSONArray().put(
                                        JSONObject().put(
                                            "text",
                                            JSONObject().put("content", transaction.name)
                                        )
                                    )
                                )
                            )
                            put("Amount", JSONObject().put("number", transaction.amount))
                            put(
                                "Date",
                                JSONObject().put(
                                    "date",
                                    JSONObject().put(
                                        "start",
                                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                            transaction.date
                                        )
                                    )
                                )
                            )
                        })
                    }

                    connection.outputStream.use { os: OutputStream ->
                        os.write(json.toString().toByteArray())
                        os.flush()
                    }

                    val responseCode = connection.responseCode
                    val responseMessage =
                        connection.inputStream.bufferedReader().use(BufferedReader::readText)

                    if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                        println("✅ Transaction sent successfully!")
                        println("🔹 Notion Response: $responseMessage")
                    } else {
                        println("❌ Error sending transaction: $responseCode")
                        println("🔹 Notion Response: $responseMessage")
                    }
                    connection.disconnect()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                fun refreshTransactions() {
                    val notionRepository = NotionRepository()
                    notionRepository.getRecentTransactions { transactions, error ->
                        runOnUiThread {
                            if (transactions != null) {
                                transactionList.clear()
                                transactionList.addAll(transactions)
                                adapter.notifyDataSetChanged()
                            } else {
                                // Gestionează eroarea
                            }
                        }
                    }
                }
            }
        }
    }
}
