package victor.berlanga.chargingstationfinder.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import victor.berlanga.chargingstationfinder.MainActivity
import victor.berlanga.chargingstationfinder.data.local.entity.StationEntity
import victor.berlanga.chargingstationfinder.databinding.FragmentStationDetailBinding
import victor.berlanga.chargingstationfinder.viewmodel.FavoritesViewModel
import victor.berlanga.chargingstationfinder.viewmodel.StationViewModel

class StationDetailFragment : Fragment() {

    private var _binding: FragmentStationDetailBinding? = null
    private val binding get() = _binding!!
    private val stationViewModel: StationViewModel by activityViewModels()
    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private var stationId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStationDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        stationId = requireArguments().getInt(ARG_STATION_ID)
        stationViewModel.selectStation(stationId)
        favoritesViewModel.checkFavorite(stationId)

        stationViewModel.selectedStation.observe(viewLifecycleOwner) { station ->
            if (station != null && station.id == stationId) {
                showStation(station)
            }
        }

        favoritesViewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            binding.btnFavorite.text = if (isFavorite) "Quitar favorito" else "Guardar favorito"
        }

        binding.btnFavorite.setOnClickListener {
            favoritesViewModel.toggleFavorite(stationId)
        }

        binding.btnReviews.setOnClickListener {
            (activity as? MainActivity)?.showReviews(stationId)
        }

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun showStation(station: StationEntity) {
        binding.txtDetailName.text = station.name
        binding.txtAddress.text = "Dirección: ${station.address}"
        binding.txtMunicipality.text = "Municipio: ${station.municipality}"
        binding.txtCoordinates.text = "Coordenadas: ${station.latitude}, ${station.longitude}"
        binding.txtUsage.text = "Uso: ${station.usageType}"
        binding.txtChargers.text = "Cargadores: ${station.chargerCount}"
        binding.txtPower.text = "Potencia: ${station.power.ifBlank { "Sin dato" }}"
        binding.txtConnector.text = "Conector: ${station.connectorStandard.ifBlank { "Sin dato" }}"
        binding.txtCurrent.text = "Corriente: ${station.currentType.ifBlank { "Sin dato" }}"
        binding.txtMainUse.text = "Uso principal: ${station.mainUse.ifBlank { "Sin dato" }}"
        binding.txtInsideOf.text = "Dentro de: ${station.insideOf.ifBlank { "Independiente" }}"
        binding.txtObservations.text = "Observaciones: ${station.observations.ifBlank { "Sin comentarios" }}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_STATION_ID = "station_id"

        fun newInstance(stationId: Int): StationDetailFragment {
            return StationDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_STATION_ID, stationId)
                }
            }
        }
    }
}
