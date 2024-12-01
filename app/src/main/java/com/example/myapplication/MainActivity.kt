package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        fun replaceFragment(frag: Fragment) {
            supportFragmentManager.beginTransaction().run {
                replace(binding.frmFrag.id, frag)
                commit()
            }
        }

        replaceFragment(MainpageFragment())

        setContentView(binding.root)
    }
}

