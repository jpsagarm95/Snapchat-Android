package com.myappcompany.sjp.snapchat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    var emailEditText: EditText? = null
    var passwordEditText: EditText? = null
    val mAuth = FirebaseAuth.getInstance()

    fun goClicked(view: View) {
        var email = emailEditText?.text.toString()
        var password = passwordEditText?.text.toString()
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        logIn()
                    } else {
                        // If sign in fails, display a message to the user.
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.i("Info", "Going to add to the db")
                                        FirebaseDatabase.getInstance().getReference().child("users").child(task.result.user.uid).child("email").setValue(emailEditText?.text.toString())
                                        Log.i("Info", "Just complete " + emailEditText?.text.toString())
                                        logIn()
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(this, "LogIn failed!!! Try again", Toast.LENGTH_SHORT).show()
                                    }
                                }
                    }
                }
    }

    fun logIn() {
        val intent = Intent(this, SnapsActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        if (mAuth.currentUser != null) {
            logIn()
        }
    }
}
