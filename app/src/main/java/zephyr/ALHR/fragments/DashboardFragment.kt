package zephyr.ALHR.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import zephyr.ALHR.Adapter
import zephyr.ALHR.Lama
import zephyr.ALHR.R
import zephyr.ALHR.SharedViewModel
import zephyr.ALHR.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    lateinit var navCtrl:NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        navCtrl = findNavController()

        val createRecordBtn = view.findViewById<Button>(R.id.btnCreateRecord)
        createRecordBtn.setOnClickListener{
            navCtrl.navigate(R.id.createAnimalRecord)
        }

        //This handles the dropdown filter section
        val filterLayout = view.findViewById<LinearLayout>(R.id.filterLayout)
        val filterBtn = view.findViewById<Button>(R.id.btnFilters)
        var flag = false
        filterBtn.setOnClickListener{
            flag = if (!flag) {
                filterLayout.animate()
                    .setDuration(500)
                    .translationY(filterLayout.height.toFloat() / 2)
                    .start()
                true
            } else{
                filterLayout.animate()
                    .setDuration(500)
                    .translationY(0f)
                    .start()
                false
            }
        }


        //Recycler View Setup Section
        val recycler = view.findViewById<RecyclerView>(R.id.animal_selector)
        val myAdapter = Adapter(sharedViewModel.lamaList){ p:Int ->
            val bundle = Bundle()
            bundle.putInt("infoPos", p)
            navCtrl.navigate(R.id.toAnimalViewer, bundle)
        }
        recycler.adapter = myAdapter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        //This handles the swipe delete
        val swipeToDeleteCallback = object : ItemTouchHelper.SimpleCallback(
            0 /* ignore row drag */,
            ItemTouchHelper.RIGHT /* handle write swipe only */
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // Ignore moving rows in the list
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val msg = "Are you sure you want to delete ${sharedViewModel.lamaList[position].barnName}?"
                val tempLama = sharedViewModel.lamaList[position]
                sharedViewModel.removeAnimal(position)
                recycler.adapter?.notifyItemRemoved(position)
                sharedViewModel.saveAnimalDataToFile(requireContext())

                Snackbar.make(viewHolder.itemView, msg, 5000)
                    .setAction("No") {
                        sharedViewModel.lamaList.add(position, tempLama)
                        recycler.adapter?.notifyDataSetChanged()
                        sharedViewModel.saveAnimalDataToFile(requireContext())
                    }
                    .show()
            }

        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recycler)

        val filterSpeciesBtn = view.findViewById<Button>(R.id.btnSpeciesFilter)
        filterSpeciesBtn.setOnClickListener{
            sharedViewModel.sortBySpecies()
            recycler.adapter?.notifyDataSetChanged()
        }



    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}