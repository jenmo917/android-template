plugins {
    alias(libs.plugins.android.template.android.library)
    alias(libs.plugins.android.template.android.library.jacoco)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "no.helge.core.data"
}

dependencies {
    lintPublish(projects.lint)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.retrofit.core)
}
