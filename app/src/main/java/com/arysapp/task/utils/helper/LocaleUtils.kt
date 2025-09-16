package com.arysapp.task.utils.helper

    import android.content.Context
    import android.content.ContextWrapper
    import android.os.Build
    import android.os.LocaleList
    import android.app.LocaleManager
    import java.util.Locale

object LocaleUtils {

    fun setLocale(context: Context, language: String): ContextWrapper {
        var ctx = context
        val locale = Locale(language)
        Locale.setDefault(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33+
            val localeList = LocaleList(locale)
            val localeManager = ctx.getSystemService(LocaleManager::class.java)
            localeManager?.setApplicationLocales(localeList)
            ctx = ctx.createConfigurationContext(ctx.resources.configuration)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // API 24-32
            val config = ctx.resources.configuration
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            config.setLocales(localeList)
            ctx = ctx.createConfigurationContext(config)
        } else { // API < 24
            val config = ctx.resources.configuration
            @Suppress("DEPRECATION")
            config.locale = locale
            @Suppress("DEPRECATION")
            ctx.resources.updateConfiguration(config, ctx.resources.displayMetrics)
        }
        return ContextWrapper(ctx)
    }
}



