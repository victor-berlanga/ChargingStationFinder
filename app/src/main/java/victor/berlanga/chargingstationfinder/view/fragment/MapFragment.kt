package victor.berlanga.chargingstationfinder.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import victor.berlanga.chargingstationfinder.MainActivity
import victor.berlanga.chargingstationfinder.data.local.entity.StationEntity
import victor.berlanga.chargingstationfinder.databinding.FragmentMapBinding
import victor.berlanga.chargingstationfinder.viewmodel.StationViewModel

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val stationViewModel: StationViewModel by activityViewModels()
    private var selectedMarkerStationId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupMap()
        observeStations()
    }

    private fun setupMap() {
        Configuration.getInstance().userAgentValue = requireContext().packageName
        binding.mapView.setTileSource(TileSourceFactory.MAPNIK)
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.controller.setZoom(11.5)
        binding.mapView.controller.setCenter(GeoPoint(25.6866, -100.3161))
    }

    private fun observeStations() {
        stationViewModel.stations.observe(viewLifecycleOwner) { stations ->
            renderMarkers(stations)
        }
    }

    private fun renderMarkers(stations: List<StationEntity>) {
        binding.mapView.overlays.clear()

        val validStations = stations.filter { station ->
            station.latitude in -90.0..90.0 &&
                station.longitude in -180.0..180.0 &&
                station.latitude != 0.0 &&
                station.longitude != 0.0
        }

        validStations.forEach { station ->
            val marker = Marker(binding.mapView)
            marker.position = GeoPoint(station.latitude, station.longitude)
            marker.title = station.name
            marker.snippet = """
                ${station.municipality}
                Conector: ${station.connectorStandard.ifBlank { "Sin dato" }}
                Cargadores: ${station.chargerCount}
                Uso: ${station.usageType.ifBlank { "Sin dato" }}
                Toca otra vez para abrir detalles
            """.trimIndent()
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.setOnMarkerClickListener { clickedMarker, _ ->
                if (selectedMarkerStationId == station.id) {
                    (activity as? MainActivity)?.showStationDetail(station.id)
                } else {
                    selectedMarkerStationId = station.id
                    clickedMarker.showInfoWindow()
                }
                true
            }

            binding.mapView.overlays.add(marker)
        }

        binding.txtMapMessage.text = if (validStations.isEmpty()) {
            "No hay estaciones con coordenadas validas."
        } else {
            "Estaciones en el mapa: ${validStations.size}. Toca un marcador para ver detalles."
        }
        binding.mapView.invalidate()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        binding.mapView.overlays.clear()
        selectedMarkerStationId = null
        super.onDestroyView()
        _binding = null
    }
}
