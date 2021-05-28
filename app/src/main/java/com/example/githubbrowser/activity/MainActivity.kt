package com.example.githubbrowser.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.githubbrowser.R
import com.example.githubbrowser.backend.RetrofitClient
import com.example.githubbrowser.model.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), TextView.OnEditorActionListener {

    // Defining the variables
    private var editTextUsername:EditText? = null
    private var progressBar: ProgressBar? = null
    private var imm:InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialiizing the views
        editTextUsername = findViewById(R.id.editTextUsername)
        progressBar = findViewById(R.id.progressBar)

        // Initialize the imm
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Setting editText listener
        editTextUsername?.setOnEditorActionListener(this)
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        return if(v == editTextUsername){

            // Get the username from the edit text
            val username: String = editTextUsername?.text?.trim().toString()


            // Check the edittext is not empty
            if (username.isEmpty() || username.isBlank()){
            // Show error message to the user
            editTextUsername?.error = getString(R.string.username_cannot_be_empty)

            }else{
                // Hide the keyboard
                imm?.hideSoftInputFromWindow(editTextUsername?.windowToken, 0)

                // Start the progress bar
                progressBar?.visibility = View.VISIBLE

                // Make the request
                getRepositoriesForUsername(username)
            }



            true
        } else {
            false
        }
    }
    private fun getRepositoriesForUsername(username: String){
        RetrofitClient
                .instance
                .getRepositoriesForUser(username)
                .enqueue(object: Callback<List<Repository>>{
                    override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
                        // Log the error
                        Log.e(TAG, "Error getting repos: ${t.localizedMessage}")

                        // Show an error message to the user
                        Toast.makeText(this@MainActivity, R.string.unable_to_get_repo, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                            call: Call<List<Repository>>,
                            response: Response<List<Repository>>
                    ) {

                        //Hide the progressbar
                        progressBar?.visibility = View.VISIBLE

                        if(response.isSuccessful){

                            // Getting the list of repositories
                            val listOfRepos = response.body() as? ArrayList<Repository>

                            // Passing data to the next activity
                            listOfRepos?.let {
                                // Create an intent
                                val intent = Intent(this@MainActivity, RepsitoryActivity :: class.java)

                                // Pass the list of activity
                                intent.putParcelableArrayListExtra(RepsitoryActivity.KEY_REPOSITORY_DATA, it)

                                // Start the new activity
                                startActivity(intent)
                            }

                        }else{

                            // Created a message based on the error code
                            val message = when(response.code()){
                                500 -> R.string.internal_server_error
                                401 -> R.string.unauthorized
                                403 -> R.string.forbidden
                                404 -> R.string.user_not_found
                                else -> R.string.try_another_user
                            }

                            // Show message to the user
                            Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                            Log.e(TAG, getString(message))

                        }
                    }
                })
    }

    companion object{
        private val TAG = MainActivity :: class.java.simpleName
    }
}
