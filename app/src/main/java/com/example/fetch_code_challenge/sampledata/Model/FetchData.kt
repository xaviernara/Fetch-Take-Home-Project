package com.example.fetch_code_challenge.sampledata.Model

class FetchData : ArrayList<FetchDataItem>()

data class FetchDataItem(
    val id: Int? = null,
    val listId: Int? = null,
    val name: String? = null
)