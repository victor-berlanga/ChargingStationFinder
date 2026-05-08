package victor.berlanga.chargingstationfinder

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import victor.berlanga.chargingstationfinder.databinding.ActivityMainBinding
import victor.berlanga.chargingstationfinder.view.fragment.MapFragment
import victor.berlanga.chargingstationfinder.view.fragment.ReviewsFragment
import victor.berlanga.chargingstationfinder.view.fragment.StationDetailFragment
import victor.berlanga.chargingstationfinder.view.fragment.StationListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnList.setOnClickListener {
            showStationList(false)
        }
        binding.btnMap.setOnClickListener {
            openFragment(MapFragment())
        }
        binding.btnFavorites.setOnClickListener {
            showStationList(true)
        }

        if (savedInstanceState == null) {
            showStationList(false)
        }
    }

    fun showStationList(onlyFavorites: Boolean) {
        openFragment(StationListFragment.newInstance(onlyFavorites))
    }

    fun showStationDetail(stationId: Int) {
        openFragment(StationDetailFragment.newInstance(stationId), addToBackStack = true)
    }

    fun showReviews(stationId: Int) {
        openFragment(ReviewsFragment.newInstance(stationId), addToBackStack = true)
    }

    private fun openFragment(
        fragment: androidx.fragment.app.Fragment,
        addToBackStack: Boolean = false
    ) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)

        if (addToBackStack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }
}
