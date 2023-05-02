package zephyr.ALHR.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import zephyr.ALHR.RecyclerAdapter
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

    @SuppressLint("NotifyDataSetChanged")
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
        val myRecyclerAdapter = RecyclerAdapter(sharedViewModel.lamaList){ p:Int ->
            val bundle = Bundle()
            bundle.putInt("infoPos", p)
            navCtrl.navigate(R.id.toAnimalViewer, bundle)
        }
        recycler.adapter = myRecyclerAdapter
        recycler.layoutManager = LinearLayoutManager(requireContext())


        //This handles the swipe delete
        val swipeToDeleteCallback = object : ItemTouchHelper.SimpleCallback(0 , ItemTouchHelper.LEFT) {
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
                deleteDialog(requireContext(), position, sharedViewModel, recycler)
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

    @SuppressLint("NotifyDataSetChanged")
    fun deleteDialog(context: Context, position: Int, vm:SharedViewModel, recycler: RecyclerView){
        val tempLama = vm.lamaList[position]
        val dialog = Dialog(context, R.style.CustomAlertDialog)

        dialog.setContentView(R.layout.confirmation_popup)
        val displayMetrics = context.resources.displayMetrics

        val width = (displayMetrics.widthPixels * 0.8).toInt()
        dialog.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        // Set the text of the title TextView
        val titleTextView = dialog.findViewById<TextView>(R.id.popup_title)
        titleTextView.text = "Are you sure you want to delete this item?"
        vm.removeAnimal(position)
        recycler.adapter?.notifyItemRemoved(position)
        vm.saveAnimalDataToFile(context)

        // Set click listeners for the Cancel and Confirm buttons
        val cancelButton = dialog.findViewById<Button>(R.id.popup_cancel_button)
        cancelButton.setOnClickListener {
            vm.lamaList.add(position, tempLama)
            recycler.adapter?.notifyDataSetChanged()
            vm.saveAnimalDataToFile(context)
            dialog.dismiss()
        }

        val confirmButton = dialog.findViewById<Button>(R.id.popup_confirm_button)
        confirmButton.setOnClickListener {

            dialog.dismiss()
        }


        dialog.show()
    }

}