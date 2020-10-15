package com.nobias.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.nobias.BuildConfig;
import com.nobias.MyApplication;
import com.nobias.R;
import com.nobias.businesslogic.network.RetroFitInterface;
import com.nobias.businesslogic.preferences.AppSharedPreferences;
import com.nobias.utils.UtilsDocument;
import com.nobias.utils.UtilsImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_CALENDAR;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.nobias.utils.Logger.e;
import static com.nobias.utils.Utils.hideKeyboard;
import static com.nobias.utils.UtilsImage.calculateFileSize;
import static com.nobias.utils.UtilsImage.createTempImageFile;
import static com.nobias.utils.UtilsImage.savePicReturnUri;
import static com.nobias.utils.constants.ConstantCodes.FILE_SEPERATOR;
import static com.nobias.utils.constants.ConstantCodes.REQUEST_CAMERA_RESULT;
import static com.nobias.utils.constants.ConstantCodes.REQUEST_PERMISSION_CALENDAR;
import static com.nobias.utils.constants.ConstantCodes.REQUEST_PERMISSION_CAMERA;
import static com.nobias.utils.constants.ConstantCodes.REQUEST_PERMISSION_CAMERAANDWRITE;
import static com.nobias.utils.constants.ConstantCodes.REQUEST_PERMISSION_RECORD_AUDIO;
import static com.nobias.utils.constants.ConstantCodes.REQUEST_PERMISSION_WRITESTORAGE;
import static com.nobias.utils.constants.ConstantCodes.REQUEST_PHOTOGALLERY_RESULT;
import static com.nobias.utils.constants.ConstantCodes.REQUEST_RESULT_DOCUMENT;

/**
 * Created by Avantika Gadhiya on Apr, 16 2020 5:30.
 */

public class ActivityBase extends AppCompatActivity {
    @Inject
    protected Context mContext;
    @Inject
    protected MyApplication mApplication;
    @Inject
    protected AppSharedPreferences mPreferences;
    @Inject
    public RetroFitInterface mApiHelper;
    @Inject
    public LocalBroadcastManager mBroadcastManager;

    //private CallbackManager mCallBackManager = null;

    private boolean mIsFromCamera = false;
    private boolean mIsFromPhotoGallery = false;
    private boolean mIsFromDocument = false;
    private boolean mIsDocumentForAdditional = false;
    private boolean mIsDocumentByCamera = false;
    protected boolean mIsForFrontCamera = false;
    private Uri mUriForPhotoResult = null;

    protected String fileSize = "";
    protected String fileSizeMin = "";
    protected String fileSizeMax = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getApplicationContext()).getAppComponent().inject(this);
        //LoginManager.getInstance().logInWithReadPermissions(ActivityBase.this, Arrays.asList("public_profile"));
        //setupFacebookLogin();
    }

    /**
     * Callback received when a permissions request has been completed
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CAMERAANDWRITE:
                onRequestPermissionsResultCameraWrite(grantResults);
                break;
            case REQUEST_PERMISSION_WRITESTORAGE:
                onRequestPermissionsResultWrite(grantResults);
                break;
            case REQUEST_PERMISSION_CAMERA:
                onRequestPermissionsResultCamera(grantResults);
                break;
            case REQUEST_PERMISSION_RECORD_AUDIO:
                onRequestPermissionsResultRecordAudio(grantResults);
                break;
            case REQUEST_PERMISSION_CALENDAR:
                onRequestPermissionsResultCalendar(grantResults);
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void onRequestPermissionsResultCalendar(int[] grantResults){
        if (grantResults != null && grantResults.length == 1 && grantResults[0] == PackageManager
                .PERMISSION_GRANTED) {
            Intent i = new Intent(mApplication.getResources().getString(R
                    .string.broadcastNobiasRefreshList));
            i.putExtra(mApplication.getResources().getString(R.string.bundleIsFromCalendarPermission),true);
            mApplication.getAppComponent().getLocalBroadcastManager().sendBroadcast(i);
        }
    }

    private void onRequestPermissionsResultRecordAudio(int[] grantResults){
        if (grantResults != null && grantResults.length >= 1 && grantResults[0] == PackageManager
                .PERMISSION_GRANTED) {
            Intent i = new Intent(mApplication.getResources().getString(R.string.broadcastCameraPermissionTwilio));
            i.putExtra("isFromPermission", true);
            mApplication.getAppComponent().getLocalBroadcastManager().sendBroadcast(i);
        }
    }

    private void onRequestPermissionsResultCameraWrite(int[] grantResults) {
        if (grantResults != null && grantResults.length == 2) {
            boolean writePermitted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean cameraPermitted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

            if (writePermitted && cameraPermitted) {
                openCamera();
            } else {
                if (writePermitted) {
                    showAlertPermissions(getResources().getString(R.string.permission_camera),
                            REQUEST_PERMISSION_CAMERA);
                } else if (cameraPermitted) {
                    showAlertPermissions(getResources().getString(
                            R.string.permission_externalstorage_camera),
                            REQUEST_PERMISSION_WRITESTORAGE);
                } else {
                    showAlertPermissions(
                            getResources().getString(R.string.permission_camera_external),
                            REQUEST_PERMISSION_CAMERAANDWRITE);
                }
            }
        } else {
            showAlertPermissions(
                    getResources().getString(R.string.permission_camera_external),
                    REQUEST_PERMISSION_CAMERAANDWRITE);
        }
    }

    private void onRequestPermissionsResultWrite(int[] grantResults) {
        if (grantResults != null && grantResults.length == 1 && grantResults[0] == PackageManager
                .PERMISSION_GRANTED) {
            if (mIsFromCamera) {
                mIsFromCamera = false;
                openCamera();
            } else if (mIsFromPhotoGallery) {
                mIsFromPhotoGallery = false;
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                        REQUEST_PHOTOGALLERY_RESULT);
            } else if (mIsFromDocument) {
                mIsFromDocument = false;
                fileChooserIntent();
            }
        } else {
            if (mIsFromCamera) {
                showAlertPermissions(
                        getResources().getString(R.string.permission_externalstorage_camera),
                        REQUEST_PERMISSION_WRITESTORAGE);
            } else if (mIsFromPhotoGallery) {
                showAlertPermissions(getResources().getString(
                        R.string.permission_externalstorage_photogallery),
                        REQUEST_PERMISSION_WRITESTORAGE);
            } else if (mIsFromDocument) {
                showAlertPermissions(getResources().getString(
                        R.string.permission_externalstorage_document),
                        REQUEST_PERMISSION_WRITESTORAGE);
            }
        }
    }

    private void onRequestPermissionsResultCamera(int[] grantResults) {
        if (grantResults != null && grantResults.length == 1 && grantResults[0] == PackageManager
                .PERMISSION_GRANTED) {
             openCamera();
        } else {
            showAlertPermissions(getResources().getString(R.string.permission_camera),
                    REQUEST_PERMISSION_CAMERA);
        }
    }


    private void broadcastError(int error, String errorString) {
        mBroadcastManager.sendBroadcast(new Intent(mContext.getResources().getString(R
                .string.broadcastFragBaseError))
                .putExtra(mContext.getResources().getString(R.string.bundleFragBaseError), error)
                .putExtra(mContext.getResources().getString(R.string.bundleFragBaseErrorString),
                        errorString));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_PHOTOGALLERY_RESULT ||
                requestCode == REQUEST_RESULT_DOCUMENT) && resultCode == Activity.RESULT_OK) {
            processResultGalleryDocument(requestCode, data);
        } else if (requestCode == REQUEST_CAMERA_RESULT && resultCode == Activity.RESULT_OK) {
            manipulateCameraCapture();
        }/*else {
            mCallBackManager.onActivityResult(requestCode, resultCode, data);
        }*/
    }

    /*private void setupFacebookLogin() {
        mCallBackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile currentAccessToken = Profile.getCurrentProfile();
                Log.e("name", currentAccessToken.getFirstName());
            }

            @Override
            public void onCancel() {
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }
*/
    private void processResultGalleryDocument(int requestCode, Intent data) {
        if (data != null) {
            if (requestCode == REQUEST_PHOTOGALLERY_RESULT) {
                new ActivityBase.AsyncImage().execute(data.getData());
            } else {
                manipulateDocumentSelected(data.getData());
            }
        } else {
            broadcastError(requestCode == REQUEST_PHOTOGALLERY_RESULT ? R.string
                    .message_unabletochooseimage : R.string.message_unabletochoosedocument, "");
        }
    }

    protected void imageChooser() {
        mIsForFrontCamera = false;
        hideKeyboard(ActivityBase.this);
        final CharSequence[] items = {getString(R.string.text_camera), getString(R.string
                .text_photogallery)};

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBase.this);
        builder.setTitle(getString(R.string.text_uploadphotousing));
        builder.setItems(items, (dialog, item) -> {
            if (item == 0) {
                manipulateCameraClick();
            } else {
                manipulatePhotoGalleryClick();
            }
            dialog.dismiss();
        });
        builder.show();
    }

    /**
     * Used to manipulate camera click on add image from alert
     */
    protected void manipulateCameraClick() {
        if (ActivityCompat.checkSelfPermission(ActivityBase.this,
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(ActivityBase.this,
                        CAMERA) != PackageManager.PERMISSION_GRANTED) {
            mIsFromPhotoGallery = false;
            mIsFromCamera = true;
            mIsFromDocument = false;
            requestCameraAndWritePermissions();
        } else if (ActivityCompat.checkSelfPermission(ActivityBase.this,
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            mIsFromPhotoGallery = false;
            mIsFromCamera = true;
            mIsFromDocument = false;
            requestWritePermissions(
                    getResources().getString(R.string.permission_externalstorage_camera));
        } else if (ActivityCompat.checkSelfPermission(ActivityBase.this,
                CAMERA) != PackageManager.PERMISSION_GRANTED) {
            mIsFromPhotoGallery = false;
            mIsFromCamera = true;
            mIsFromDocument = false;
            requestCameraPermissions();
        } else {
            openCamera();
        }
    }

    /**
     * Used to manipulate photo gallery click on add image from alert
     */
    private void manipulatePhotoGalleryClick() {
        if (ActivityCompat.checkSelfPermission(ActivityBase.this,
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            mIsFromPhotoGallery = true;
            mIsFromCamera = false;
            mIsFromDocument = false;
            requestWritePermissions(
                    getResources().getString(R.string.permission_externalstorage_photogallery));
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_PHOTOGALLERY_RESULT);
            }
        }
    }

    /**
     * Requests the Camera as well as Write External Storage permission.
     * <p>
     * If the permission has been denied previously, an alert dialog will prompt the user to grant
     * the permission, otherwise it is requested directly.
     */
    private void requestCameraAndWritePermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityBase.this,
                WRITE_EXTERNAL_STORAGE) &&
                ActivityCompat.shouldShowRequestPermissionRationale(ActivityBase.this,
                        CAMERA)) {
            showAlertPermissions(getResources().getString(R.string.permission_camera_external),
                    REQUEST_PERMISSION_CAMERAANDWRITE);
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA},
                    REQUEST_PERMISSION_CAMERAANDWRITE);
        }
    }

    /**
     * Requests the Camera as well as Write External Storage permission.
     * <p>
     * If the permission has been denied previously, an alert dialog will prompt the user to grant
     * the permission, otherwise it is requested directly.
     */
    public void requestCalendarPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityBase.this,
                WRITE_CALENDAR) &&
                ActivityCompat.shouldShowRequestPermissionRationale(ActivityBase.this,
                        READ_CALENDAR)) {
            showAlertPermissions(getResources().getString(R.string.permission_calendar),
                    REQUEST_PERMISSION_CALENDAR);
        } else {
            requestPermissions(new String[]{WRITE_CALENDAR, READ_CALENDAR},
                    REQUEST_PERMISSION_CALENDAR);
        }
    }

    /**
     * Requests the Write External Storage permission.
     * <p>
     * If the permission has been denied previously, an alert dialog will prompt the user to grant
     * the permission, otherwise it is requested directly.
     */
    private void requestWritePermissions(String alertMessage) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityBase.this,
                WRITE_EXTERNAL_STORAGE)) {
            showAlertPermissions(alertMessage, REQUEST_PERMISSION_WRITESTORAGE);
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITESTORAGE);
        }
    }

    /**
     * Requests the Camera permission.
     * <p>
     * If the permission has been denied previously, an alert dialog will prompt the user to grant
     * the permission, otherwise it is requested directly.
     */
    public void requestCameraPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityBase.this,
                CAMERA)) {
            showAlertPermissions(getResources().getString(R.string.permission_camera),
                    REQUEST_PERMISSION_CAMERA);
        } else {
            requestPermissions(new String[]{CAMERA},
                    REQUEST_PERMISSION_CAMERA);
        }
    }

    /**
     * Requests the Camera permission.
     * <p>
     * If the permission has been denied previously, an alert dialog will prompt the user to grant
     * the permission, otherwise it is requested directly.
     */
    public void requestCameraAndMicPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityBase.this, CAMERA) &&
        ActivityCompat.shouldShowRequestPermissionRationale(ActivityBase.this, RECORD_AUDIO)) {
            showAlertPermissions(getResources().getString(R.string.permission_camera_audio),
                    REQUEST_PERMISSION_RECORD_AUDIO);
        } else {
            requestPermissions(new String[]{CAMERA,RECORD_AUDIO},
                    REQUEST_PERMISSION_RECORD_AUDIO);
        }
    }

    /**
     * Shows alert for permissions
     *
     * @param alertMessage Alert message to be shown
     * @param requestType  Type of permission requested and for which request is to be made
     *                     depending on it
     */
    private void showAlertPermissions(String alertMessage, final int requestType) {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(ActivityBase.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getResources().getString(R.string.message_alert));
        builder.setMessage(alertMessage);
        builder.setCancelable(true);
        builder.setPositiveButton(getResources().getString(R.string.text_request),
                (dialog, which) -> {
                    dialog.dismiss();
                    switch (requestType) {
                        case REQUEST_PERMISSION_WRITESTORAGE:
                            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE},
                                    REQUEST_PERMISSION_WRITESTORAGE);
                            break;
                        case REQUEST_PERMISSION_CAMERAANDWRITE:
                            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,
                                    CAMERA}, REQUEST_PERMISSION_CAMERAANDWRITE);
                            break;
                        case REQUEST_PERMISSION_CALENDAR:
                            requestPermissions(new String[]{WRITE_CALENDAR}, REQUEST_PERMISSION_CALENDAR);
                            break;
                        case REQUEST_PERMISSION_RECORD_AUDIO:
                            requestPermissions(new String[]{CAMERA, RECORD_AUDIO}, REQUEST_PERMISSION_RECORD_AUDIO);
                            break;
                        default:
                            requestPermissions(new String[]{CAMERA}, REQUEST_PERMISSION_CAMERA);
                            break;
                    }
                });
        builder.setNegativeButton(getResources().getString(R.string.message_cancel),
                (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    /**
     * Used to open camera for capturing of image
     */
    private void openCamera() {
        int error = -1;
        if (UtilsImage.isCameraPresent(mContext.getPackageManager())) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
                File fileTemporary = null;
                try {
                    fileTemporary = createTempImageFile(mContext);
                } catch (IOException exception) {
                    e(getClass().getSimpleName() + " openCamera", exception);
                }
                if (fileTemporary != null) {
                    mUriForPhotoResult = FileProvider.getUriForFile(mContext,
                            BuildConfig.APPLICATION_ID + ".provider", fileTemporary);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUriForPhotoResult);
                    if (mIsForFrontCamera) {
                        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                        takePictureIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                        takePictureIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                        mIsForFrontCamera = false;
                    }
                    startActivityForResult(takePictureIntent, REQUEST_CAMERA_RESULT);
                } else {
                    error = R.string.message_camera_unabletocapture;
                }
            } else {
                error = R.string.message_camera_unabletoopen;
            }
        } else {
            error = R.string.message_camera_absent;
        }
        if (error != -1) {
            broadcastError(error, "");
        }
    }

    private class AsyncImage extends AsyncTask<Uri, Void, Uri> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBroadcastManager.sendBroadcast(new Intent(mContext.getResources().getString(R.string
                    .bundleChooserProgressIsToShow)).putExtra(mContext.getResources().getString(R
                    .string.bundleChooserProgressIsToShow), true));
        }

        @Override
        protected Uri doInBackground(Uri... uris) {
            try {
                return savePicReturnUri(MediaStore.Images.Media.getBitmap(mContext
                        .getContentResolver(), uris[0]), mContext);
            } catch (IOException exception) {
                e(getClass().getSimpleName() + " AsyncImage", exception);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Uri uri) {
            super.onPostExecute(uri);
            if (uri != null) {
                mUriForPhotoResult = uri;
                manipulateCameraCapture();
            } else {
                broadcastError(R.string.message_unabletochooseimage, "");
            }
            mBroadcastManager.sendBroadcast(new Intent(mContext.getResources().getString(R.string
                    .bundleChooserProgressIsToShow)).putExtra(mContext.getResources().getString(R
                    .string.bundleChooserProgressIsToShow), false));
        }
    }

    private void manipulateCameraCapture() {
        int error = -1;
        String errorString = "";
        if (mUriForPhotoResult != null) {
            String fileName = mUriForPhotoResult.toString().substring(mUriForPhotoResult.toString
                    ().lastIndexOf("/") + 1);
            File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) +
                    FILE_SEPERATOR + fileName);
            if (!file.exists() || file.length() <= 0) {
                if (mIsDocumentByCamera) {
                    broadcastError(R.string.message_unabletochoosedocument, errorString);
                } else {
                    broadcastError(R.string.message_unabletochooseimage, errorString);
                }
                mIsDocumentByCamera = false;
                return;
            }
            String fileSizeError = calculateFileSize(ActivityBase.this, file, fileSize, fileSizeMin,
                    fileSizeMax, false);
            if (TextUtils.isEmpty(fileSizeError)) {
                if (mIsDocumentByCamera) {
                    mBroadcastManager.sendBroadcast(new Intent(mContext.getResources().getString
                            (R.string.broadcastDocumentResult))
                            .putExtra(mContext.getResources().getString(R.string
                                    .bundleDocumentIsAdditional), mIsDocumentForAdditional)
                            .putExtra(mContext.getResources().getString(R.string
                                    .bundleDocumentFilePath), file.getAbsolutePath()));
                    if (mIsDocumentForAdditional) {
                        mIsDocumentForAdditional = false;
                    }
                } else {
                    Bitmap bitmap = UtilsImage.getScaledMatrixedBitmapFromFile(file, mContext
                            .getResources().getDimensionPixelSize(R.dimen.appbar_height));
                    if (bitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        mBroadcastManager.sendBroadcast(new Intent(mContext.getResources().getString
                                (R.string.broadcastImageResult))
                                .putExtra(mContext.getResources().getString(R.string
                                        .bundleImageResultStream), byteArray)
                                .putExtra(mContext.getResources().getString(R.string
                                        .bundleImageResultUri), mUriForPhotoResult.toString()));
                    } else {
                        error = R.string.message_unabletochooseimage;
                    }
                }
            } else {
                errorString = fileSizeError;
            }
        } else {
            error = R.string.message_unabletochooseimage;
        }
        if (error != -1 || !TextUtils.isEmpty(errorString)) {
            broadcastError(error, errorString);
        }
        mIsDocumentByCamera = false;
    }

    protected void fileChooser(boolean isAdditional) {
        hideKeyboard(ActivityBase.this);
        final CharSequence[] items = {getString(R.string.text_camera), getString(R.string
                .text_storage)};

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityBase.this);
        builder.setTitle(getString(R.string.text_uploaddocumentusing));
        builder.setItems(items, (dialog, item) -> {
            mIsDocumentForAdditional = isAdditional;
            if (item == 0) {
                mIsDocumentByCamera = true;
                manipulateCameraClick();
            } else {
                manipulateFileChooser();
            }
            dialog.dismiss();
        });
        builder.show();
    }

    private void manipulateFileChooser() {
        if (ActivityCompat.checkSelfPermission(ActivityBase.this,
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            mIsFromPhotoGallery = false;
            mIsFromCamera = false;
            mIsFromDocument = true;
            requestWritePermissions(
                    getResources().getString(R.string.permission_externalstorage_document));
        } else {
            fileChooserIntent();
        }
    }

    private void fileChooserIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, mContext.getResources().getString(R
                .string.text_uploaddocument)), REQUEST_RESULT_DOCUMENT);
    }

    private void manipulateDocumentSelected(Uri data) {
        int error = -1;
        String errorString = "";
        String filePath = "";
        try {
            filePath = UtilsDocument.getPath(ActivityBase.this, data);
        } catch (Exception exception) {
            e(getClass().getSimpleName() + " manipulateDocumentSelected", exception);
        }

        if (filePath != null) {
            File file = new File(filePath);
            if (!file.exists() || file.length() <= 0) {
                broadcastError(R.string.message_unabletochoosedocument, errorString);
                return;
            }
            String fileSizeError = calculateFileSize(ActivityBase.this, file, fileSize, fileSizeMin,
                    fileSizeMax, true);
            if (TextUtils.isEmpty(fileSizeError)) {
                mBroadcastManager.sendBroadcast(new Intent(mContext.getResources().getString
                        (R.string.broadcastDocumentResult))
                        .putExtra(mContext.getResources().getString(R.string
                                .bundleDocumentIsAdditional), mIsDocumentForAdditional)
                        .putExtra(mContext.getResources().getString(R.string
                                .bundleDocumentFilePath), filePath));
                if (mIsDocumentForAdditional) {
                    mIsDocumentForAdditional = false;
                }
            } else {
                errorString = fileSizeError;
            }
        } else {
            error = R.string.message_unabletochoosedocument;
        }
        if (error != -1 || !TextUtils.isEmpty(errorString)) {
            broadcastError(error, errorString);
        }
    }
}


