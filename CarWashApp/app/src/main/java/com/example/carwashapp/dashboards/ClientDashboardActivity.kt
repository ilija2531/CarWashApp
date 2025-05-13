package com.example.carwashapp.dashboards

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.carwashapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class ClientDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_dashboard)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val navController = navHostFragment?.findNavController()

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navController?.let {
            bottomNavView.setupWithNavController(it)
        }
    }
}
