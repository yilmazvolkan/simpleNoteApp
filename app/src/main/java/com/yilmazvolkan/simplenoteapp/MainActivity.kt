package com.yilmazvolkan.simplenoteapp

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.yilmazvolkan.simplenoteapp.databinding.ActivityMainBinding
import com.yilmazvolkan.simplenoteapp.fragments.NoteListFragment
import com.yilmazvolkan.simplenoteapp.util.permissions.PermissionHelper
import com.yilmazvolkan.simplenoteapp.util.permissions.PermissionHelper.Companion.internetPermissions
import com.yilmazvolkan.simplenoteapp.util.permissions.PermissionResult

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var noteFragment: NoteListFragment? = null
    private val permissionsHelper by lazy { PermissionHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        if (savedInstanceState == null) {
            startFragment()
        }

        noteFragment?.setOnBackButtonClicked {
            finishAffinity()
        }
    }

    private fun startFragment() {
        permissionsHelper.handlePermissions(
            this,
            internetPermissions,
            RC_ACCESS_NETWORK,
            {
                startFragmentHelper()
            },
            {
                showPermissionDeniedDialog()
            },
            PermissionResult(
                {
                    startFragmentHelper()
                },
                {
                    permissionsHelper.setAskBefore(internetPermissions[0])
                }
            )
        )
    }

    private fun startFragmentHelper() {
        noteFragment = NoteListFragment.newInstance()
        if (noteFragment != null && this.isFinishing.not()) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, noteFragment!!)
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.permission_denied_header))
            .setMessage(getString(R.string.permission_denied_text))
            .setPositiveButton(
                getString(R.string.permission_denied_app_settings)
            ) { _, _ ->
                // send to app settings if permission is denied permanently
                openSettings()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun openSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onDestroy() {
        noteFragment?.clearData()
        super.onDestroy()
    }

    companion object {
        private const val RC_ACCESS_NETWORK = 187
    }
}