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

class ItemDamageAdapter (private val damageList: ArrayList<ItemDamageData>, private val activityContext: Context): RecyclerView.Adapter<ItemDamageAdapter.ViewHolder>() {

    public var canRemoveItems: Boolean = true

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemDamageAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context) //TODO: View
            .inflate(R.layout.item_damage_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val damageViewModel = damageList[position]

        holder.type.text = damageViewModel.type.replaceFirstChar { char -> char.uppercaseChar() }
        holder.count.text = damageViewModel.count.toString()
        holder.dice.text = damageViewModel.dice.toString()

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

    class ViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView) {
        val type = itemView.findViewById<TextView>(R.id.tv_damageType)
        val count = itemView.findViewById<TextView>(R.id.tv_damageCount)
        val dice = itemView.findViewById<TextView>(R.id.tv_damageDice)

        val view = itemView
    }
}