package com.example.githubbrowser.backend

import com.example.githubbrowser.model.Repository
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Endpoint {

    @GET("users/{user}/repos")
    fun getRepositoriesForUser(@Path("user")username: String) : Call<List<Repository>>
}