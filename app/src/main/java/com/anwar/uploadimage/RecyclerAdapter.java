package com.anwar.uploadimage;

/*-->
    <!--* Anwar Chowdhury-->
    <!--* https://github.com/anwarch0wdhury-->
    <!--* Date:2020/01/28-->
    <!--* */

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;


public  class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{
    private Context mContext;
    private List<Image_model> imageModelModels;
    private OnItemClickListener mListener;

    public RecyclerAdapter(Context context, List<Image_model> uploads) {
        mContext = context;
        imageModelModels = uploads;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_model, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Image_model currentImageModelModel = imageModelModels.get(position);
        holder.tv_name.setText(currentImageModelModel.getName());
        holder.tv_description.setText(currentImageModelModel.getDescription());

        Picasso.with (mContext)
                .load(currentImageModelModel.getImageUrl())
                .fit()
                .centerCrop()
                .into(holder.imgv_photo);


    }

    @Override
    public int getItemCount() {
        return imageModelModels.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView tv_name, tv_description;
        public ImageView imgv_photo;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tv_name =itemView.findViewById ( R.id.tv_Name);
            tv_description = itemView.findViewById(R.id.tv_Description);
            imgv_photo = itemView.findViewById(R.id.Imgv_Photo);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem showItem = menu.add( Menu.NONE, 1, 1, "Show");
            MenuItem deleteItem = menu.add(Menu.NONE, 2, 2, "Delete");
            MenuItem copyItem = menu.add(Menu.NONE, 3, 3, "Copy");

            showItem.setOnMenuItemClickListener(this);
            deleteItem.setOnMenuItemClickListener(this);
            copyItem.setOnMenuItemClickListener(this);




        }

// on click the item menu will be shown
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onShowItemClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteItemClick(position);
                            return true;
                        case 3:
                            mListener.onCopyItemClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onShowItemClick(int position);
        void onDeleteItemClick(int position);
        void onCopyItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }




}
