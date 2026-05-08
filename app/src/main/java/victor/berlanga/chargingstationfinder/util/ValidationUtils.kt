package victor.berlanga.chargingstationfinder.util

object ValidationUtils {

    fun isValidUser(name: String, email: String): Boolean {
        return name.trim().isNotEmpty() && email.trim().isNotEmpty()
    }

    fun isValidReview(comment: String, rating: Int): Boolean {
        return comment.trim().isNotEmpty() && rating in 1..5
    }
}
