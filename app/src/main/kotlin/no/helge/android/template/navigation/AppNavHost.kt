package no.helge.android.template.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import no.helge.android.featureone.navigation.FeatureOneRoute
import no.helge.android.featureone.navigation.featureOneScreen
import no.helge.android.featureone.navigation.navigateToFeatureOne
import no.helge.android.featuretwo.navigation.featureTwoScreen
import no.helge.android.featuretwo.navigation.navigateToFeatureTwo

@Composable
fun AppNavHost(modifier: Modifier = Modifier, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = FeatureOneRoute,
        modifier = modifier,
    ) {
        featureOneScreen(
            onNavigate = { navController.navigateToFeatureTwo("Bob", 50) }
        )
        featureTwoScreen(
            onNavigate = { navController.navigateToFeatureOne() }
        )
    }
}