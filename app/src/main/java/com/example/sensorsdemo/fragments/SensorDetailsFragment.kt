package com.example.sensorsdemo.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sensorsdemo.MainActivity
import com.example.sensorsdemo.R
import com.example.sensorsdemo.databinding.FragmentSensorDetailsBinding
import com.example.sensorsdemo.utils.SensorImage

class SensorDetailsFragment : Fragment(), SensorEventListener {
    private var _binding: FragmentSensorDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sensor: Sensor
    private lateinit var sensorManager: SensorManager
    private var isSensorEnabled = false
    private val args: SensorDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSensorDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        setupGyroscopeFragment()
        setupLayout()
        (activity as MainActivity).supportActionBar?.title =
            if (this::sensor.isInitialized) sensor.name
            else "Error with sensor"

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun setupLayout() {
        if (isSensorEnabled) {
            binding.enableSensorTv.text = "Enabled"
            binding.enableSensorIv.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
        } else {
            binding.enableSensorTv.text = "Disabled"
            binding.enableSensorIv.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red))
        }
        binding.sensorImage.setImageResource(SensorImage().getSensorImage(args.sensorType))
    }

    @SuppressLint("SetTextI18n")
    private fun setupGyroscopeFragment() {
        try {
            sensor = sensorManager.getDefaultSensor(args.sensorType)
            isSensorEnabled =
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onSensorChanged(event: SensorEvent?) {
        var result = ""
        event?.values?.indices?.forEach {
            result += "value[$it]: ${String.format("%.2f", event.values?.get(it))} \n"
        }
        binding.valuesTv.text = result
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}