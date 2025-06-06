package com.example.carwashapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.carwashapp.fragments.BookFragment
import com.example.carwashapp.fragments.HomeFragment
import com.example.carwashapp.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNavigationView)


        loadFragment(HomeFragment())

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> loadFragment(HomeFragment())
                R.id.bookFragment -> loadFragment(BookFragment())
                R.id.profileFragment -> loadFragment(ProfileFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
