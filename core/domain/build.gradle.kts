plugins {
    alias(libs.plugins.android.template.jvm.library)
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.kotlinx.coroutines.core)
}