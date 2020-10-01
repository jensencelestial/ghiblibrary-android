package com.jensencelestial.ghiblibrary.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.jensencelestial.ghiblibrary.android.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.ThemeBase)
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)

        setSupportActionBar(binding.appBarHome.tlbrHome)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration =
            AppBarConfiguration(
                setOf(
                    R.id.filmsFragment,
                    R.id.locationsFragment,
                    R.id.peopleFragment,
                    R.id.speciesFragment,
                    R.id.vehiclesFragment,
                    R.id.aboutFragment,
                ),
                binding.drwLytHome
            )
        binding.navVwHome.setupWithNavController(navController)
        binding.appBarHome.tlbrHome.setupWithNavController(navController, appBarConfiguration)

        initEvents()

        setContentView(binding.root)
    }

    private fun initEvents() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            title = when (destination.id) {
                R.id.filmsFragment -> getString(R.string.films_pascal)
                R.id.locationsFragment -> getString(R.string.locations_pascal)
                R.id.peopleFragment -> getString(R.string.people_pascal)
                R.id.speciesFragment -> getString(R.string.species_pascal)
                R.id.vehiclesFragment -> getString(R.string.vehicles_pascal)
                R.id.aboutFragment -> getString(R.string.about_pascal)
                else -> ""
            }
        }
    }
}