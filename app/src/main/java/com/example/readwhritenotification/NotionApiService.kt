package com.example.readwhritenotification

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotionApiService {
    @Headers(
        "Authorization: Bearer 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",
        "Content-Type: application/json",
        "Notion-Version: 2022-06-28"  // O versiune mai recentă a API-ului
    )
    @POST("pages")
    fun createTransaction(@Body transaction: NotionTransactionRequest): Call<NotionResponse>
}
