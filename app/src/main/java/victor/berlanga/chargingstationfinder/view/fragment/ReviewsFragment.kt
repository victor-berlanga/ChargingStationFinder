package victor.berlanga.chargingstationfinder.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import victor.berlanga.chargingstationfinder.databinding.FragmentReviewsBinding
import victor.berlanga.chargingstationfinder.view.adapter.ReviewAdapter
import victor.berlanga.chargingstationfinder.viewmodel.ReviewViewModel

class ReviewsFragment : Fragment() {

    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!
    private val reviewViewModel: ReviewViewModel by viewModels()
    private lateinit var adapter: ReviewAdapter
    private var stationId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        stationId = requireArguments().getInt(ARG_STATION_ID)
        adapter = ReviewAdapter()

        binding.recyclerReviews.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerReviews.adapter = adapter

        reviewViewModel.loadReviews(stationId)
        reviewViewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            adapter.submitList(reviews)
            binding.txtReviewCount.text = "Reseñas: ${reviews.size}"
            binding.txtEmptyReviews.visibility = if (reviews.isEmpty()) View.VISIBLE else View.GONE
        }

        reviewViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrBlank()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnAddReview.setOnClickListener {
            val rating = binding.edtRating.text.toString().toIntOrNull() ?: 0
            reviewViewModel.addReview(stationId, binding.edtComment.text.toString(), rating)
            if (binding.edtComment.text.toString().isNotBlank() && rating in 1..5) {
                binding.edtComment.setText("")
                binding.edtRating.setText("")
            }
        }

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_STATION_ID = "station_id"

        fun newInstance(stationId: Int): ReviewsFragment {
            return ReviewsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_STATION_ID, stationId)
                }
            }
        }
    }
}
