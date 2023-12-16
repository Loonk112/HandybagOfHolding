package com.handybagofholding.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.handybagofholding.AlertDialog
import com.handybagofholding.R
import com.handybagofholding.ViewModel
import com.handybagofholding.activity.EntryActivity

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        val login = view.findViewById<TextView>(R.id.tv_login)

        view.findViewById<TextView>(R.id.tv_email).text = ViewModel.auth.currentUser?.email.toString()

        ViewModel.db.collection("users").document(ViewModel.account)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("FirestoreResults", "Listen failed.", e)
                    return@addSnapshotListener
                }
                Log.d("FirestoreResults", "${snapshot?.data}")
                if (snapshot != null && snapshot.exists()) {
                    login.text = snapshot.get("name").toString()
                } else {
                    Log.d("FirestoreResults", "Current data: null")
                }
            }


        view.findViewById<Button>(R.id.b_signOut).setOnClickListener {
            ViewModel.auth.signOut()
            activity?.let {
                val intent = Intent(it, EntryActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                it.finish()
            }
        }


        view.findViewById<Button>(R.id.b_password_reset).setOnClickListener {
            AlertDialog(requireContext()).show("Do you want to rest your password?\nOn confirmation, an email will be sent to your inbox."){
                if (it == AlertDialog.ResponseType.YES) {
                    Log.d("AlertDialog", "YES")
                    ViewModel.auth.sendPasswordResetEmail(ViewModel.auth.currentUser?.email.toString()).addOnSuccessListener {
                        Toast.makeText(context, "E-mail successfully sent.", Toast.LENGTH_SHORT).show()
                        view.findNavController().navigateUp()
                    }.addOnFailureListener {ex ->
                        Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        return view
    }

}