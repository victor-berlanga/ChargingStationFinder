package victor.berlanga.chargingstationfinder.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import victor.berlanga.chargingstationfinder.data.local.entity.ReviewEntity
import victor.berlanga.chargingstationfinder.databinding.ItemReviewBinding

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private val reviews = mutableListOf<ReviewEntity>()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun submitList(newReviews: List<ReviewEntity>) {
        reviews.clear()
        reviews.addAll(newReviews)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int = reviews.size

    inner class ReviewViewHolder(
        private val binding: ItemReviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(review: ReviewEntity) {
            binding.txtRating.text = "Calificación: ${review.rating}/5"
            binding.txtComment.text = review.comment
            binding.txtDate.text = dateFormat.format(Date(review.date))
        }
    }
}
