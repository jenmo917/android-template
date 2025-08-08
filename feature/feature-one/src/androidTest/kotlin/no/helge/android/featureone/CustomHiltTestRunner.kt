package no.helge.android.featureone

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * Custom test runner that extends AndroidJUnitRunner and uses HiltTestApplication.
 * This serves as a workaround when HiltTestRunner from dagger.hilt.android.testing is not found.
 */
class CustomHiltTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}