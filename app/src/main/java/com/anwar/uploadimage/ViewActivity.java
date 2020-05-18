package com.anwar.uploadimage;

/*
 * Anwar Chowdhury
 * https://github.com/anwarch0wdhury
 * Date:2020/01/28
 * */

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;



public class ViewActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener{
private FloatingActionButton fbtn_add;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Image_model> mImageModelModels;
    TextView tv_view;

    int[] animationList = {R.anim.layout_animation_up_to_down, R.anim.layout_animation_right_to_left, R.anim.layout_animation_down_to_up, R.anim.layout_animation_left_to_right};

    int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_view);






        initAdapter();
        populateData();




        fbtn_add =findViewById (R.id.fbtn_Add);
        fbtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ViewActivity.this, UploadActivity.class);
                startActivity(i);
            }
        });
        tv_view=findViewById (R.id.tv_view);
        tv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (i < animationList.length - 1) {
                    i++;
                } else {
                    i = 0;
                }
                runAnimationAgain();}
        });


    }








    private void openDetailActivity(String[] data){
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("NAME_KEY",data[0]);
        intent.putExtra("DESCRIPTION_KEY",data[1]);
        intent.putExtra("IMAGE_KEY",data[2]);
        Log.e (String.valueOf (ViewActivity.this),"Clickedddd");
        startActivity(intent);
    }

    private void initAdapter() {

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager (this));

        mProgressBar = findViewById(R.id.myDataLoaderProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mImageModelModels = new ArrayList<> ();

        mAdapter = new RecyclerAdapter (ViewActivity.this, mImageModelModels);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(ViewActivity.this);

    }

    private void populateData() {



        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("image_list");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mImageModelModels.clear();

                for (DataSnapshot imageSnapshot : dataSnapshot.getChildren()) {
                    Image_model upload = imageSnapshot.getValue(Image_model.class);
                    upload.setKey(imageSnapshot.getKey());
                    mImageModelModels.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ViewActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });


    }


    private void runAnimationAgain() {


        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(this,animationList[i]);

        mRecyclerView.setLayoutAnimation(controller);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scheduleLayoutAnimation();

    }


    public void onItemClick(int position) {
        Image_model clickedImageModelModel = mImageModelModels.get(position);
        String[] imageData ={clickedImageModelModel.getName(), clickedImageModelModel.getDescription(), clickedImageModelModel.getImageUrl()};

        openDetailActivity(imageData);
    }

    @Override
    public void onShowItemClick(int position) {
        Image_model clickedImageModelModel = mImageModelModels.get(position);
        String[] imageData ={clickedImageModelModel.getName(), clickedImageModelModel.getDescription(), clickedImageModelModel.getImageUrl()};
        openDetailActivity(imageData);
    }

    @Override
    public void onDeleteItemClick(int position) {
        Image_model selectedItem = mImageModelModels.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void> () {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(ViewActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCopyItemClick(int position) {
        Image_model selectedItem = mImageModelModels.get(position);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("URL", selectedItem.getImageUrl());
        clipboard.setPrimaryClip(clip);
    }

    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

}

