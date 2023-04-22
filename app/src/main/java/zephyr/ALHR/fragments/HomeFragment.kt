package zephyr.ALHR.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import zephyr.ALHR.R
import zephyr.ALHR.SharedViewModel
import zephyr.ALHR.databinding.FragmentHomeBinding
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import zephyr.ALHR.Lama

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var navCtrl:NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navCtrl = findNavController()
        val sharedViewModel =
            ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        //This is a test section.
        val serializeBtn = view.findViewById<Button>(R.id.btnSerialize)

        serializeBtn.setOnClickListener{
            val animals = sharedViewModel.lamaList
            val myJson = Json.encodeToString(animals)
            Toast.makeText(requireContext(),myJson, Toast.LENGTH_SHORT).show()
        }

        val deserializeBtn = view.findViewById<Button>(R.id.btnDeserialize)
        deserializeBtn.setOnClickListener{
            val animals = sharedViewModel.lamaList
            val myJson = Json.encodeToString(animals)
            val jsonToLama = Json.decodeFromString<List<Lama>>(myJson)
            Toast.makeText(requireContext(),(jsonToLama[0].properName + jsonToLama[0].barnName), Toast.LENGTH_SHORT).show()
        }

        val numLlamasTxt = view.findViewById<TextView>(R.id.txtTotalLlamas)
        val numAlpacasTxt = view.findViewById<TextView>(R.id.txtTotalAlpacas)
        sharedViewModel.liveLamaList.observe(viewLifecycleOwner, Observer {
            numLlamasTxt.text = "# Llamas\n" + it.count { it.species == "Llama" }.toString()
            numAlpacasTxt.text = "# Alpacas\n" + it.count { it.species == "Alpaca" }.toString()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}