package com.example.a91910.apppermissions;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST=1;
    private Button mchoosebutton;
    private Button mupload;
    private Button mshowupload;
    private EditText imagename;
    private ImageView mimageview;
    private ProgressDialog progressDialog;

    private Uri mimageuri;
////////////////////////////////////
    private StorageReference mStorageRef;
    private FirebaseDatabase mFiredatabase;
    private DatabaseReference mdataref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //////////////////////////////////////////////
        mFiredatabase=FirebaseDatabase.getInstance();
        mdataref=mFiredatabase.getReference();
        mStorageRef=FirebaseStorage.getInstance().getReference();

    mchoosebutton=findViewById(R.id.choosefile);
    mupload=findViewById(R.id.uploadbutton);
    mshowupload=findViewById(R.id.showuploadbutton);

    imagename=findViewById(R.id.nameofupload);
    mimageview=findViewById(R.id.imageView);
    progressDialog=new ProgressDialog(this);
    progressDialog.setMessage("Uploading");

    mchoosebutton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           openfilechooser();
        }
    });

    mupload.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         uploadFile();
        }
    });

    mshowupload.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    });
    }

    private void openfilechooser()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    private String getFilExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadFile(){

     StorageReference newref= mStorageRef.child("uploads/"+System.currentTimeMillis()+"."+getFilExtension(mimageuri));
      newref.putFile(mimageuri)
              .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                  @Override
                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();

                      Upload upload=new Upload(imagename.getText().toString().trim(),
                              taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                      String uplodedId=mdataref.push().getKey();
                      DatabaseReference newmrefernce=mdataref.child(uplodedId);
                      newmrefernce.setValue(upload);
                  }
              })
              .addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"uploading Failed",Toast.LENGTH_LONG).show();


                  }
              })
              .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                  @Override
                  public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                      progressDialog.show();
                  }
              });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            mimageuri=data.getData();
            Picasso.get().load(mimageuri).into(mimageview);
        }
        else{
            Toast.makeText(getApplicationContext(),"not working",Toast.LENGTH_LONG).show();
        }
    }
}
