package com.example.parentalguard

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.provider.Settings
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var adminComponent: ComponentName
    private lateinit var apps: List<ApplicationInfo>
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adminComponent = ComponentName(this, MyDeviceAdminReceiver::class.java)

        findViewById<Button>(R.id.btnEnableAccessibility).setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        findViewById<Button>(R.id.btnEnableDeviceAdmin).setOnClickListener {
            val dpm = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
            if (!dpm.isAdminActive(adminComponent)) {
                val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                    putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent)
                    putExtra(
                        DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "Uygulamanın izinsiz kaldırılmasını engellemek için bu izin gerekli."
                    )
                }
                startActivity(intent)
            }
        }

        listView = findViewById(R.id.listApps)
        loadAppList()
    }

    override fun onResume() {
        super.onResume()
        // Ekrana her dönüşte işaretleri güncel tut
        if (::listView.isInitialized && listView.adapter != null) {
            refreshCheckedStates()
        }
    }

    private fun loadAppList() {
        val pm = packageManager
        // Sistem uygulamalarını gizleyip sadece kullanıcı tarafından yüklenmiş
        // ya da güncellenmiş uygulamaları gösteriyoruz; istersen bu filtreyi kaldırabilirsin.
        apps = pm.getInstalledApplications(0)
            .filter { info ->
                (info.flags and ApplicationInfo.FLAG_SYSTEM) == 0 ||
                    (info.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
            }
            .sortedBy { pm.getApplicationLabel(it).toString().lowercase() }

        val labels = apps.map { pm.getApplicationLabel(it).toString() }
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            labels
        )
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        refreshCheckedStates()

        listView.setOnItemClickListener { _, _, position, _ ->
            val packageName = apps[position].packageName
            if (listView.isItemChecked(position)) {
                PrefsHelper.addBlockedPackage(this, packageName)
            } else {
                PrefsHelper.removeBlockedPackage(this, packageName)
            }
        }
    }

    private fun refreshCheckedStates() {
        val blocked = PrefsHelper.getBlockedPackages(this)
        apps.forEachIndexed { index, info ->
            listView.setItemChecked(index, info.packageName in blocked)
        }
    }
}
