package com.example.tekotest2.fragments.listproduct.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.example.tekotest2.R
import com.example.tekotest2.databinding.ItemOptionColorLayoutBinding
import com.example.tekotest2.model.Color

class OptionColorAdapter(var context: Context, private var listValueCondition: List<Color>) : BaseAdapter() {
    var mOnSelectOptionListener: OnSelectOptionListener? = null

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val option: Color = listValueCondition[position]
        val binding: ItemOptionColorLayoutBinding?
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_option_color_layout, parent, false)
        binding = DataBindingUtil.bind(view)
        view.tag = binding
        binding?.nameTv?.text = option.name
        view.setOnClickListener {
            run {
                mOnSelectOptionListener?.onSelect(option)
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
        fun onSelect(option: Color?)
    }
}