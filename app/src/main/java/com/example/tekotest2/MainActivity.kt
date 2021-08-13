package com.example.tekotest2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tekotest2.screens.templatescreen.TemplateFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startFragment()
    }

    private fun startFragment() {
        var fragmentManager = supportFragmentManager
        var ft = fragmentManager.beginTransaction()
        ft.add(R.id.main_id,TemplateFragment())
        ft.commit()
    }
}