package com.handybagofholding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.handybagofholding.R

class CharacterDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_character_details, container, false)

        view.findViewById<ImageButton>(R.id.ib_return).setOnClickListener {
            requireActivity().finish()
        }

        return view

    }

}