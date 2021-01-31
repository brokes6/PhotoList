package com.laboratory.gallery.self_control

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.laboratory.gallery.R
import kotlinx.android.synthetic.main.activity_auto_load.*
import kotlinx.android.synthetic.main.activity_main.*

class AutoLoadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_load)
        setSupportActionBar(A_toolbar)
    }
}