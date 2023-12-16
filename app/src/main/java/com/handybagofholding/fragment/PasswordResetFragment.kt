package com.handybagofholding.fragment

import android.os.Bundle
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


class PasswordResetFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_password_reset, container, false)

        val etEmail: EditText = view.findViewById(R.id.et_email)
        val tvReturn: TextView = view.findViewById(R.id.tv_returnLogin)
        val bSend: Button = view.findViewById(R.id.b_sendRequest)

        bSend.setOnClickListener {
            if (etEmail.text.isNullOrBlank()) {
                Toast.makeText(context, "E-mail must be filled to reset password.", Toast.LENGTH_SHORT).show()
            }
            else {
                ViewModel.auth.sendPasswordResetEmail(etEmail.text.toString().trim()).addOnSuccessListener {
                    Toast.makeText(context, "E-mail successfully sent.", Toast.LENGTH_SHORT).show()
                    view.findNavController().navigateUp()
                }.addOnFailureListener {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        tvReturn.setOnClickListener {
            view.findNavController().navigateUp()
        }

        return view
    }

}