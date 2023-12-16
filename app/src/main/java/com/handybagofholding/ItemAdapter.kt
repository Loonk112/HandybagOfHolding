package com.handybagofholding

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.handybagofholding.activity.ItemActivity
import com.handybagofholding.data.ItemMetaData

class ItemAdapter (private val itemList: ArrayList<ItemMetaData>, private val activityContext: Context): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemViewModel = itemList[position]

        holder.tvItemName.text = itemViewModel.name

        when(itemViewModel.category)
        {
            "weapons" -> holder.tvItemCategory.text = activityContext.getString(R.string.weapons_Capital)
            "armour" -> holder.tvItemCategory.text = activityContext.getString(R.string.armour_Capital)
            "consumables" -> holder.tvItemCategory.text = activityContext.getString(R.string.consumables_Capital)
            else -> holder.tvItemCategory.text = activityContext.getString(R.string.other_Capital)
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

                    ViewModel.db.runTransaction { transaction ->

                        transaction.delete(ViewModel.db.collection("items").document(itemViewModel.id))
                        if (itemViewModel.category != "other") {
                            transaction.delete(ViewModel.db.collection(itemViewModel.category).document(itemViewModel.id))
                        }
                        transaction.delete(ViewModel.db.collection("item_notes").document(itemViewModel.id))

                    }.addOnSuccessListener {
                        Log.d("Firebase", "Item was deleted")
                        Toast.makeText(activityContext, "${itemViewModel.name} was deleted.", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { e ->
                        Log.w("Firebase", "Error deleting document", e)
                        Toast.makeText(activityContext, "${itemViewModel.name} was NOT deleted!", Toast.LENGTH_SHORT).show()
                    }


                }
            }
            true
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvItemName: TextView = itemView.findViewById(R.id.tv_itemName)
        val tvItemCategory: TextView = itemView.findViewById(R.id.tv_itemCategory)
        val myItemView = itemView
    }

}