package no.helge.android.featuretwo.navigation


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import no.helge.android.featuretwo.ui.FeatureTwoScreen

fun NavController.navigateToFeatureTwo(name: String, age: Int) = navigate(route = "feature-two/${name}?age=${age}")

fun NavGraphBuilder.featureTwoScreen(onNavigate: () -> Unit) {
    composable("feature-two/{name}?age={age}") { backStackEntry ->
        val name = backStackEntry.arguments?.getString("name")!!
        val age = backStackEntry.arguments?.getString("age")!!
        FeatureTwoScreen(name, age, onNavigate)
    }
}
