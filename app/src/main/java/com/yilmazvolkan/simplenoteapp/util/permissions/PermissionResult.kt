package com.yilmazvolkan.simplenoteapp.util.permissions

data class PermissionResult(
    val newlyGrantedAction: (() -> Unit),
    val deniedAction: (() -> Unit)
)