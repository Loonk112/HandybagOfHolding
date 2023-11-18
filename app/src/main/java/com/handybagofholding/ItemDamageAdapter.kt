package com.handybagofholding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemDamageAdapter (private val damageList: ArrayList<ItemDamageData>, private val activityContext: Context): RecyclerView.Adapter<ItemDamageAdapter.ViewHolder>() {

    var canRemoveItems: Boolean = true

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context) //TODO: View
            .inflate(R.layout.layout_item_damage, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val damageViewModel = damageList[position]

        holder.type.text = damageViewModel.type.replaceFirstChar { char -> char.uppercaseChar() }
        holder.count.text = damageViewModel.count.toString()
        holder.dice.text = damageViewModel.dice

        if (canRemoveItems) {
            holder.view.setOnLongClickListener {
                damageList.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return damageList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val type: TextView = itemView.findViewById(R.id.tv_damageType)
        val count: TextView = itemView.findViewById(R.id.tv_damageCount)
        val dice: TextView = itemView.findViewById(R.id.tv_damageDice)

        val view = itemView
    }
}