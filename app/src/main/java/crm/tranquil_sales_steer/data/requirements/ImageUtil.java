package crm.tranquil_sales_steer.data.requirements;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;

public class ImageUtil {

    public static String getRealPathFromURI(Uri uri, Context context) {
        String path = null;

        if (uri != null) {
            String[] projection = {MediaStore.Images.Media.DATA};

            try (Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    path = cursor.getString(dataIndex);
                }
            }
        }

        return path;
    }

    public static Uri getImageUri(Context context, Bitmap image) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

        long tsLong = System.currentTimeMillis() / 1000;
        String ts = String.valueOf(tsLong);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, ts);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            if (uri != null) {
                contentResolver.openOutputStream(uri).write(bytes.toByteArray());
                return uri;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
