package com.handibagofholding

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.toObject

class ConsumableInfoTile @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
) : MaterialCardView(context, attributeSet, defaultStyleAttribute) {


    private val ib_increase: ImageButton
    private val ib_decrease: ImageButton
    private val tv_itemCount: TextView

    init {
        inflate(context, R.layout.consumable_info_tile, this)

        ib_increase = findViewById(R.id.ib_increase)
        ib_decrease = findViewById(R.id.ib_decrease)
        tv_itemCount = findViewById(R.id.tv_itemCount)

        ib_decrease.setOnClickListener {
            if (tv_itemCount.text.toString().toInt() > 0) {
                ViewModel.db.collection("consumables").document(ViewModel.item).update("count", FieldValue.increment(-1))
            }
        }
        ib_decrease.setOnLongClickListener {
            if (tv_itemCount.text.toString().toInt() > 10) {
                ViewModel.db.collection("consumables").document(ViewModel.item).update("count", FieldValue.increment(-10))
            }
            else if (tv_itemCount.text.toString().toInt() > 0) {
                ViewModel.db.collection("consumables").document(ViewModel.item).update("count", 0)
            }
            true
        }

        ib_increase.setOnClickListener {
            ViewModel.db.collection("consumables").document(ViewModel.item).update("count", FieldValue.increment(1))
        }
        ib_increase.setOnLongClickListener {
            ViewModel.db.collection("consumables").document(ViewModel.item).update("count", FieldValue.increment(10))
            true
        }


        getItemInfo()

    }

    private fun getItemInfo() {
        val iId = ViewModel.item

        ViewModel.db.collection("consumables").document(iId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("ConsumableInfoTile", "Listen failed.", e)
                return@addSnapshotListener
            }
            Log.d("ConsumableInfoTile", "$snapshot")
            if (snapshot != null)
            {
                snapshot.toObject<ConsumableData>()?.let {
                    tv_itemCount.text = it.count.toString()
                }
            }
        }
    }
}