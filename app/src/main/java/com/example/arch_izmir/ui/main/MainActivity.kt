package com.example.arch_izmir.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.arch_izmir.R
import com.example.arch_izmir.databinding.ActivityMainBinding
import com.example.arch_izmir.databinding.FragmentHomeBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // bottom geçişi
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController


        binding.bottomNavView.setupWithNavController(navController)

        // Login ve Register ekranlarında Bottom menu gizlenecek
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.registerFragment, R.id.loginFragment ->{
                    binding.bottomNavView.visibility = View.GONE
                    binding.bottomNavView.alpha = 0f
                }
                else -> {
                    binding.bottomNavView.visibility = View.VISIBLE
                    binding.bottomNavView.animate()
                        .alpha(1f)
                        .duration = 300
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavView) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Sistem çubuğu boşluğunu korurken üst boşluğu sabit tutuyoruz
            v.updatePadding(
                top = (5 * resources.displayMetrics.density).toInt(),
                bottom = bars.bottom // Sistem çubuğu ile çakışmayı önler
            )
            insets
        }


    }
}