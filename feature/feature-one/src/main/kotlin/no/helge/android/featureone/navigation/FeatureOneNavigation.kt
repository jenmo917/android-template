package no.helge.android.featureone.navigation


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import no.helge.android.featureone.ui.FeatureOneScreen

@Serializable data object FeatureOneRoute

fun NavController.navigateToFeatureOne() = navigate(route = FeatureOneRoute)

fun NavGraphBuilder.featureOneScreen(onNavigate: () -> Unit) {
    composable<FeatureOneRoute> {
        FeatureOneScreen(onNavigate = onNavigate)
    }
}
