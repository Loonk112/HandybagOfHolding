package com.handibagofholding

import android.content.Context
import android.app.AlertDialog

class AlertDialog(context: Context) : AlertDialog.Builder(context) {

    lateinit var onResponse: (r : ResponseType) -> Unit

    enum class ResponseType {
        YES, NO
    }

    fun show(title: String, message: String, listener: (r : ResponseType) -> Unit) {

        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        onResponse = listener

        builder.setPositiveButton("Yes") { _, _ ->
            onResponse(ResponseType.YES)
        }

        builder.setNegativeButton("No") { _, _ ->
            onResponse(ResponseType.NO)
        }

        val alertDialog: AlertDialog = builder.create()

        alertDialog.setCancelable(true)
        alertDialog.show()
    }
}