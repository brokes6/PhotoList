package com.laboratory.gallery.smart

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.laboratory.gallery.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolBar)
        NavigationUI.setupActionBarWithNavController(this, findNavController(R.id.fragment))
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || findNavController(R.id.fragment).navigateUp()
    }

}