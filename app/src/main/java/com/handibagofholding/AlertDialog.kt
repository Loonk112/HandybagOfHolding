package com.handibagofholding

import android.content.Context
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.TextView

class AlertDialog(context: Context) : AlertDialog.Builder(context) {

    lateinit var onResponse: (r : ResponseType) -> Unit

    enum class ResponseType {
        YES, NO
    }

    fun show(message: String, listener: (r : ResponseType) -> Unit) {

        val builder = AlertDialog.Builder(context)

        val view = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null)

        var alertDialog: AlertDialog = builder.create()

        view.findViewById<TextView>(R.id.tv_message).text = message

        view.findViewById<TextView>(R.id.tv_confirm).setOnClickListener() {
            onResponse(ResponseType.YES)
            alertDialog.dismiss()
        }

        view.findViewById<TextView>(R.id.tv_cancel).setOnClickListener() {
            onResponse(ResponseType.NO)
            alertDialog.dismiss()
        }



        builder.setView(view)

        onResponse = listener

        alertDialog = builder.create()

        alertDialog.setCancelable(true)
        alertDialog.show()
    }
}