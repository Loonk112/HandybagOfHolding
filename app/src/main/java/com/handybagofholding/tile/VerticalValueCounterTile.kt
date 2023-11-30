package com.handybagofholding.tile

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.handybagofholding.R

@SuppressLint("SetTextI18n")
class VerticalValueCounterTile @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttribute: Int = 0
) : LinearLayout(context, attributeSet, defaultStyleAttribute) {

    private val tvValue: TextView
    private val ibIncrease: ImageButton
    private val ibDecrease: ImageButton


    init {
        inflate(context, R.layout.tile_vertical_value_counter, this)

        tvValue = findViewById(R.id.tv_value)
        ibDecrease = findViewById(R.id.ib_decrease)
        ibIncrease = findViewById(R.id.ib_increase)

        ibIncrease.setOnClickListener {
            tvValue.text = (tvValue.text.toString().toInt() + 1).toString()
        }

        ibDecrease.setOnClickListener {
            tvValue.text = if (tvValue.text.toString().toInt() <= 0) "0" else ((tvValue.text.toString().toInt() - 1)).toString()
        }

    }

    fun setValue(value: Int) {
        if (value <= 0) tvValue.text = "0"
        else tvValue.text = value.toString()
    }

    fun getValue(): Int {
        return tvValue.text.toString().toInt()
    }

}