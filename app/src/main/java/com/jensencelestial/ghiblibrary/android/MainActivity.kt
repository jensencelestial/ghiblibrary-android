package com.jensencelestial.ghiblibrary.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.tlbrHome))

        navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        findViewById<Toolbar>(R.id.tlbrHome)
            .setupWithNavController(navController, appBarConfiguration)
        findViewById<BottomNavigationView>(R.id.btmnvHome).setupWithNavController(navController)

        initEvents()
    }

    private fun initEvents() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            title = when (destination.id) {
                R.id.filmsFragment -> getString(R.string.films_pascal)
                R.id.moreFragment -> getString(R.string.more_pascal)
                else -> ""
            }
        }
    }
}