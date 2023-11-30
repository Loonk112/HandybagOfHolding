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
import com.handybagofholding.R
import com.handybagofholding.ViewModel
import com.handybagofholding.activity.LoginActivity

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
                val intent = Intent(it, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                it.finish()
            }
        }

        return view
    }

}