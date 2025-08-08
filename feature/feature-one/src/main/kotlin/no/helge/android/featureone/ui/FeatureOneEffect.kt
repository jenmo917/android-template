package no.helge.android.featureone.ui

sealed class FeatureOneEffect {
    data object NavigateToFeatureTwo : FeatureOneEffect()
}