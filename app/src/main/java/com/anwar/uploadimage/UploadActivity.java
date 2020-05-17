package com.anwar.uploadimage;
/*
* Anwar Chowdhury
* https://github.com/anwarch0wdhury
* Date:2020/01/28
* */
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class UploadActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button imgbtn_choose;
    private Button btn_upload;
    private EditText et_name;
    private EditText et_description;
    private ImageView imgv_chosen;
    private ProgressBar progressbar_upload;

    private Uri mImageUri;

    private StorageReference mStorageRef;

    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_upload );

        imgbtn_choose = findViewById(R.id.imgbtn_Choose);
        btn_upload = findViewById(R.id.btn_Upload);
        et_name = findViewById(R.id.et_Name);
        et_description = findViewById ( R.id.et_Description );
        imgv_chosen = findViewById(R.id.imgv_Chosen);
        progressbar_upload = findViewById(R.id.progress_bar);

        mStorageRef = FirebaseStorage.getInstance().getReference("image_list");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("image_list");

        imgbtn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(UploadActivity.this, "An Upload is Still in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });
    }
    //choose image file from mobile storages.
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with (this).load(mImageUri).into(imgv_chosen);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            progressbar_upload.setVisibility(View.VISIBLE);
            progressbar_upload.setIndeterminate(true);

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot> () {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressbar_upload.setVisibility(View.VISIBLE);
                                    progressbar_upload.setIndeterminate(false);
                                    progressbar_upload.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(UploadActivity.this, "Image_model Upload successful.", Toast.LENGTH_LONG).show();
                            Image_model upload = new Image_model (et_name.getText().toString().trim(),
                                    taskSnapshot.getDownloadUrl().toString(),
                                    et_description.getText ().toString ());

                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);

                            progressbar_upload.setVisibility(View.INVISIBLE);
                            openImagesActivity ();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener () {
                        @Override
                        public void onFailure(@NonNull  Exception e) {
                            progressbar_upload.setVisibility(View.INVISIBLE);
                            Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot> () {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressbar_upload.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "You haven't selected any file.", Toast.LENGTH_SHORT).show();
        }
    }
    private void openImagesActivity(){
        Intent intent = new Intent(this, ViewActivity.class);
        startActivity(intent);
    }
}
