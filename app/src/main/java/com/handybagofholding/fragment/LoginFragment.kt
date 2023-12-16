package com.handybagofholding.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.handybagofholding.R
import com.handybagofholding.ViewModel
import com.handybagofholding.activity.AccountActivity

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val etEmail = view.findViewById<EditText>(R.id.et_email)
        val etPassword = view.findViewById<EditText>(R.id.et_password)
        val bLogin = view.findViewById<Button>(R.id.b_loginConfirm)
        val tvRegister = view.findViewById<TextView>(R.id.tv_register)
        val tvResetPassword = view.findViewById<TextView>(R.id.tv_reset_password)


        tvRegister.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
        tvResetPassword.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginFragment_to_passwordResetFragment)
        }

        bLogin.setOnClickListener {
            when{
                TextUtils.isEmpty(etEmail.text.toString().trim {it <= ' '}) ->
                {
                    Toast.makeText(context, "Email can not be empty.", Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(etPassword.text.toString().trim {it <= ' '}) ->
                {
                    Toast.makeText(context, "Password can not be empty.", Toast.LENGTH_SHORT).show()
                }

                else ->
                {
                    val email: String = etEmail.text.toString().trim { it <= ' '}
                    val password: String = etPassword.text.toString().trim { it <= ' '}

                    ViewModel.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful)
                        {
                            Toast.makeText(
                                context,
                                "You were logged in successfully.",
                                Toast.LENGTH_SHORT
                            ).show()

                            ViewModel.account = ViewModel.auth.currentUser?.uid.toString()

                            val intent = Intent(requireActivity(), AccountActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            requireActivity().finish()
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