/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mapdemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Utility class for access to runtime permissions.
 */
public abstract class PermissionUtils {

    /**
     * 申请权限，申请失败则关闭activity
     *
     * @param activity
     * @param requestId
     * @param permission
     * @param finishActivity
     * @param permissonRequestMessage
     * @param permissonRequestToast
     */
    public static void requestPermission(AppCompatActivity activity, int requestId,
                                         String permission, boolean finishActivity,
                                         String permissonRequestMessage, String permissonRequestToast) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            // Display a dialog with rationale.
            PermissionUtils.RationaleDialog.newInstance(permission, requestId, finishActivity, permissonRequestMessage, permissonRequestToast)
                    .show(activity.getSupportFragmentManager(), "dialog");
        } else {
            // Location permission has not been granted yet, request it.
            ActivityCompat.requestPermissions(activity, new String[]{permission}, requestId);

        }
    }

    /**
     * 权限是否已经获取
     *
     * @param grantPermissions
     * @param grantResults
     * @param permission       需要检测的权限
     * @return
     */
    public static boolean isPermissionGranted(String[] grantPermissions, int[] grantResults,
                                              String permission) {
        for (int i = 0; i < grantPermissions.length; i++) {
            if (permission.equals(grantPermissions[i])) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }

    public static class RationaleDialog extends DialogFragment {
        private static final String PERMISSON = "permisson";
        private static final String ARGUMENT_PERMISSION_REQUEST_CODE = "requestCode";
        private static final String ARGUMENT_FINISH_ACTIVITY = "finish";
        private static final String PERMISSONREQUESTMESSAGE = "permissonRequestMessage";
        private static final String PERMISSIONREQUESTTOAST = "permissonRequestToast";
        private boolean mFinishActivity = false;
        private boolean showToast = true;
        private String permissonRequestMessage;
        private String permissonRequestToast;
        private int requestCode;
        private String permisson;

        /**
         * @param requestCode
         * @param finishActivity
         * @param permissonRequestMessage
         * @param permissonRequestToast
         * @return
         */
        public static RationaleDialog newInstance(String permisson, int requestCode, boolean finishActivity, String permissonRequestMessage, String permissonRequestToast) {
            Bundle arguments = new Bundle();
            arguments.putString(PERMISSON, permisson);
            arguments.putInt(ARGUMENT_PERMISSION_REQUEST_CODE, requestCode);
            arguments.putBoolean(ARGUMENT_FINISH_ACTIVITY, finishActivity);
            arguments.putString(PERMISSONREQUESTMESSAGE, permissonRequestMessage);
            arguments.putString(PERMISSIONREQUESTTOAST, permissonRequestToast);
            RationaleDialog dialog = new RationaleDialog();
            dialog.setArguments(arguments);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            permisson = arguments.getString(PERMISSON);
            requestCode = arguments.getInt(ARGUMENT_PERMISSION_REQUEST_CODE);
            mFinishActivity = arguments.getBoolean(ARGUMENT_FINISH_ACTIVITY);
            permissonRequestMessage = arguments.getString(PERMISSONREQUESTMESSAGE);
            permissonRequestToast = arguments.getString(PERMISSIONREQUESTTOAST);

            return new AlertDialog.Builder(getActivity())
                    .setMessage(permissonRequestMessage)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // After click on Ok, request the permission.
                            ActivityCompat.requestPermissions(getActivity(), new String[]{permisson}, requestCode);
                            // Do not finish the Activity while requesting permission.
                            mFinishActivity = false;
                            showToast = false;
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .create();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
            if (showToast) {
                Toast.makeText(getActivity(), permissonRequestToast, Toast.LENGTH_SHORT).show();
            }
            if (mFinishActivity) {
                getActivity().finish();
            }
        }
    }
}