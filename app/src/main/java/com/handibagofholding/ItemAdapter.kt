package com.handibagofholding

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter (val itemList: ArrayList<ItemMetaData>, private val activityContext: Context): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemViewModel = itemList[position]

        holder.tv_itemName.text = itemViewModel.name

        when(itemViewModel.category)
        {
            "weapons" -> holder.tv_itemCategory.text = "Weapons"
            "armours" -> holder.tv_itemCategory.text = "Armour"
            else -> holder.tv_itemCategory.text = "Other"
        }

        holder.myItemView.setOnClickListener {
            val intent = Intent(activityContext, ItemActivity::class.java)
            ViewModel.item = itemViewModel.id
            activityContext.startActivity(intent)
        }

        holder.myItemView.setOnLongClickListener {
            AlertDialog(activityContext).show("You are about to delete ${itemViewModel.name}.\nIt is irreversible.\nAre you sure?"){
                if (it == AlertDialog.ResponseType.YES) {
                    Log.d("AlertDialog", "YES")
                    ViewModel.db.collection("items").document("${itemViewModel.id}")
                        .delete()
                        .addOnSuccessListener {
                            Log.d("Firebase", "Item was deleted")
                            Toast.makeText(activityContext, "${itemViewModel.name} was deleted.", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e -> Log.w("Firebase", "Error deleting document", e) }
                }
            }
            true
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView) {
        val tv_itemName = itemView.findViewById<TextView>(R.id.tv_itemName)
        val tv_itemCategory = itemView.findViewById<TextView>(R.id.tv_itemCategory)
        val myItemView = itemView
    }

}