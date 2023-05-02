package zephyr.ALHR.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import zephyr.ALHR.R
import zephyr.ALHR.SharedViewModel
import zephyr.ALHR.databinding.ActivityMainBinding
import zephyr.ALHR.fragments.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("AppCompatMethod")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actionBar?.hide()
        val sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        sharedViewModel.updateAnimalList(this)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setupWithNavController(findNavController(R.id.navhost))
    }



}


