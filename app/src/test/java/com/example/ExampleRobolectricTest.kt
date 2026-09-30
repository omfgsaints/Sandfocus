package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.ReelTheme
import com.example.ui.viewmodel.FocusUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read app name from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("SandFocus", appName)
    }

    @Test
    fun `verify sand patterns availability`() {
        val themes = ReelTheme.ALL_THEMES
        assertTrue(themes.isNotEmpty())
        assertEquals(6, themes.size)

        val mandala = ReelTheme.getById("mandala_lotus")
        assertNotNull(mandala)
        assertEquals("Zen Lotus Bloom", mandala.title)
    }

    @Test
    fun `verify activity launches without crash`() {
        org.robolectric.Robolectric.buildActivity(MainActivity::class.java).setup()
    }
}
