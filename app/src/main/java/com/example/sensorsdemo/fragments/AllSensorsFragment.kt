package com.example.sensorsdemo.fragments

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
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
        setupSensorRecyclerView()
    }

    private fun setupSensorRecyclerView() {
        sensorsAdapter = SensorsAdapter(sensorManager)
        binding.sensorRv.apply {
            adapter = sensorsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        sensorsAdapter.differ.submitList(getAllAvailableSensors())
        sensorsAdapter.setOnItemClickListener {
            val action =
                AllSensorsFragmentDirections.actionAllSensorsFragmentToSensorDetailsFragment(it.type)
            Navigation.findNavController(requireView()).navigate(action)
        }
    }

    private fun getAllAvailableSensors(): List<Sensor> {
        return sensorManager.getSensorList(Sensor.TYPE_ALL).sortedBy { it.type }
    }
}