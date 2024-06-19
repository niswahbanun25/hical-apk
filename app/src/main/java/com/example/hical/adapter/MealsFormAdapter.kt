package com.example.hical.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.hical.databinding.ItemMealsFormRowBinding

class MealsFormAdapter(private var rowTotal: Int = 1) :
    RecyclerView.Adapter<MealsFormAdapter.MainViewHolder>() {

    private val editTextValues: MutableMap<Int, ArrayList<String>> = mutableMapOf()

    @SuppressLint("NotifyDataSetChanged")
    fun addRow() {
        rowTotal++
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            ItemMealsFormRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = rowTotal

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MainViewHolder(private val itemBinding: ItemMealsFormRowBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(position: Int) {
            val inputtedText = arrayListOf("", "")
            itemBinding.edMeal.addTextChangedListener(onTextChanged = { meal, _, _, _ ->
                inputtedText[0] = meal.toString()
                editTextValues[position] = inputtedText
            })
            itemBinding.edCal.addTextChangedListener(onTextChanged = { cal, _, _, _ ->
                inputtedText[1] = cal.toString()
                editTextValues[position] = inputtedText
            })
        }
    }

    fun getInputtedEditText() = editTextValues
}