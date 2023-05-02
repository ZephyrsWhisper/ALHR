package zephyr.ALHR.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import zephyr.ALHR.R
import zephyr.ALHR.activities.MainActivity

class ViewPagerFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = view.findViewById<ViewPager2>(R.id.mainViewPager)
        viewPager.adapter = ViewPagerAdapter(requireActivity())
        val navView: BottomNavigationView = view.findViewById(R.id.nav_view)
        navView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> viewPager.setCurrentItem(0, true)
                R.id.navigation_dashboard -> viewPager.setCurrentItem(1, true)
                R.id.navigation_health_recorder -> viewPager.setCurrentItem(2, true)
                R.id.navigation_settings -> viewPager.setCurrentItem(3, true)
            }

            true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_view_pager, container, false)
    }


    private class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment()
                1 -> DashboardFragment()
                2 -> HealthRecorderFragment()
                else -> SettingsFragment()
            }
        }
    }

}