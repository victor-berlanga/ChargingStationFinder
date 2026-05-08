package victor.berlanga.chargingstationfinder.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import victor.berlanga.chargingstationfinder.MainActivity
import victor.berlanga.chargingstationfinder.databinding.FragmentStationListBinding
import victor.berlanga.chargingstationfinder.view.adapter.StationAdapter
import victor.berlanga.chargingstationfinder.viewmodel.FavoritesViewModel
import victor.berlanga.chargingstationfinder.viewmodel.StationViewModel

class StationListFragment : Fragment() {

    private var _binding: FragmentStationListBinding? = null
    private val binding get() = _binding!!
    private val stationViewModel: StationViewModel by activityViewModels()
    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private lateinit var adapter: StationAdapter
    private var filtersVisible = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStationListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = StationAdapter { station ->
            (activity as? MainActivity)?.showStationDetail(station.id)
        }

        binding.recyclerStations.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerStations.adapter = adapter

        binding.btnSearch.setOnClickListener {
            applySearchAndFilters()
        }

        binding.btnClearSearch.setOnClickListener {
            binding.edtSearch.setText("")
            applySearchAndFilters()
        }

        binding.btnToggleFilters.setOnClickListener {
            filtersVisible = !filtersVisible
            updateFilterVisibility()
        }

        binding.btnApplyFilters.setOnClickListener {
            applySearchAndFilters()
        }

        binding.btnClearFilters.setOnClickListener {
            binding.edtSearch.setText("")
            binding.edtConnector.setText("")
            binding.edtMunicipality.setText("")
            binding.radioUsage.clearCheck()
            binding.radioCurrent.clearCheck()
            applySearchAndFilters()
        }

        val onlyFavorites = requireArguments().getBoolean(ARG_FAVORITES, false)

        if (onlyFavorites) {
            binding.txtScreenTitle.text = "Favoritas"
            binding.searchContainer.visibility = View.GONE
            binding.filterContainer.visibility = View.GONE
            favoritesViewModel.favorites.observe(viewLifecycleOwner) { stations ->
                adapter.submitList(stations)
                showCount(stations.size)
            }
        } else {
            binding.txtScreenTitle.text = "Estaciones"
            updateFilterVisibility()
            stationViewModel.filteredStations.observe(viewLifecycleOwner) { stations ->
                adapter.submitList(stations)
                showCount(stations.size)
            }
            applySearchAndFilters()
        }
    }

    private fun applySearchAndFilters() {
        stationViewModel.filterStations(
            binding.edtSearch.text.toString(),
            binding.edtConnector.text.toString(),
            binding.edtMunicipality.text.toString(),
            selectedUsageType(),
            selectedCurrentType()
        )
        updateFilterSummary()
    }

    private fun selectedUsageType(): String {
        return when (binding.radioUsage.checkedRadioButtonId) {
            binding.rbPublic.id -> "Public"
            binding.rbPrivate.id -> "Private"
            else -> ""
        }
    }

    private fun selectedCurrentType(): String {
        return when (binding.radioCurrent.checkedRadioButtonId) {
            binding.rbAc.id -> "AC"
            binding.rbDc.id -> "DC"
            else -> ""
        }
    }

    private fun showCount(count: Int) {
        binding.txtResultCount.text = "Resultados: $count"
        binding.txtEmptyList.visibility = if (count == 0) View.VISIBLE else View.GONE
    }

    private fun updateFilterVisibility() {
        binding.filterContainer.visibility = if (filtersVisible) View.VISIBLE else View.GONE
        binding.btnToggleFilters.text = if (filtersVisible) "Ocultar filtros" else "Mostrar filtros"
    }

    private fun updateFilterSummary() {
        val activeFilters = listOf(
            binding.edtSearch.text.toString(),
            binding.edtConnector.text.toString(),
            binding.edtMunicipality.text.toString(),
            selectedUsageType(),
            selectedCurrentType()
        ).count { it.isNotBlank() }

        binding.txtFilterSummary.text = "Filtros: $activeFilters activos"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_FAVORITES = "favorites"

        fun newInstance(onlyFavorites: Boolean): StationListFragment {
            return StationListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_FAVORITES, onlyFavorites)
                }
            }
        }
    }
}
