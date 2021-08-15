package com.example.tekotest2.screens.templatescreen.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.example.tekotest2.R
import com.example.tekotest2.databinding.ItemOptionColorLayoutBinding
import com.example.tekotest2.model.Color

class OptionColorAdapter( var listValueCondition: List<Color>) :
    BaseAdapter() {


    var mOnSelectOptionListener: OnSelectOptionListener? = null
    set(value) {
        field = value
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var option : Color? = listValueCondition[position]
        var binding : ItemOptionColorLayoutBinding? = null
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_option_color_layout, parent, false)
        binding = DataBindingUtil.bind(view)
        view.tag = binding
        binding?.nameTv?.text = option?.name
        binding?.root?.setOnClickListener {
            run {
                mOnSelectOptionListener?.onSelect(option)
                convertView?.performClick()
            }
        }
        return view

    }

    override fun getCount(): Int {
        return listValueCondition.size
    }

    override fun getItem(color: Int): Any {
        return listValueCondition[color]
    }

    override fun getItemId(color: Int): Long {
        return 0
    }

    interface OnSelectOptionListener {
        fun  onSelect(option: Color?)
    }
}