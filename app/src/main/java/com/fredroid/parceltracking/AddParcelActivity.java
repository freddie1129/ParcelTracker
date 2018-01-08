package com.fredroid.parceltracking;

import android.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.fredroid.parceltracking.db.entity.Customer;
import com.fredroid.parceltracking.db.entity.MyParcel;
import com.fredroid.parceltracking.zxing.client.android.CaptureActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fredroid.parceltracking.MainActivity.REQUEST_CAMERA_PERMISSION;

public class AddParcelActivity extends AppCompatActivity implements
        MyPhotoRecyclerViewAdapter.OnListFragmentInteractionListener {

    public static final String PARAM_PARCEL = "add-parcel";
    public static final String PARAM_CUSTOMER = "addpage-customer";
    public static final String MAIN_CODE_STRING = "scan-result";
    public static final int ACTIVITY_CODE = 101;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "MyPhotoRecyclerViewAdapter";
    String mCurrentPhotoPath;
    @BindView(R.id.button_add_parcel_close)
    Button buttonClose;
    @BindView(R.id.editText_addparcel_barcode)
    EditText editTextBarcode;
    @BindView(R.id.editText_addparcel_note)
    EditText editTextNote;
    @BindView(R.id.spinner_addparcel_commpany)
    Spinner spinnerCommpany;
    @BindView(R.id.recyclerView_add_parcel_photo)
    RecyclerView recyclerViewAddPhoto;
    @BindView(R.id.button_add_parcel_addPhoto)
    ImageButton buttonAddPhoto;
    @BindView(R.id.button_add_parcel_deletePhoto)
    ImageButton buttonDelete;
    private MyParcel myParcel;
    private Customer customer;
    private List<String> photoLists = new ArrayList<String>();

    @OnClick(R.id.button_add_parcel_addPhoto)
    public void onClickPhoto() {

        if (photoLists.size() >= ParcelConstant.MAX_PHOTO) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(AddParcelActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(AddParcelActivity.this);
            }
            String sInfo = String.format(getString(R.string.dialog_info_over_photo), ParcelConstant.MAX_PHOTO);

            builder.setTitle(AddParcelActivity.this.getString(R.string.dialog_title_error))
                    .setMessage(sInfo)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
            return;
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.fredroid.parceltracking",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

    }

    @OnClick(R.id.button_add_parcel_deletePhoto)
    public void deletePhoto() {
        boolean bShow = ((MyPhotoRecyclerViewAdapter) recyclerViewAddPhoto.getAdapter()).isbShowDelete();
        bShow = !bShow;
        ((MyPhotoRecyclerViewAdapter) recyclerViewAddPhoto.getAdapter()).setbShowDelete(bShow);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(AddParcelActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @OnClick(R.id.button_add_parcel_scan)
    public void onClickScan() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {

                showMessageOKCancel(getString(R.string.request_permission_camera),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(AddParcelActivity.this, new String[] {android.Manifest.permission.CAMERA},
                                        REQUEST_CAMERA_PERMISSION);
                            }
                        });

            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},
                        REQUEST_CAMERA_PERMISSION);

            }
        }
        else {
            Intent intent = new Intent(this, CaptureActivity.class);
            startActivityForResult(intent, ACTIVITY_CODE);
        }
        }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            /*  AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setMessage("need perssion")
                        .setPositiveButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                              //  finish();
                                return;

                            }
                        })
                        .setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                if (ContextCompat.checkSelfPermission(MainActivity.this,
                                        Manifest.permission.READ_CONTACTS)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.CAMERA)) {
                                    } else {
                                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA},
                                                REQUEST_CAMERA_PERMISSION);
                                        return;
                                    }
                                }
                            }
                        })
                        .show();*/
                return;
            } else {
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, ACTIVITY_CODE);
            }
        }
    }

    @OnClick(R.id.button_add_parcel_close)
    public void onClickClose() {

        if (myParcel == null) {
            myParcel = new MyParcel();
        }
        myParcel.setnCustomerId(customer.nId);
        myParcel.setTime(new Date());
        myParcel.setsNote(editTextNote.getText().toString());
        myParcel.setsParcel2DBar(editTextBarcode.getText().toString());
        myParcel.setsPhotoPath(myParcel.getPhotoPathString(photoLists));
        myParcel.setnCommpany(spinnerCommpany.getSelectedItemPosition());
        Intent intent = new Intent();
        intent.putExtra(ParcelActivity.RETURN_VALUE_PARCEL,
                myParcel);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parcel);
        ButterKnife.bind(this);

        editTextBarcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v instanceof EditText) {
                    EditText textView = (EditText) v;
                    int nCommpany = ParcelConstant.getCommpanyId(textView.getText().toString());
                    spinnerCommpany.setSelection(nCommpany);
                }
            }
        });

        if (getIntent() != null) {
            myParcel = getIntent().getParcelableExtra(AddParcelActivity.PARAM_PARCEL);
            if (myParcel != null) {
                editTextBarcode.setText(myParcel.sParcel2DBar);
                editTextNote.setText(myParcel.sNote);
                buttonClose.setText(getString(R.string.activity_add_parcel_confirm));
                spinnerCommpany.setSelection(myParcel.nCommpany);
            }
            customer = getIntent().getParcelableExtra(AddParcelActivity.PARAM_CUSTOMER);
            if (customer != null) {
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            String title = getString((myParcel == null) ? R.string.activity_add_parcel_title_add : R.string.activity_add_parcel_title_edit);
            getSupportActionBar().setTitle(title);

        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.commpany_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCommpany.setAdapter(adapter);
        recyclerViewAddPhoto.setLayoutManager(new GridLayoutManager(recyclerViewAddPhoto.getContext(), 4));
        //  recyclerViewAddPhoto.setLayoutManager(new LinearLayoutManager(recyclerViewAddPhoto.getContext()));
        if (myParcel != null)
            photoLists = myParcel.getPhotoPathList();
        else
            photoLists = new ArrayList<String>();
        recyclerViewAddPhoto.setAdapter(new MyPhotoRecyclerViewAdapter(this, photoLists, this));
    }
    @Override
    public boolean onSupportNavigateUp() {
        //return super.onSupportNavigateUp();
        onBackPressed();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_camera) {
            if (photoLists.size() >= ParcelConstant.MAX_PHOTO) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(AddParcelActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(AddParcelActivity.this);
                }
                String sInfo = String.format(getString(R.string.dialog_info_over_photo), ParcelConstant.MAX_PHOTO);

                builder.setTitle(AddParcelActivity.this.getString(R.string.dialog_title_error))
                        .setMessage(sInfo)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                return true;
            }
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.fredroid.parceltracking",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }

            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    public void setCommpany(int nCommpanyId) {

     /*   radioButtonEwe.setChecked(false);
        radioButtonFreakyQuicky.setChecked(false);
        radioButtonFastGo.setChecked(false);
        radioButtonStarEx.setChecked(false);
        radioButtonSpeed.setChecked(false);
        radioButtonChangjiang.setChecked(false);
        radioButtonOther.setChecked(false);

        switch (nCommpanyId)
        {
            case ParcelConstant.COMMPANY_ewe:
                radioButtonEwe.setChecked(true);
                break;
            case ParcelConstant.COMMPANY_freaky_quick:
                radioButtonFreakyQuicky.setChecked(true);
                break;
            case ParcelConstant.COMMPANY_fast_go:
                radioButtonFastGo.setChecked(true);
                break;
            case ParcelConstant.COMMPANY_star_ex:
                radioButtonStarEx.setChecked(true);
                break;
            case ParcelConstant.COMMPANY_speed:
                radioButtonSpeed.setChecked(true);
                break;
            case ParcelConstant.COMMPANY_changjiang:
                radioButtonChangjiang.setChecked(true);
                break;
            default:
                radioButtonOther.setChecked(true);
                break;
        }*/
    }

    // Get the results:
    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                String sCode = result.getContents();
                int nCommpany = ParcelConstant.getCommpanyId(sCode);
                setCommpany(nCommpany);
                editTextBarcode.setText(sCode);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }*/
        if (requestCode == ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String sCode = data.getStringExtra(MAIN_CODE_STRING);
                //  ((TextView)findViewById(R.id.textView_code)).setText(sCode);
                //  String sCode = result.getContents();
                int nCommpany = ParcelConstant.getCommpanyId(sCode);
                spinnerCommpany.setSelection(nCommpany);
                //setCommpany(nCommpany);

                editTextBarcode.setText(sCode);
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
          /*  Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            try {
                File file = createImageFile();
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

*/
            photoLists.add(mCurrentPhotoPath);
            recyclerViewAddPhoto.getAdapter().notifyDataSetChanged();
            ((MyPhotoRecyclerViewAdapter) recyclerViewAddPhoto.getAdapter()).setbShowDelete(false);

            // }
            //catch (IOException e)
            //{
            //    Log.d(TAG, "onActivityResult: " + e.getMessage());
            //}
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onListFragmentInteractionPhoto(String item) {
        Log.d(TAG, "onListFragmentInteractionPhoto: " + item);
        Intent intent = new Intent(this, ShowPhotoActivity.class);
        intent.putExtra(ShowPhotoActivity.PARAM_SHOW_PHOTO, item);
        startActivity(intent);

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
