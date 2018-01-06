package com.fredroid.parceltracking;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.BitSet;
import java.util.List;

/**
 * Created by jackttc on 1/1/18.
 */



public class MyPhotoRecyclerViewAdapter extends RecyclerView.Adapter<MyPhotoRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;
    private final OnListFragmentInteractionListener mListener;
    private  boolean bShowDelete = false;
    private final Activity activity;

    public MyPhotoRecyclerViewAdapter(Activity activity, List<String> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
       this.activity = activity;
    }

    public boolean isbShowDelete() {
        return bShowDelete;
    }

    public void setbShowDelete(boolean bShowDelete) {
        this.bShowDelete = bShowDelete;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_photos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        // holder.textViewTime.setText(mValues.get(position).sTime);
        // holder.textViewSite.setText(mValues.get(position).sLocation);
        //holder.textViewRecord.setText(mValues.get(position));

     //   Bitmap imageBitmap = (Bitmap) extras.get("data");
      //  mImageView.setImageBitmap(imageBitmap);

        if (holder.bitmap == null) {
            //  Bitmap image = BitmapFactory.decodeFile(mValues.get(position));


            Bitmap image = BitmapFactory.decodeFile(mValues.get(position));
            if (image != null) {
                int image_width = MainActivity.SCREEN_WIDTH / 4;
                float ratio = ((float) image.getWidth()) / ((float) image_width);
                int image_hight = (int) (image.getHeight() / ratio);


                holder.bitmap = Bitmap.createScaledBitmap(image,
                        image_width, image_hight, false);

                //  holder.imageViewDeleteMark.setLayoutParams(new FrameLayout.LayoutParams(image_width, image_hight));

                //  Layoutp holder.imageViewDeleteMark.getLayoutParams();


                // BitmapFactory.decodeFile(mValues.get(op ))

                if (image != null)
                    holder.imageViewPhoto.setImageBitmap(holder.bitmap);
            }
        }

            Bitmap icon = BitmapFactory.decodeResource( activity.getResources(),R.drawable.ic_delete_forever);
            holder.imageViewDeleteMark.setImageBitmap(icon);


          //  ViewGroup.LayoutParams lp = holder.imageViewDeleteMark.getLayoutParams();
          // lp.width = holder.imageViewPhoto.getWidth();
          // lp.height = holder.imageViewPhoto.getHeight();
          //  holder.imageViewDeleteMark.setLayoutParams(lp);
            holder.imageViewDeleteMark.setVisibility(bShowDelete ? View.VISIBLE : View.INVISIBLE);

           /* holder.imageViewPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
*/

            //mListener = (MyParcelRecyclerViewAdapter.OnListFragmentInteractionListener) activity.getBaseContext();


            holder.imageViewPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        if (bShowDelete) {
                            mValues.remove(mValues.get(position));
                            notifyDataSetChanged();
                        }
                        else
                        {
                            mListener.onListFragmentInteractionPhoto(holder.mItem);
                        }
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageViewPhoto;
        public final ImageView imageViewDeleteMark;
        public  Bitmap bitmap;
        // public final TextView textViewTime;
        //  public final TextView textViewSite;
        //  public final TextView textViewRecord;
        //public final TextView textViewRecord;


        public String mItem;

        public ViewHolder(View view) {
            super(view);
                mView = view;
                imageViewPhoto = (ImageView) view.findViewById(R.id.image_parcel_photo);
                imageViewDeleteMark = (ImageView) view.findViewById(R.id.image_parcel_delete_mark);
            //  textViewTime = (TextView) view.findViewById(R.id.textView_record_time);
            //  textViewSite = (TextView) view.findViewById(R.id.textView_record_site);
            //  textViewRecord = (TextView) view.findViewById(R.id.textView_record_record);
           // textViewRecord = (TextView) view.findViewById(R.id.textView_record);


        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteractionPhoto(String item);
    }
}
