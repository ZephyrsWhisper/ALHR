package zephyr.ALHR.activities

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import zephyr.ALHR.R
import zephyr.ALHR.SharedViewModel
import zephyr.ALHR.databinding.ActivityMainBinding
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.hide()
        val navView: BottomNavigationView = binding.navView
        val sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        //This is a test section for the shared viewModel
        sharedViewModel.updateAnimalList(this)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)
    }


}

