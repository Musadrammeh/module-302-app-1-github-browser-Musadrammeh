package com.example.githubbrowser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githubbrowser.R
import com.example.githubbrowser.model.Repository

class RepsitoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repsitory)

        // Receiving the list of repositories
        val listOfRepos = intent?.getParcelableArrayListExtra<Repository>(KEY_REPOSITORY_DATA)

        // Ensuring the list of repos is not null before showing the items
        listOfRepos?.let {

        }

    }

    companion object{
        const val  KEY_REPOSITORY_DATA = "keyRepositoryData"
    }
}