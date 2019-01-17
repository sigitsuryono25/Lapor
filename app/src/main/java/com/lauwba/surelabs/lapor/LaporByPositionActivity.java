package com.lauwba.surelabs.lapor;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.lauwba.surelabs.lapor.library.FilePath;
import com.lauwba.surelabs.lapor.library.RequestHandler;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.lauwba.surelabs.lapor.library.FilePath.MEDIA_TYPE_IMAGE;

public class LaporByPositionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LaporByPositionActivity.class.getSimpleName().toString();
    private final int CAMERA_REQ = 8888;
    private final int GALLERY_REQ = 9999;
    private Uri fileUri, fromRealPath;
    private String folderName = "119";
    String realPath, filename, convertedImage;
    ByteArrayOutputStream byteArrayOutputStream;

    Button cameraPick, galleryPick, pratinjau, close, send;
    FrameLayout pratinjauContainer;
    EditText pelapor, posisi, jumlah, status, kondisi;
    ImageView pratinjauImage, getPosition;
    LocationManager locationManager;
    LocationListener locationListener;
    LinearLayout location;
    boolean flag = false;
    private Bitmap FixBitmap, bmp;
    private byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapor_by_position);

        setTitle("Halaman Kirim Laporan");

        getPosition = findViewById(R.id.getPosition);
        cameraPick = findViewById(R.id.ambilFoto);
        galleryPick = findViewById(R.id.gallery);
        pratinjau = findViewById(R.id.tampilkanPratinjau);
        close = findViewById(R.id.tutup);
        send = findViewById(R.id.btnKirim);
        pratinjauContainer = findViewById(R.id.pratinjauContainer);
        pelapor = findViewById(R.id.pelapor);
        posisi = findViewById(R.id.posisi);
        jumlah = findViewById(R.id.jumlahKorban);
        status = findViewById(R.id.statusKorban);
        kondisi = findViewById(R.id.kondisiKorban);
        pratinjauImage = findViewById(R.id.pratinjau);
        location = findViewById(R.id.location);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        getPosition.setOnClickListener(this);
        cameraPick.setOnClickListener(this);
        galleryPick.setOnClickListener(this);
        pratinjau.setOnClickListener(this);
        close.setOnClickListener(this);
        send.setOnClickListener(this);

        getLocation();
        byteArrayOutputStream = new ByteArrayOutputStream();
    }

    @Override
    public void onClick(View v) {
        int isShowing = pratinjauContainer.getVisibility();
        switch (v.getId()) {
            case R.id.getPosition:
                getLocation();
                break;
            case R.id.ambilFoto:
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(i, CAMERA_REQ);
                break;
            case R.id.gallery:
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(gallery, GALLERY_REQ);
                break;
            case R.id.tampilkanPratinjau:
                if (isShowing == View.VISIBLE) {
                    pratinjauContainer.setVisibility(View.GONE);
                } else {
                    pratinjauContainer.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tutup:
                if (isShowing == View.VISIBLE) {
                    pratinjauContainer.setVisibility(View.GONE);
                } else {
                    pratinjauContainer.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btnKirim:
                new prosesKirimLaporan().execute();
                break;
        }
    }

    private void getLocation() {
        flag = GPSStatus();
        if (flag) {
            location.setVisibility(View.VISIBLE);
            locationListener = new CustomLocationListener();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        } else {
            showAlertGPSDisabled();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQ) {
            if (resultCode == RESULT_OK) {
                fromRealPath = Uri.fromFile(new File(realPath));
                try {
                    FixBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(fromRealPath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                pratinjauImage.setImageBitmap(FixBitmap);
                pratinjauContainer.setVisibility(View.VISIBLE);
            } else if (resultCode == RESULT_CANCELED) {

            }
        } else if (requestCode == GALLERY_REQ) {
            if (resultCode == RESULT_OK) {
                if (Build.VERSION.SDK_INT < 19)
                    realPath = FilePath.getRealPathFromURI_API11to18(this, data.getData());
                else
                    realPath = FilePath.getRealPathFromURI_API19(this, data.getData());

                fromRealPath = Uri.fromFile(new File(realPath));
                try {
                    FixBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(fromRealPath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                pratinjauImage.setImageBitmap(FixBitmap);
                pratinjauContainer.setVisibility(View.VISIBLE);

                //convert Image


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Dibatalkan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    private File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                folderName);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + folderName + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
            filename = "IMG_" + timeStamp + ".jpg";
            realPath = mediaFile.toString();
        } else {
            return null;
        }

        return mediaFile;
    }

    private Boolean GPSStatus() {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;
        } else {
            return false;
        }
    }

    private void showAlertGPSDisabled() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Device's GPS is Disable")
                .setCancelable(false)
                .setTitle("** Gps Status **")
                .setPositiveButton("Gps On",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // finish the current activity
                                // AlertBoxAdvance.this.finish();
                                Intent myIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // cancel the dialog box
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private class CustomLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            posisi.setText("");
            if (location.getVisibility() == View.VISIBLE) {
                location.setVisibility(View.GONE);
            }
//            Toast.makeText(getBaseContext(), "Location changed : Lat: " +
//                            loc.getLatitude() + " Lng: " + loc.getLongitude(),
//                    Toast.LENGTH_SHORT).show();
            String longitude = String.valueOf(new DecimalFormat("##.####").format(loc.getLongitude()));
            Log.v(TAG, longitude);
            String latitude = String.valueOf(new DecimalFormat("##.####").format(loc.getLatitude()));
            Log.v(TAG, latitude);

            /*----------to get City-Name from coordinates ------------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(),
                    Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(), loc
                        .getLongitude(), 1);
                if (addresses.size() > 0)
                    System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String s = latitude + "; " + longitude +
                    "\nKota/Kabupaten/Desa: " + cityName;
            posisi.setText(s);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    private class prosesKirimLaporan extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            FixBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
            convertedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            progressDialog = ProgressDialog.show(LaporByPositionActivity.this, "", "Mengirimkan Laporan", false, false);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<>();
            RequestHandler handler = new RequestHandler();

            params.put(Config.NO_KTP, "1606022502940003");
            params.put(Config.POSISI, "-7.78328184, 110.50409317");
            params.put(Config.JUMLAH_KORBAN, "2");
            params.put(Config.STATUS_KORBAN, "MD");
            params.put(Config.GAMBAR, "IMG_0001.jpg");
            params.put(Config.KONDISI_KORBAN, "Patah bagian kaki");
            params.put(Config.FILE_CONTENT, convertedImage);
            return handler.sendPostRequest(Config.URL_LAPOR, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Log.i(TAG, "onPostExecute: " + s);
        }
    }
}

