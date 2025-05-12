package com.example.carwashapp.dashboards

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.carwashapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class ClientDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_dashboard)

        val navView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(navView, navController)
    }
}

