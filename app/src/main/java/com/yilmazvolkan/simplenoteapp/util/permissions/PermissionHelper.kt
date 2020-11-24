package com.yilmazvolkan.simplenoteapp.util.permissions

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

class PermissionHelper(private val context: Context) {

    private val sharedPreferences by lazy {
        context.getSharedPreferences(PERMISSIONS_STATUS, Context.MODE_PRIVATE)
    }

    private val editor by lazy {
        sharedPreferences.edit()
    }

    private val permissionResultMap = HashMap<Int, PermissionResult>()

    @SuppressLint("NewApi")
    fun handlePermissions(
        activity: Activity,
        permissions: Array<String>,
        requestCode: Int,
        grantedAction: (() -> Unit),
        neverAskAction: (() -> Unit),
        permissionResult: PermissionResult
    ) {
        when {
            permissions.isPermissionGranted(context) -> {
                grantedAction()
            }
            neverAskAgainSelected(activity, permissions[0]) -> {
                neverAskAction()
            }
            else -> {
                permissionResultMap[requestCode] = permissionResult
                activity.requestPermissions(permissions, requestCode)
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun neverAskAgainSelected(activity: Activity, permission: String): Boolean {
        return getDisplayStatus(permission) !=
                activity.shouldShowRequestPermissionRationale(permission)
    }

    fun setAskBefore(permission: String) {
        editor.putBoolean(permission, true)
        editor.apply()
    }

    private fun getDisplayStatus(permission: String): Boolean {
        return sharedPreferences.getBoolean(permission, false)
    }

    companion object {
        private const val PERMISSIONS_STATUS = "PERMISSIONS_STATUS"

        val internetPermissions = arrayOf(Manifest.permission.INTERNET)
    }
}