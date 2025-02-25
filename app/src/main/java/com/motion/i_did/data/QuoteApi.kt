package com.motion.i_did.data

import retrofit2.http.GET
//Interface for the quote API
interface QuoteApi {
    //fetching random quotes from zenquotes
    @GET("api/random")
   suspend fun getRandomQuote(): List<Quote>
}
