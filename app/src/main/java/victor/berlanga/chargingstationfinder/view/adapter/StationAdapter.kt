package victor.berlanga.chargingstationfinder.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import victor.berlanga.chargingstationfinder.data.local.entity.StationEntity
import victor.berlanga.chargingstationfinder.databinding.ItemStationBinding

class StationAdapter(
    private val showCoordinates: Boolean = false,
    private val onStationClick: (StationEntity) -> Unit
) : RecyclerView.Adapter<StationAdapter.StationViewHolder>() {

    private val stations = mutableListOf<StationEntity>()

    fun submitList(newStations: List<StationEntity>) {
        stations.clear()
        stations.addAll(newStations)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val binding = ItemStationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        holder.bind(stations[position])
    }

    override fun getItemCount(): Int = stations.size

    inner class StationViewHolder(
        private val binding: ItemStationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(station: StationEntity) {
            binding.txtStationName.text = station.name
            binding.txtMunicipality.text = station.municipality
            binding.txtConnector.text = "Conector: ${station.connectorStandard.ifBlank { "Sin dato" }}"
            binding.txtChargers.text = "Cargadores: ${station.chargerCount}"
            binding.txtUsage.text = "Uso: ${station.usageType.ifBlank { "Sin dato" }}"
            binding.txtObservation.text = station.observations.ifBlank { "Sin observaciones" }
            binding.txtCoordinates.text = "Lat: ${station.latitude}, Lon: ${station.longitude}"
            binding.txtCoordinates.visibility = if (showCoordinates) View.VISIBLE else View.GONE
            binding.root.setOnClickListener { onStationClick(station) }
        }
    }
}
