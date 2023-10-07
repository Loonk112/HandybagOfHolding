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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        val et_email = findViewById<EditText>(R.id.et_email)
        val et_username = findViewById<EditText>(R.id.et_username)
        val et_password1 = findViewById<EditText>(R.id.et_password1)
        val et_password2 = findViewById<EditText>(R.id.et_password2)
        val tv_login = findViewById<TextView>(R.id.tv_login)
        val b_registerConfirm = findViewById<Button>(R.id.b_registerConfirm)

        tv_login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        b_registerConfirm.setOnClickListener {
           when {
               TextUtils.isEmpty(et_email.text.toString().trim {it <= ' '}) ->
               {
                   Toast.makeText(this@RegisterActivity, "Email can not be empty.", Toast.LENGTH_SHORT).show()
               }

               !et_email.text.toString().trim {it <= ' '}.contains('@') ->
               {
                   Toast.makeText(this@RegisterActivity, "Email must be valid.", Toast.LENGTH_SHORT).show()
               }

               TextUtils.isEmpty(et_username.text.toString().trim {it <= ' '}) ->
               {
                   Toast.makeText(this@RegisterActivity, "Username can not be empty.", Toast.LENGTH_SHORT).show()
               }

               TextUtils.isEmpty(et_password1.text.toString().trim {it <= ' '}) ->
               {
                   Toast.makeText(this@RegisterActivity, "Password can not be empty.", Toast.LENGTH_SHORT).show()
               }

               et_password1.text.toString().trim {it <= ' '}.length < 6 ->
               {
                   Toast.makeText(this@RegisterActivity, "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show()
               }

               !et_password1.text.toString().trim {it <= ' '}.contains("[0-9]".toRegex()) ->
               {
                   Toast.makeText(this@RegisterActivity, "Password must have at least 1 number.", Toast.LENGTH_SHORT).show()
               }

               !et_password1.text.toString().trim {it <= ' '}.lowercase().contains("[a-z]".toRegex()) ->
               {
                   Toast.makeText(this@RegisterActivity, "Password must have at least 1 letter.", Toast.LENGTH_SHORT).show()
               }

               et_password1.text.toString().trim {it <= ' '} !=  et_password2.text.toString().trim { it <= ' '}->
               {
                   Toast.makeText(this@RegisterActivity, "Passwords must match.", Toast.LENGTH_SHORT).show()
               }

               else ->
               {
                   val email: String = et_email.text.toString().trim { it <= ' '}
                   val password: String = et_password1.text.toString().trim { it <= ' '}

                   FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                       .addOnCompleteListener { task ->
                           //Success check
                           if (task.isSuccessful) {
                               Toast.makeText(
                                   this@RegisterActivity,
                                   "You were registered successfully.",
                                   Toast.LENGTH_SHORT
                               ).show()

                               val username = et_username.text.toString().trim { it <= ' '}

                               val user = hashMapOf(
                                   "username" to "$username"
                               )

                               Log.d("FirebaseCode", "Creating document")

                               val db = Firebase.firestore

                               db.collection("users").document("${FirebaseAuth.getInstance().currentUser?.uid}")
                                   .set(user)
                                   .addOnSuccessListener {
                                       Log.d("FirebaseCode", "DocumentSnapshot successfully written!")
                                   }
                                   .addOnFailureListener { e ->
                                       Log.w("FirebaseCode", "Error writing document", e)
                                   }.addOnCompleteListener {
                                       FirebaseAuth.getInstance().signOut()
                                       //Moving to login - clearing past activities
                                       val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                       intent.flags =
                                           Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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