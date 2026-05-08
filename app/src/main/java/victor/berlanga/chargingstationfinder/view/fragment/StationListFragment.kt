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
            stationViewModel.searchStations(binding.edtSearch.text.toString())
        }

        binding.btnClearSearch.setOnClickListener {
            binding.edtSearch.setText("")
            stationViewModel.searchStations("")
        }

        val onlyFavorites = requireArguments().getBoolean(ARG_FAVORITES, false)
        val isFiltered = requireArguments().getBoolean(ARG_FILTERED, false)

        when {
            onlyFavorites -> {
                binding.txtScreenTitle.text = "Favoritas"
                binding.searchContainer.visibility = View.GONE
                favoritesViewModel.favorites.observe(viewLifecycleOwner) { stations ->
                    adapter.submitList(stations)
                    showCount(stations.size)
                }
            }
            isFiltered -> {
                binding.txtScreenTitle.text = "Resultados filtrados"
                binding.searchContainer.visibility = View.GONE
                stationViewModel.filterStations(
                    requireArguments().getString(ARG_CONNECTOR).orEmpty(),
                    requireArguments().getString(ARG_MUNICIPALITY).orEmpty(),
                    requireArguments().getString(ARG_USAGE).orEmpty(),
                    requireArguments().getString(ARG_CURRENT).orEmpty()
                )
                stationViewModel.filteredStations.observe(viewLifecycleOwner) { stations ->
                    adapter.submitList(stations)
                    showCount(stations.size)
                }
            }
            else -> {
                binding.txtScreenTitle.text = "Estaciones de carga"
                stationViewModel.searchResults.observe(viewLifecycleOwner) { stations ->
                    adapter.submitList(stations)
                    showCount(stations.size)
                }
            }
        }
    }

    private fun showCount(count: Int) {
        binding.txtResultCount.text = "Resultados: $count"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_FAVORITES = "favorites"
        private const val ARG_FILTERED = "filtered"
        private const val ARG_CONNECTOR = "connector"
        private const val ARG_MUNICIPALITY = "municipality"
        private const val ARG_USAGE = "usage"
        private const val ARG_CURRENT = "current"

        fun newInstance(onlyFavorites: Boolean): StationListFragment {
            return StationListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_FAVORITES, onlyFavorites)
                }
            }
        }

        fun newFilterInstance(
            connector: String,
            municipality: String,
            usageType: String,
            currentType: String
        ): StationListFragment {
            return StationListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_FILTERED, true)
                    putString(ARG_CONNECTOR, connector)
                    putString(ARG_MUNICIPALITY, municipality)
                    putString(ARG_USAGE, usageType)
                    putString(ARG_CURRENT, currentType)
                }
            }
        }
    }
}
