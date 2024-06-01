package com.example.satellitefinder.ui.fragments

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    //permissions
    var actionOnPermissionHandlePermission: ((granted: Boolean) -> Unit)? = null
    var isAskingPermissionsHandlePermission = false

    var actionOnPermission: ((granted: Boolean) -> Unit)? = null
    var isAskingPermissions = false
    private val GENERIC_PERM_HANDLER = 100
    val TAG = "TESTINGFrag"


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        isAskingPermissionsHandlePermission = false
        if (requestCode == GENERIC_PERM_HANDLER && grantResults.isNotEmpty()) {
            actionOnPermissionHandlePermission?.invoke(grantResults[0] == 0)
        }
    }

}