package com.example.parentalguard

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

/**
 * Ekranda ön plana gelen her pencereyi dinler. Eğer açılan uygulamanın paket adı
 * engellenenler listesindeyse, kullanıcıyı anında ana ekrana (launcher) yönlendirir.
 */
class BlockerAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val packageName = event?.packageName?.toString() ?: return

        // Kendi uygulamamızı ve sistem launcher'ını hiç kontrol etmeye gerek yok
        if (packageName == applicationContext.packageName) return

        val blocked = PrefsHelper.getBlockedPackages(applicationContext)
        if (packageName in blocked) {
            goHome()
        }
    }

    private fun goHome() {
        val homeIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(homeIntent)
    }

    override fun onInterrupt() {
        // Gerekli değil, boş bırakılabilir
    }
}
