import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import util.configureKotlinJvm
import util.libs

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply("androidtemplate.android.lint")
            }
            configureKotlinJvm()
            dependencies {
                add("testImplementation", libs.findLibrary("kotlin.test").get())
            }
        }
    }
}
