plugins {
    alias(libs.plugins.android.template.feature)
    alias(libs.plugins.android.template.android.library.compose)
    alias(libs.plugins.android.template.android.library.jacoco)
}

android {
    namespace = "no.helge.android.feature.featuretwo"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.domain)
    implementation(projects.core.designsystem)
}

