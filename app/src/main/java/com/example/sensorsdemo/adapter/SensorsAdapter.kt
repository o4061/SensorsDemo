package com.example.sensorsdemo.adapter

import android.annotation.SuppressLint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sensorsdemo.R
import com.example.sensorsdemo.databinding.SensorAdapterItemBinding

class SensorsAdapter(val sensorManager: SensorManager) :
    RecyclerView.Adapter<SensorsAdapter.SensorsViewHolder>() {
    inner class SensorsViewHolder(val binding: SensorAdapterItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Sensor>() {
        override fun areItemsTheSame(
            oldItem: Sensor,
            newItem: Sensor
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: Sensor,
            newItem: Sensor
        ): Boolean {
            return oldItem.name == newItem.name
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorsViewHolder {
        return SensorsViewHolder(
            SensorAdapterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SensorsViewHolder, position: Int) {
        val sensor = differ.currentList[position]
        val binding = holder.binding

        holder.itemView.apply {
            binding.sensorName.text = "Name: ${sensor.name}"
            binding.sensorVendor.text = "Vendor: ${sensor.vendor}"
            binding.sensorVersion.text = "Version: ${sensor.version}"
            binding.sensorType.text = "Type: ${sensor.type}"
            binding.sensorTypeTitle.text = "Type: ${sensor.stringType}"
            binding.sensorMaxRange.text = "MaxRange: ${sensor.maximumRange}"
            binding.sensorResolution.text = "Resolution: ${sensor.resolution}"
            binding.sensorPower.text = "Power: ${sensor.power}"
            binding.sensorDelay.text = "DelayRange: ${sensor.minDelay} - ${sensor.maxDelay}"
//            val strokeColor = if (sensor) R.color.green else R.color.red
//            binding.root.strokeColor = ContextCompat.getColor(holder.itemView.context, strokeColor)
            setOnClickListener {
                onItemClickListener?.let { it(sensor) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Sensor) -> Unit)? = null

    fun setOnItemClickListener(listener: (Sensor) -> Unit) {
        onItemClickListener = listener
    }
}