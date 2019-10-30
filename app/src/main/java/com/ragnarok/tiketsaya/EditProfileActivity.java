package com.ragnarok.tiketsaya;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {

    ImageView user_edit_photo;
    EditText editName, editBio, editUsername, editPassword, editEmail;
    Button btn_save, add;
    LinearLayout btn_back;

    Uri photo_location;
    Integer photo_max = 1;

    DatabaseReference reference;
    StorageReference storage;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        user_edit_photo = findViewById(R.id.user_edit_photo);
        btn_save = findViewById(R.id.btn_save);
        add = findViewById(R.id.add);
        editName = findViewById(R.id.editName);
        editBio = findViewById(R.id.editBio);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editEmail = findViewById(R.id.editEmail);

        getUsernameLocal();

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(username_key_new);

        storage = FirebaseStorage.getInstance().getReference()
                .child("Photousers").child(username_key_new);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                editName.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                editBio.setText(dataSnapshot.child("bio").getValue().toString());
                editUsername.setText(dataSnapshot.child("username").getValue().toString());
                editPassword.setText(dataSnapshot.child("password").getValue().toString());
                editEmail.setText(dataSnapshot.child("email_address").getValue().toString());

                Picasso.with(EditProfileActivity.this)
                        .load(dataSnapshot.child("url_photo_profile")
                                .getValue().toString()).centerCrop().fit().into(user_edit_photo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ubah state jadi loading
                btn_save.setEnabled(false);
                btn_save.setText("LOADING...");

                if (photo_location == null) {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().child("nama_lengkap").setValue(editName.getText().toString());
                            dataSnapshot.getRef().child("bio").setValue(editBio.getText().toString());
                            dataSnapshot.getRef().child("username").setValue(editUsername.getText().toString());
                            dataSnapshot.getRef().child("password").setValue(editPassword.getText().toString());
                            dataSnapshot.getRef().child("email_address").setValue(editEmail.getText().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    Intent backToProfile = new Intent(EditProfileActivity.this, UserProfileActivity.class);
                    startActivity(backToProfile);

                } else {
//                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            dataSnapshot.getRef().child("nama_lengkap").setValue(editName.getText().toString());
//                            dataSnapshot.getRef().child("bio").setValue(editBio.getText().toString());
//                            dataSnapshot.getRef().child("username").setValue(editUsername.getText().toString());
//                            dataSnapshot.getRef().child("password").setValue(editPassword.getText().toString());
//                            dataSnapshot.getRef().child("email_address").setValue(editEmail.getText().toString());
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });

                    // validasi file apakah ada
                    if (photo_location != null) {
                        final StorageReference storageReference =
                                storage.child(System.currentTimeMillis() + "." + getFileExtention(photo_location));

                        storageReference.putFile(photo_location)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String uri_photo = uri.toString();
                                                reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                                reference.getRef().child("nama_lengkap").setValue(editName.getText().toString());
                                                reference.getRef().child("bio").setValue(editBio.getText().toString());
                                                reference.getRef().child("username").setValue(editUsername.getText().toString());
                                                reference.getRef().child("password").setValue(editPassword.getText().toString());
                                                reference.getRef().child("email_address").setValue(editEmail.getText().toString());
                                            }
                                        });

                                    }
                                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                Toast.makeText(getApplicationContext(), "Data tersimpan!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    Intent backToProfile = new Intent(EditProfileActivity.this, UserProfileActivity.class);
                    startActivity(backToProfile);
                }
//                reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        dataSnapshot.getRef().child("nama_lengkap").setValue(editName.getText().toString());
//                        dataSnapshot.getRef().child("bio").setValue(editBio.getText().toString());
//                        dataSnapshot.getRef().child("username").setValue(editUsername.getText().toString());
//                        dataSnapshot.getRef().child("password").setValue(editPassword.getText().toString());
//                        dataSnapshot.getRef().child("email_address").setValue(editEmail.getText().toString());
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//                // validasi file apakah ada
//                if (photo_location != null) {
//                    final StorageReference storageReference =
//                            storage.child(System.currentTimeMillis() + "." + getFileExtention(photo_location));
//
//                    storageReference.putFile(photo_location)
//                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            String uri_photo = uri.toString();
//                                            reference.getRef().child("url_photo_profile").setValue(uri_photo);
//                                        }
//                                    });
//
//                                }
//                            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                            Toast.makeText(getApplicationContext(), "Data tersimpan!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//
//                Intent backToProfile = new Intent(EditProfileActivity.this, UserProfileActivity.class);
//                startActivity(backToProfile);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });
    }

    String getFileExtention(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(user_edit_photo);
        }
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
