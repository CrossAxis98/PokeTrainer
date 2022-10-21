package com.example.poketrainer.screens.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    fun createNewUserAccount(email: String, password: String, context: Context, home: () -> Unit) {
        val name = email.split("@")[0].replaceFirstChar { it.uppercase() }
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Hi, $name!", Toast.LENGTH_SHORT).show()
                        home.invoke()
                    } else {
                        Log.d("XXX2", "Error ${task.exception}")
                        Toast.makeText(context, "Failed to create an account", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    fun signIn(email: String, password: String, context: Context, home: () -> Unit) {
        val name = email.split("@")[0].replaceFirstChar { it.uppercase() }
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Hi, $name!", Toast.LENGTH_SHORT).show()
                        home.invoke()
                    } else {
                        Log.d("XXX3", "Error ${task.exception}")
                        Toast.makeText(context, "Failed to login", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}