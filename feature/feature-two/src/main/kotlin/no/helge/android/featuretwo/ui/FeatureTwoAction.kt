package no.helge.android.featuretwo.ui

sealed class FeatureTwoAction {
    data object LoadData : FeatureTwoAction()
    data object Retry : FeatureTwoAction()
    data object NavigateBack : FeatureTwoAction()
}