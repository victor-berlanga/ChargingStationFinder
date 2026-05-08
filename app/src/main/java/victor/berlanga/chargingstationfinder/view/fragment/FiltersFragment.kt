package victor.berlanga.chargingstationfinder.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import victor.berlanga.chargingstationfinder.MainActivity
import victor.berlanga.chargingstationfinder.databinding.FragmentFiltersBinding

class FiltersFragment : Fragment() {

    private var _binding: FragmentFiltersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnApplyFilters.setOnClickListener {
            (activity as? MainActivity)?.showFilteredStations(
                binding.edtConnector.text.toString(),
                binding.edtMunicipality.text.toString(),
                selectedUsageType(),
                selectedCurrentType()
            )
        }

        binding.btnClearFilters.setOnClickListener {
            binding.edtConnector.setText("")
            binding.edtMunicipality.setText("")
            binding.radioUsage.clearCheck()
            binding.radioCurrent.clearCheck()
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
