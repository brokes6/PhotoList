package com.laboratory.gallery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.laboratory.gallery.databinding.ActivityHomeBinding
import com.laboratory.gallery.self_control.AutoLoadActivity
import com.laboratory.gallery.smart.MainActivity

class HomeActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        binding.button1.setOnClickListener(this)
        binding.button2.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.button1 ->{
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
            R.id.button2 ->{
                val intent = Intent(this,AutoLoadActivity::class.java)
                startActivity(intent)
            }
        }
    }
}