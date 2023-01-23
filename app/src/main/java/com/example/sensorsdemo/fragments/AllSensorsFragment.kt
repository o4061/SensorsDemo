package com.example.sensorsdemo.fragments

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sensorsdemo.MainActivity
import com.example.sensorsdemo.adapter.SensorsAdapter
import com.example.sensorsdemo.databinding.FragmentAllSensorsBinding

class AllSensorsFragment : Fragment() {
    private var _binding: FragmentAllSensorsBinding? = null
    private val binding get() = _binding!!

    private lateinit var sensorManager: SensorManager
    private lateinit var sensorsAdapter: SensorsAdapter
    private lateinit var sensorList: List<Sensor>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllSensorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = "All Sensors"
        sensorManager = requireActivity().getSystemService(SENSOR_SERVICE) as SensorManager
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL)
        setupSensorRecyclerView()
        setupSearchSensor()
    }

    private fun setupSearchSensor() {
        binding.sensorSearchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count >= 2) {
                    sensorsAdapter.differ.submitList(sensorList.filter {
                        it.name.contains(s!!, true)
                    })
                } else {
                    sensorsAdapter.differ.submitList(sensorList.sortedBy { it.type })
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupSensorRecyclerView() {
        sensorsAdapter = SensorsAdapter(sensorManager)
        binding.sensorRv.apply {
            adapter = sensorsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        sensorsAdapter.differ.submitList(sensorList.sortedBy { it.type })
        sensorsAdapter.setOnItemClickListener {
            val action =
                AllSensorsFragmentDirections.actionAllSensorsFragmentToSensorDetailsFragment(it.type)
            Navigation.findNavController(requireView()).navigate(action)
        }
    }
}