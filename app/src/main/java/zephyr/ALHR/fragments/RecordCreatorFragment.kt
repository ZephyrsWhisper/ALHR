package zephyr.ALHR.fragments

import android.os.Bundle
import android.provider.MediaStore.Audio.Radio
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.ScrollView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import zephyr.ALHR.Lama
import zephyr.ALHR.R
import zephyr.ALHR.SharedViewModel
import java.time.Month

class RecordCreatorFragment : Fragment() {

    lateinit var navCtrl:NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_record_creator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navCtrl = findNavController()
        val scrollView = view.findViewById<ScrollView>(R.id.recordScrollView)
        val layout = LayoutInflater.from(requireContext()).inflate(R.layout.scroll_layout, scrollView, false) as LinearLayout
        scrollView.addView(layout)

        val sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        val pNameTxt = view.findViewById<EditText>(R.id.txtPName)
        val bNameTxt = view.findViewById<EditText>(R.id.txtBName)
        val speciesAlpaca = view.findViewById<RadioButton>(R.id.rbtnAlpaca)
        val speciesLlama = view.findViewById<RadioButton>(R.id.rbtnLlama)
        val calendar = view.findViewById<CalendarView>(R.id.calendarView)
        val sexMale = view.findViewById<RadioButton>(R.id.rbtnMale)
        val sexFemale = view.findViewById<RadioButton>(R.id.rbtnFemale)
        val sexGelding = view.findViewById<RadioButton>(R.id.rbtnGelding)
        val saveBtn = view.findViewById<Button>(R.id.btnSave)


        saveBtn.setOnClickListener{
            val sex = when {
                sexMale.isChecked -> "Male"
                sexFemale.isChecked -> "Female"
                sexGelding.isChecked -> "Gelding"
                else -> ""
            }
            val species = when{
                speciesAlpaca.isChecked -> "Alpaca"
                speciesLlama.isChecked -> "Llama"
                else -> ""
            }
            var dob = ""
            calendar.setOnDateChangeListener { _, year, month, dayOfMonth -> dob = "$month + $dayOfMonth + $year" }
            val bName = bNameTxt.text.toString()
            val pName = pNameTxt.text.toString()
            val newLama = Lama(pName, bName, dob, sex, species)
            sharedViewModel.addLama(newLama)
            sharedViewModel.saveAnimalDataToFile(requireContext())
            navCtrl.popBackStack()
        }
    }


}