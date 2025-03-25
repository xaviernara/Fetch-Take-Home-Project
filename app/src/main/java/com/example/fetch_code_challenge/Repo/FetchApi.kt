package com.example.fetch_code_challenge.Repo

import com.example.fetch_code_challenge.sampledata.Model.FetchData
import retrofit2.Response
import retrofit2.http.GET

interface FetchApi {

    // https://fetch-hiring.s3.amazonaws.com/hiring.json
    @GET("hiring.json")
    suspend fun getFetchData(): Response<FetchData>
}