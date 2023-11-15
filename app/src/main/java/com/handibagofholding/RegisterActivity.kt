package com.handibagofholding

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        val etEmail = findViewById<EditText>(R.id.et_email)
        val etUsername = findViewById<EditText>(R.id.et_username)
        val etPassword1 = findViewById<EditText>(R.id.et_password1)
        val etPassword2 = findViewById<EditText>(R.id.et_password2)
        val tvLogin = findViewById<TextView>(R.id.tv_login)
        val bRegisterConfirm = findViewById<Button>(R.id.b_registerConfirm)

        tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        bRegisterConfirm.setOnClickListener {
           when {
               TextUtils.isEmpty(etEmail.text.toString().trim {it <= ' '}) ->
               {
                   Toast.makeText(this@RegisterActivity, "Email can not be empty.", Toast.LENGTH_SHORT).show()
               }

               TextUtils.isEmpty(etUsername.text.toString().trim {it <= ' '}) ->
               {
                   Toast.makeText(this@RegisterActivity, "Username can not be empty.", Toast.LENGTH_SHORT).show()
               }

               TextUtils.isEmpty(etPassword1.text.toString().trim {it <= ' '}) ->
               {
                   Toast.makeText(this@RegisterActivity, "Password can not be empty.", Toast.LENGTH_SHORT).show()
               }

               etPassword1.text.toString().trim {it <= ' '}.length < 6 ->
               {
                   Toast.makeText(this@RegisterActivity, "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show()
               }

               !etPassword1.text.toString().trim {it <= ' '}.contains("[0-9]".toRegex()) ->
               {
                   Toast.makeText(this@RegisterActivity, "Password must have at least 1 number.", Toast.LENGTH_SHORT).show()
               }

               !etPassword1.text.toString().trim {it <= ' '}.lowercase().contains("[a-z]".toRegex()) ->
               {
                   Toast.makeText(this@RegisterActivity, "Password must have at least 1 letter.", Toast.LENGTH_SHORT).show()
               }

               etPassword1.text.toString().trim {it <= ' '} !=  etPassword2.text.toString().trim { it <= ' '}->
               {
                   Toast.makeText(this@RegisterActivity, "Passwords must match.", Toast.LENGTH_SHORT).show()
               }

               else ->
               {
                   val email: String = etEmail.text.toString().trim { it <= ' '}
                   val password: String = etPassword1.text.toString().trim { it <= ' '}

                   ViewModel.auth.createUserWithEmailAndPassword(email, password)
                       .addOnCompleteListener { task ->
                           //Success check
                           if (task.isSuccessful) {
                               Toast.makeText(
                                   this@RegisterActivity,
                                   "You were registered successfully.",
                                   Toast.LENGTH_SHORT
                               ).show()

                               val username = etUsername.text.toString().trim { it <= ' '}

                               val user = hashMapOf(
                                   "id" to "${FirebaseAuth.getInstance().currentUser?.uid}",
                                   "name" to username
                               )

                               Log.d("FirebaseCode", "Creating document")

                               ViewModel.db.collection("users").document("${ViewModel.auth.currentUser?.uid}")
                                   .set(user)
                                   .addOnSuccessListener {
                                       Log.d("FirebaseCode", "DocumentSnapshot successfully written!")
                                   }
                                   .addOnFailureListener { e ->
                                       Log.w("FirebaseCode", "Error writing document", e)
                                   }.addOnCompleteListener {
                                       ViewModel.auth.signOut()
                                       //Moving to login - clearing past activities
                                       val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                       intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                       startActivity(intent)
                                       finish()
                                   }
                           } else {
                               Toast.makeText(
                                   this@RegisterActivity,
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