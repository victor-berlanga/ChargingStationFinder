package victor.berlanga.chargingstationfinder

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChargingStationUiTest {

    @Before
    fun clearRegistrationPreference() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.getSharedPreferences("user_session", 0)
            .edit()
            .clear()
            .commit()
    }

    @Test
    fun registerActivityDisplaysNameAndEmailInputs() {
        ActivityScenario.launch(RegisterActivity::class.java).use {
            onView(withId(R.id.edtName)).check(matches(isDisplayed()))
            onView(withId(R.id.edtEmail)).check(matches(isDisplayed()))
            onView(withId(R.id.btnContinue)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun registerActivityShowsValidationErrorWhenFieldsAreEmpty() {
        ActivityScenario.launch(RegisterActivity::class.java).use {
            onView(withId(R.id.btnContinue)).perform(click())
            onView(withId(R.id.txtRegisterMessage))
                .check(matches(withText("Nombre y correo son obligatorios")))
        }
    }

    @Test
    fun validRegistrationNavigatesToMainActivity() {
        ActivityScenario.launch(RegisterActivity::class.java).use {
            onView(withId(R.id.edtName)).perform(typeText("Victor"))
            onView(withId(R.id.edtEmail)).perform(typeText("victor@mail.com"), closeSoftKeyboard())
            onView(withId(R.id.btnContinue)).perform(click())

            waitSmallMoment()

            onView(withId(R.id.btnList)).check(matches(isDisplayed()))
            onView(withId(R.id.btnMap)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun mainActivityShowsNavigationButtons() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.btnList)).check(matches(isDisplayed()))
            onView(withId(R.id.btnMap)).check(matches(isDisplayed()))
            onView(withId(R.id.btnFavorites)).check(matches(isDisplayed()))
            onView(withId(R.id.btnFilters)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun stationListScreenDisplaysRecyclerView() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.recyclerStations)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun detailScreenDisplaysFavoriteAndReviewButtons() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                activity.showStationDetail(1)
            }

            waitSmallMoment()

            onView(withId(R.id.btnFavorite)).check(matches(isDisplayed()))
            onView(withId(R.id.btnReviews)).check(matches(isDisplayed()))
        }
    }

    private fun waitSmallMoment() {
        Thread.sleep(500)
    }
}
