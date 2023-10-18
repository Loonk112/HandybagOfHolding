package com.handibagofholding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser != null)
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

        val et_email = findViewById<EditText>(R.id.et_email)
        val et_password = findViewById<EditText>(R.id.et_password)
        val b_login = findViewById<Button>(R.id.b_loginConfirm)
        val tv_register = findViewById<TextView>(R.id.tv_register)

        tv_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        b_login.setOnClickListener {
            when{
                TextUtils.isEmpty(et_email.text.toString().trim {it <= ' '}) ->
                {
                    Toast.makeText(this@LoginActivity, "Email can not be empty.", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(et_password.text.toString().trim {it <= ' '}) ->
                {
                    Toast.makeText(this@LoginActivity, "Password can not be empty.", Toast.LENGTH_SHORT).show()
                }

                else ->
                {
                    val email: String = et_email.text.toString().trim { it <= ' '}
                    val password: String = et_password.text.toString().trim { it <= ' '}

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful)
                        {
                            Toast.makeText(
                                this@LoginActivity,
                                "You were logged in successfully.",
                                Toast.LENGTH_SHORT
                            ).show()

                            //Moving to login - clearing past activities

                            ViewModel.account = FirebaseAuth.getInstance().currentUser?.uid.toString()

                            val intent = Intent(this@LoginActivity, AccountActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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