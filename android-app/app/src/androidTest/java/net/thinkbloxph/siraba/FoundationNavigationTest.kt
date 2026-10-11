package net.thinkbloxph.siraba

import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import net.thinkbloxph.siraba.ui.testing.UiTestTags

class FoundationNavigationTest {
    @get:Rule val rule = createAndroidComposeRule<MainActivity>()

    private fun startApp() {
        rule.onNodeWithTag(UiTestTags.INTRO_SCREEN).assertExists()
        rule.onNodeWithTag(UiTestTags.INTRO_START).performClick()
        rule.onNodeWithTag(UiTestTags.WELCOME_SCREEN).assertExists()
    }

    @Test fun introAlwaysShowsBeforeWelcome() {
        startApp()
    }

    @Test fun customerEntryNavigates() {
        startApp()
        rule.onNodeWithTag(UiTestTags.CUSTOMER_ENTRY).performClick()
        rule.onNodeWithTag(UiTestTags.CUSTOMER_SCREEN).assertExists()
        rule.onNodeWithTag(UiTestTags.BACK_TO_WELCOME).performClick()
        rule.onNodeWithTag(UiTestTags.WELCOME_SCREEN).assertExists()
    }

    @Test fun workerEntryNavigates() {
        startApp()
        rule.onNodeWithTag(UiTestTags.WORKER_ENTRY).performClick()
        rule.onNodeWithTag(UiTestTags.WORKER_SCREEN).assertExists()
        rule.onNodeWithTag(UiTestTags.BACK_TO_WELCOME).performClick()
        rule.onNodeWithTag(UiTestTags.WELCOME_SCREEN).assertExists()
    }
}
