package com.swin.edu.au.chatbox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.swin.edu.au.chatbox.Model.User;

import java.net.URI;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {
    CircleImageView img_pf;
    TextView userName;
    DatabaseReference ref;
    FirebaseUser firebaseUser;
    StorageReference  storageRef;
    private  static final  int img_req=1;
    private Uri  imgURL;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        img_pf=findViewById(R.id.pf_image);
        userName=findViewById(R.id.userName);
        storageRef= FirebaseStorage.getInstance().getReference("img");




        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        ref=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user= dataSnapshot.getValue(User.class);
                userName.setText(user.getUserName());
                if(user.getImgURL().equals("default"))
                {
                    img_pf.setImageResource(R.mipmap.ic_launcher);
                }
                else
                {
                    Glide.with(getApplicationContext()).load(user.getImgURL()).into(img_pf);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

                img_pf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openImg();

                }
                });
    }

    private void openImg() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        //setResult(RESULT_OK);
        startActivityForResult(i,img_req);
    }

    private  String getFileType(Uri uri)
    {
        ContentResolver contentResolver =getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private  void uploadImg()
    {


        try {
            final ProgressDialog progressDialog= new ProgressDialog(UserProfileActivity.this);
            progressDialog.setMessage("uploading..");
            progressDialog.show();


            if(imgURL!=null)
            {
                final  StorageReference fileRef= storageRef.child(System.currentTimeMillis( )
                        +"."+getFileType(imgURL));

                uploadTask = fileRef.putFile(imgURL);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task <UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        return fileRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if(task.isSuccessful())
                        {
                            Uri downldURI=task.getResult();
                            String uri = downldURI.toString();
                            ref= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("imgURL",uri);
                            ref.updateChildren(hashMap);
                            progressDialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(UserProfileActivity.this, "Failed to Upload!!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
            else
            {
                Toast.makeText(this, "no image is selected yet!!", Toast.LENGTH_SHORT).show();
                //progressDialog.dismiss();
            }

        }
        catch(Exception e)
        {
            System.out.println(e);
        }





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==img_req && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imgURL= data.getData();
            if(uploadTask!=null && uploadTask.isInProgress())
            {
                Toast.makeText(this, "Image Uploading in progress..!!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                uploadImg();
            }

        }
    }
}
