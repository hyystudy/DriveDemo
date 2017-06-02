/**
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jccy.drivedemo;

import android.os.Bundle;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.metadata.CustomPropertyKey;

/**
 * An activity to illustrate how to delete a custom property from a file.
 */
public class DeleteCustomPropertyActivity extends BaseDemoActivity {

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
        Drive.DriveApi.fetchDriveId(getGoogleApiClient(), EXISTING_FILE_ID)
                .setResultCallback(idCallback);
    }

    final ResultCallback<DriveApi.DriveIdResult> idCallback =
            new ResultCallback<DriveApi.DriveIdResult>() {
                @Override
                public void onResult(DriveApi.DriveIdResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Cannot find DriveId. Are you authorized to view this file?");
                        return;
                    }
                    DriveId driveId = result.getDriveId();
                    DriveFile file = driveId.asDriveFile();
                    CustomPropertyKey approvalPropertyKey = new CustomPropertyKey("approved",
                            CustomPropertyKey.PUBLIC);
                    CustomPropertyKey submitPropertyKey = new CustomPropertyKey("submitted",
                            CustomPropertyKey.PUBLIC);
                    MetadataChangeSet.Builder changeSetBuilder = new MetadataChangeSet.Builder();
                    changeSetBuilder.deleteCustomProperty(approvalPropertyKey);
                    changeSetBuilder.deleteCustomProperty(submitPropertyKey);
                    file.updateMetadata(getGoogleApiClient(), changeSetBuilder.build())
                            .setResultCallback(metadataCallback);
                }
            };

    final ResultCallback<DriveResource.MetadataResult> metadataCallback = new ResultCallback<DriveResource.MetadataResult>() {
        @Override
        public void onResult(DriveResource.MetadataResult result) {
            if (!result.getStatus().isSuccess()) {
                showMessage("Problem while trying to delete custom properties.");
                return;
            }
            showMessage("Custom properties successfully deleted.");
        }
    };
}
