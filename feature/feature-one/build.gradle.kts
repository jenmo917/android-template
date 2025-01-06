plugins {
    alias(libs.plugins.android.template.feature)
    alias(libs.plugins.android.template.android.library.compose)
    alias(libs.plugins.android.template.android.library.jacoco)
}

android {
    namespace = "no.helge.android.feature.featureone"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.domain)
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.core.designsystem)
}

