package com.nobias.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import androidx.exifinterface.media.ExifInterface;

import com.nobias.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.nobias.utils.Logger.e;

/**
 * Created by Brij Dholakia on Jul, 25 2018 17:05.
 * <p>
 * Utility for images
 */
public class UtilsImage {

    /**
     * Used to check presence of camera on device
     *
     * @param packageManager Package manager to check for camera
     * @return Is camera present or not
     */
    public static boolean isCameraPresent(PackageManager packageManager) {
        boolean isCameraPresent = false;
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
                packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
                packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            final int cameraCount = Camera.getNumberOfCameras();
            if (cameraCount > 0) {
                isCameraPresent = true;
            }
        }
        return isCameraPresent;
    }

    public static File createTempImageFile(Context mContext) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format
                (new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );
    }

    public static Bitmap getMatrixedBitmapFromFile(File file) {
        Matrix matrixForRotation = new Matrix();
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), new BitmapFactory
                .Options());

        if (bitmap == null) {
            return null;
        }
        matrixForRotation.postRotate(bitmapExifOrientation(file.getAbsolutePath()));
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrixForRotation, true);
    }

    public static Bitmap getScaledMatrixedBitmapFromFile(File file, int dimension) {
        Matrix matrixForRotation = new Matrix();
        Bitmap bitmapDecodedFile = getScaledDownBitmapForImageView(file.getAbsolutePath(),
                dimension);
        if (bitmapDecodedFile == null) {
            return null;
        }
        matrixForRotation.postRotate(bitmapExifOrientation(file.getAbsolutePath()));
        return Bitmap.createBitmap(bitmapDecodedFile, 0, 0,
                bitmapDecodedFile.getWidth(), bitmapDecodedFile.getHeight(),
                matrixForRotation, true);
    }

    private static Bitmap getScaledDownBitmapForImageView(String absolutePath, int dimension) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(absolutePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / dimension, photoH / dimension);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(absolutePath, bmOptions);
    }

    /**
     * Used to fetch the exif rotation for the image chosen from the gallery.
     * <p/>
     * Image captured from camera can be converted to different orientation while saving, hence to
     * get it vertical upright, the rotation angle is to be fetched.
     *
     * @param picturePath File picture whose rotation is to be checked
     * @return The rotation value for the image
     */
    private static int bitmapExifOrientation(String picturePath) {
        ExifInterface exifInterface;
        int rotation = 0;
        int orientation;
        try {
            exifInterface = new ExifInterface(picturePath);
            orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotation = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotation = 270;
                    break;
                default:
                    rotation = 0;
                    break;
            }
        } catch (IOException exception) {
            e("UtilsImage bitmapExifOrientation", exception);
        }
        return rotation;
    }

    /**
     * Used to calculate the file size from file path provided and provide that it is within size
     * limits if any
     *
     * @param activity    Activity in contention
     * @param file        File whose size is to be measured
     * @param fileSize    File size
     * @param fileSizeMin File size minimum
     * @param fileSizeMax File size maximum
     * @param isDoc       Is file size to be checked for document
     * @return Validity whether bitmap is of needed size
     */
    public static String calculateFileSize(Activity activity, File file, String fileSize,
                                           String fileSizeMin, String fileSizeMax, boolean isDoc) {
        String alphabetsPattern = "[^a-zA-Z]";
        String numericPattern = "[^0-9]";
        float fileSizeInBytes = file.length();
        float fileSizeInKB = fileSizeInBytes / 1024;
        float fileSizeInMB = fileSizeInKB / 1024;
        String calStringInKB = Float.toString(fileSizeInKB);
        double imageSizeInKB = Double.parseDouble(calStringInKB);
        String calStringInMB = Float.toString(fileSizeInMB);
        double imageSizeInMB = Double.parseDouble(calStringInMB);

        String fileTypeContent = isDoc ?
                activity.getString(R.string.error_filesize_type_document) :
                activity.getString(R.string.error_filesize_type_image);
        String sizePrefix = activity.getString(R.string.error_filesize_prefix) +
                " " + fileTypeContent + " ";

        if (!TextUtils.isEmpty(fileSizeMin)) {
            String isInvalid = getFileSizeValidation(sizePrefix,
                    fileSizeMin.replaceAll(alphabetsPattern, ""),
                    Double.parseDouble(fileSizeMin.replaceAll(numericPattern, "")),
                    imageSizeInKB, imageSizeInMB, fileSizeMin,
                    activity.getString(R.string.error_filesize_type_suffix_min),
                    activity.getString(R.string.error_filesize_suffix_min),
                    true);
            if (!TextUtils.isEmpty(isInvalid)) {
                return isInvalid;
            }
        }

        if (!TextUtils.isEmpty(fileSizeMax)) {
            String isInvalid = getFileSizeValidation(sizePrefix,
                    fileSizeMax.replaceAll(alphabetsPattern, ""),
                    Double.parseDouble(fileSizeMax.replaceAll(numericPattern, "")),
                    imageSizeInKB, imageSizeInMB, fileSizeMax,
                    activity.getString(R.string.error_filesize_type_suffix_max),
                    activity.getString(R.string.error_filesize_suffix_max),
                    false);
            if (!TextUtils.isEmpty(isInvalid)) {
                return isInvalid;
            }
        }

        if (!TextUtils.isEmpty(fileSize)) {
            String isInvalid = getFileSizeValidation(sizePrefix,
                    fileSize.replaceAll(alphabetsPattern, ""),
                    Double.parseDouble(fileSize.replaceAll(numericPattern, "")),
                    imageSizeInKB, imageSizeInMB, fileSize,
                    activity.getString(R.string.error_filesize_type_suffix_max),
                    activity.getString(R.string.error_filesize_suffix_max),
                    false);
            if (!TextUtils.isEmpty(isInvalid)) {
                return isInvalid;
            }
        }
        return "";
    }

    private static String getFileSizeValidation(String sizePrefix,
                                                String fileSizeFormat, double fileSizeValue,
                                                double imageSizeInKB, double imageSizeInMB,
                                                String size, String typeSuffix, String suffix,
                                                boolean isForMin) {
        boolean isSuccess;

        if (fileSizeFormat.equalsIgnoreCase("KB")) {
            isSuccess = isForMin ?
                    (imageSizeInKB < fileSizeValue) :
                    (imageSizeInKB > fileSizeValue);
        } else {
            isSuccess = isForMin ?
                    (imageSizeInMB < fileSizeValue) :
                    (imageSizeInMB > fileSizeValue);
        }
        if (!isSuccess) {
            return "";
        } else {
            return (sizePrefix + typeSuffix + " " + size + " " + suffix).trim();
        }
    }

    /**
     * Saves the pic bitmap for temporary usage for the size of the image
     *
     * @param pic Bitmap to be manipulated
     * @return Return the file stored
     */
    public static Uri savePicReturnUri(Bitmap pic, Context context) {
        Bitmap bitmap;
        OutputStream outputStream = null;
        bitmap = pic;

        try {
            File fileImage = createTempImageFile(context);
            outputStream = new FileOutputStream(fileImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            return Uri.parse(fileImage.getAbsolutePath());
        } catch (Exception exception) {
            e("UtilsImage savePicReturnUri", exception);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException exception) {
                e("UtilsImage savePicReturnUri", exception);
            }
        }
        return null;
    }

    public static File savepic(Bitmap pic, File file) {
        try (OutputStream output = new FileOutputStream(file)) {
            pic.compress(Bitmap.CompressFormat.JPEG, 100, output);
            return file;
        } catch (Exception exception) {
            e("UtilsImage savepic", exception);
        }
        return null;
    }

    public static String convertImageToBase64(byte[] byteArray) {
        //return Base64.encodeToString(byteArray, Base64.DEFAULT);
        return org.apache.commons.codec.binary.Base64.encodeBase64String(byteArray);
    }

    public static String convertBitmapToBase64(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] array = stream.toByteArray();
            return convertImageToBase64(array);
        } else
            return "";
    }
}
