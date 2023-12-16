package com.handybagofholding.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.handybagofholding.R
import com.handybagofholding.ViewModel


class RegistrationFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_registration, container, false)

        val etEmail = view.findViewById<EditText>(R.id.et_email)
        val etUsername = view.findViewById<EditText>(R.id.et_username)
        val etPassword1 = view.findViewById<EditText>(R.id.et_password1)
        val etPassword2 = view.findViewById<EditText>(R.id.et_password2)
        val tvLogin = view.findViewById<TextView>(R.id.tv_login)
        val bRegisterConfirm = view.findViewById<Button>(R.id.b_registerConfirm)

        tvLogin.setOnClickListener {
            view.findNavController().navigateUp()
        }

        bRegisterConfirm.setOnClickListener {
            when {
                TextUtils.isEmpty(etEmail.text.toString().trim {it <= ' '}) ->
                {
                    Toast.makeText(context, "Email can not be empty.", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(etUsername.text.toString().trim {it <= ' '}) ->
                {
                    Toast.makeText(context, "Username can not be empty.", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(etPassword1.text.toString().trim {it <= ' '}) ->
                {
                    Toast.makeText(context, "Password can not be empty.", Toast.LENGTH_SHORT).show()
                }

                etPassword1.text.toString().trim {it <= ' '}.length < 6 ->
                {
                    Toast.makeText(context, "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show()
                }

                !etPassword1.text.toString().trim {it <= ' '}.contains("[0-9]".toRegex()) ->
                {
                    Toast.makeText(context, "Password must have at least 1 number.", Toast.LENGTH_SHORT).show()
                }

                !etPassword1.text.toString().trim {it <= ' '}.lowercase().contains("[a-z]".toRegex()) ->
                {
                    Toast.makeText(context, "Password must have at least 1 letter.", Toast.LENGTH_SHORT).show()
                }

                etPassword1.text.toString().trim {it <= ' '} !=  etPassword2.text.toString().trim { it <= ' '}->
                {
                    Toast.makeText(context, "Passwords must match.", Toast.LENGTH_SHORT).show()
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
                                    context,
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
                                        view.findNavController().navigateUp()
                                    }
                            } else {
                                Toast.makeText(
                                    context,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                }
            }


        }

        return view
    }


}