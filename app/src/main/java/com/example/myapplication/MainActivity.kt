package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        fun replaceFragment(frag: Fragment) {
            //Fragment 설정
            supportFragmentManager.beginTransaction().run {
                replace(binding.frmFrag.id, frag)
                commit()
            }
        }

        replaceFragment(MainpageFragment())

        setContentView(binding.root)
    }
}

