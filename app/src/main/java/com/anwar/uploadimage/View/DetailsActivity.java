package com.anwar.uploadimage.View;
/*
 * Anwar Chowdhury
 * https://github.com/anwarch0wdhury
 * Date:2020/01/28
 * */
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.anwar.uploadimage.R;
import com.squareup.picasso.Picasso;



public class DetailsActivity extends AppCompatActivity {


    ImageView imgv_photo;

    private void initializeWidgets(){


        imgv_photo =findViewById(R.id.imgv_Photo);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initializeWidgets();

        //Receive data from ViewActivity via intent
        Intent i=this.getIntent();

        final String imageURL=i.getExtras().getString("IMAGE_KEY");

        //Set my Recived datas to imageviews

        Picasso.with(this)
                .load(imageURL)
                .fit()
                .centerInside ()
                .into(imgv_photo);





    }

}
