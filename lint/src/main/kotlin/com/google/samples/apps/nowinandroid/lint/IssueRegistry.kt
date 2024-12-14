package com.google.samples.apps.nowinandroid.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.google.samples.apps.nowinandroid.lint.designsystem.DesignSystemDetector

class IssueRegistry : IssueRegistry() {

    override val issues = listOf(
        DesignSystemDetector.ISSUE,
        TestMethodNameDetector.FORMAT,
        TestMethodNameDetector.PREFIX,
    )

    override val api: Int = CURRENT_API

    override val minApi: Int = 12

    override val vendor: Vendor = Vendor(
        vendorName = "Now in Android",
        feedbackUrl = "https://github.com/android/nowinandroid/issues",
        contact = "https://github.com/android/nowinandroid",
    )
}
