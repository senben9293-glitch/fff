package com.example.parentalguard

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent

/**
 * Bu receiver "Cihaz Yöneticisi" (Device Admin) olarak aktif olduğu sürece,
 * Android işletim sistemi uygulamanın normal yoldan (Ayarlar > Uygulamalar > Kaldır,
 * veya Play Store üzerinden) silinmesine izin vermez. Önce bu admin yetkisinin
 * Ayarlar > Güvenlik > Cihaz Yöneticileri ekranından kaldırılması gerekir.
 *
 * onDisableRequested(), kullanıcı tam o ekrandan admin yetkisini kaldırmaya
 * çalıştığında tetiklenir ve kullanıcıya gösterilecek uyarı mesajını döndürür.
 * Bu, silme işlemini tek başına engellemez (sistem diyaloğu olduğu için uygulama
 * onu iptal edemez) ama en azından kullanıcıyı bilgilendirir.
 */
class MyDeviceAdminReceiver : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
    }

    override fun onDisableRequested(context: Context, intent: Intent): CharSequence {
        return "Bu korumayı kapatmak uygulamanın kaldırılmasına izin verir. " +
            "Devam etmeden önce ebeveyn ile görüş."
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        // Admin yetkisi kaldırıldıktan sonra uygulama artık normal yoldan silinebilir.
    }
}
