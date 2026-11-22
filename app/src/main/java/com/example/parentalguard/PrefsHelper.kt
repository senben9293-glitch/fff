package com.example.parentalguard

import android.content.Context
import android.content.SharedPreferences

/**
 * Engellenen uygulama paket adlarının listesini saklayan basit yardımcı sınıf.
 */
object PrefsHelper {

    private const val PREFS_NAME = "parental_guard_prefs"
    private const val KEY_BLOCKED_PACKAGES = "blocked_packages"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getBlockedPackages(context: Context): Set<String> =
        prefs(context).getStringSet(KEY_BLOCKED_PACKAGES, emptySet()) ?: emptySet()

    fun setBlockedPackages(context: Context, packages: Set<String>) {
        prefs(context).edit().putStringSet(KEY_BLOCKED_PACKAGES, packages).apply()
    }

    fun addBlockedPackage(context: Context, packageName: String) {
        val current = getBlockedPackages(context).toMutableSet()
        current.add(packageName)
        setBlockedPackages(context, current)
    }

    fun removeBlockedPackage(context: Context, packageName: String) {
        val current = getBlockedPackages(context).toMutableSet()
        current.remove(packageName)
        setBlockedPackages(context, current)
    }
}
