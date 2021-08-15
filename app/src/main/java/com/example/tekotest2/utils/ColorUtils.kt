package com.example.tekotest2.utils

import com.example.tekotest2.model.Color

class ColorUtils {
    companion object {
        val arrayListColor: ArrayList<Color> = ArrayList()
        fun contain(id : Int) : Boolean{
            val color = Color()
            color.id = id
            val result = arrayListColor.binarySearch(color,object : Comparator<Color>{
                override fun compare(color0: Color?, color1: Color?): Int {
                    color0?.id?.let {id0->
                        color1?.id?.let {id1->
                            return id0 - id1
                        }
                    }
                    return 0
                }

            },0, arrayListColor.size)
            return result >= 0
        }
        fun getColor(id : Int) : Color?{
            val color = Color()
            color.id = id
            val result = arrayListColor.binarySearch(color,object : Comparator<Color>{
                override fun compare(color0: Color?, color1: Color?): Int {
                    color0?.id?.let {id0->
                        color1?.id?.let {id1->
                            return id0 - id1
                        }
                    }
                    return 0
                }

            },0, arrayListColor.size)
            return if (result >= 0 && result < arrayListColor.size) {
                arrayListColor[result]
            } else {
                null
            }
        }
    }

}