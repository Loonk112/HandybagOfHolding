package com.handybagofholding.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.handybagofholding.R
import com.handybagofholding.ViewModel

class EntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)
    }

    override fun onStart() {
        super.onStart()

        //Clearing VM data
        ViewModel.clearAccountData()

        if (ViewModel.auth.currentUser != null)
        {
            Toast.makeText(this@EntryActivity, "Welcome back!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@EntryActivity, AccountActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}