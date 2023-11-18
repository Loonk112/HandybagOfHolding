package com.handybagofholding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()

        //Clearing VM data
        ViewModel.clearAccountData()

        if (ViewModel.auth.currentUser != null)
        {
            Toast.makeText(this@LoginActivity, "Welcome back!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@LoginActivity, AccountActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val bLogin = findViewById<Button>(R.id.b_loginConfirm)
        val tvRegister = findViewById<TextView>(R.id.tv_register)

        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        bLogin.setOnClickListener {
            when{
                TextUtils.isEmpty(etEmail.text.toString().trim {it <= ' '}) ->
                {
                    Toast.makeText(this@LoginActivity, "Email can not be empty.", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(etPassword.text.toString().trim {it <= ' '}) ->
                {
                    Toast.makeText(this@LoginActivity, "Password can not be empty.", Toast.LENGTH_SHORT).show()
                }

                else ->
                {
                    val email: String = etEmail.text.toString().trim { it <= ' '}
                    val password: String = etPassword.text.toString().trim { it <= ' '}

                    ViewModel.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful)
                        {
                            Toast.makeText(
                                this@LoginActivity,
                                "You were logged in successfully.",
                                Toast.LENGTH_SHORT
                            ).show()

                            ViewModel.account = ViewModel.auth.currentUser?.uid.toString()

                            val intent = Intent(this@LoginActivity, AccountActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

    }
}