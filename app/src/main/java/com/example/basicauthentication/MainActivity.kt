package com.example.basicauthentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var signUpButton: Button
    private lateinit var signInButton: Button

    private lateinit var mailEditText: EditText
    private lateinit var passwordEditText: EditText


    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        signUpButton = findViewById(R.id.sign_up_button)
        signInButton = findViewById(R.id.sign_in_button)

        mailEditText = findViewById(R.id.mail_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)

        signInButton.setOnClickListener {
            signIn()
        }

        signUpButton.setOnClickListener {
           signUp()
        }
    }

    private fun signIn() {
        val mail = mailEditText.editableText.toString()
        val password = passwordEditText.editableText.toString()

        if (mail.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(mail, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    verifyEmail()
                } else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        else{
            Toast.makeText(this, "Empty mail or password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signUp() {
        val mail = mailEditText.editableText.toString()
        val password = passwordEditText.editableText.toString()

        if (mail.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    sendVerification()
                } else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        else{
            Toast.makeText(this, "Empty mail or password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyEmail () {
        val user = auth.currentUser
        if (user!!.isEmailVerified){
            Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this , WelcomeActivity::class.java))
        }
        else{
            Toast.makeText(this, "Please verify your email before signing in!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendVerification () {
        val user = auth.currentUser

        user!!.sendEmailVerification().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Signed up... verify email before signing in!", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


}