package com.ashepping.fuelcost.ui

data class AppLang(val code: String, val label: String, val shortCode: String)

object AppLangs {
    val all = listOf(
        AppLang("ru", "Русский", "RU"),
        AppLang("en", "English", "EN"),
        AppLang("es", "Español", "ES"),
        AppLang("pt", "Português", "PT"),
        AppLang("de", "Deutsch", "DE"),
        AppLang("fr", "Français", "FR"),
        AppLang("tr", "Türkçe", "TR"),
        AppLang("ar", "العربية", "AR")
    )

    fun of(code: String) = all.firstOrNull { it.code == code } ?: all.first()
}
