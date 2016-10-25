package edu.orangecoastcollege.cs273.jcabrera31.petprotector;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class PetListActivity extends AppCompatActivity {

    private ImageView petImageView;

    // This memeber variable stores the URI to wheatever image has been selected
    // Default: none.png (R.drawable.none)
    private Uri imageURI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);

        //hook up the petImageView to the layout
        petImageView = (ImageView) findViewById(R.id.petImageView);

        //Constructs a full URI to any Android resouce (id, drawable, color, layout, etc)
        imageURI = getUriToResource(this, R.drawable.none);

        //set the imageURI of the imageView in code
        petImageView.setImageURI(imageURI);

    }

    public void selectPetImage(View view)
    {
        //List of all the permissions we need to request form the user
        ArrayList<String> permList = new ArrayList<>();

        //start by seeing if we have permission to camera
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if(cameraPermission != PackageManager.PERMISSION_GRANTED);
            permList.add(Manifest.permission.CAMERA);

        //next check to see if we have read external storage permissions
        int readExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(readExternalStoragePermission != PackageManager.PERMISSION_GRANTED);
            permList.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        //next check to see if we have read external storage permissions
        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED);
        permList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //if the list has items (size >0) we need to repeat permission form the user:
        int requestCode =100;
        if(permList.size() > 0)
        {
            //convert the ArrayList (permList) into an array of strings
            String[] perms = new String[permList.size()];
            //request permissions from the user:

            ActivityCompat.requestPermissions(this, permList.toArray(perms), requestCode);

        }

        if(cameraPermission == PackageManager.PERMISSION_GRANTED
                && readExternalStoragePermission == PackageManager.PERMISSION_GRANTED
                && writeExternalStoragePermission == PackageManager.PERMISSION_GRANTED)
        {
            //User an Intent to launch the gallery and take pictures
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, requestCode);

        }
        else
        {
            Toast.makeText(this,
                    "Pet Protector requires camera and edteran store permission",
                    Toast.LENGTH_LONG);
        }

    }

    /**
     *
     * @param context
     * @param resId
     * @return Uri to resource by given id
     * @throws Resources.NotFoundException
     */
    public static Uri getUriToResource(@NonNull Context context,
                                       @AnyRes int resId) throws Resources.NotFoundException{

        /**
         * Return a Resource instance for your application's package.
         */
        Resources res = context.getResources();

        /**
         * Return Uri
         */
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId)
                +'/' + res.getResourceTypeName(resId)
                +'/' + res.getResourceEntryName(resId));


    }


}
