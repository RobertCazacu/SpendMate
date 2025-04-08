package com.example.readwhritenotification

data class NotionTransactionRequest(
    val parent: Parent,
    val properties: NotionProperties
)

data class Parent(val database_id: String)

data class NotionProperties(
    val Name: TitleProperty,
    val Amount: NumberProperty,
    val Date: DateProperty,
    val Description: TextProperty
)

data class TitleProperty(val title: List<TextContent>)
data class NumberProperty(val number: Double)
data class DateProperty(val date: DateContent)
data class TextProperty(val rich_text: List<TextContent>)

data class TextContent(val text: Text)
data class Text(val content: String)
data class DateContent(val start: String)
data class NotionResponse(
    val id: String = "",
    val `object`: String = "",
    val created_time: String = "",
    val last_edited_time: String = ""
)