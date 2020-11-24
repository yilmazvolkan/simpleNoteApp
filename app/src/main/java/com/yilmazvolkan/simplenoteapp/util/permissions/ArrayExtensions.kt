package com.yilmazvolkan.simplenoteapp.util.permissions

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

fun Array<String>.isPermissionGranted(context: Context): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        return true
    }

    for (i in this.indices) {
        if (ContextCompat.checkSelfPermission(context, this[i])
            != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
    }
    return true
}