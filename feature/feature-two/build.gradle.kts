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
    implementation(projects.core.data)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.moshi)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.adapters)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)


}

