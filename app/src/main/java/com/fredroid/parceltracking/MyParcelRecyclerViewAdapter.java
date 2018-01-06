package com.fredroid.parceltracking;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fredroid.parceltracking.ParcelFragment.OnListFragmentInteractionListener;
import com.fredroid.parceltracking.db.entity.MyParcel;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MyParcel} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyParcelRecyclerViewAdapter extends RecyclerView.Adapter<MyParcelRecyclerViewAdapter.ViewHolder> {

    private final List<MyParcel> mValues;
    private final ParcelFragment.OnListFragmentInteractionListener mListener;
    private Activity activity;

    public MyParcelRecyclerViewAdapter(Activity activity, List<MyParcel> items, ParcelFragment.OnListFragmentInteractionListener listener) {
        this.activity = activity;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_parcel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Date time = mValues.get(position).time;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
      //  String sFirstLine = df.format(time) + " Code: " + mValues.get(position).sParcel2DBar;


        String [] commpanyName = activity.getResources().getStringArray(R.array.commpany_array);
        String sCommpanyName = "";
        if (mValues.get(position).getnCommpany() == ParcelConstant.COMMPANY_other)
        {
            sCommpanyName = activity.getResources().getString(R.string.commpany_name_other);
        }
        else
        {
            sCommpanyName = commpanyName[mValues.get(position).getnCommpany()];
        }
        String strCompany = String.format("%s:%s",
                activity.getResources().getString(R.string.activity_parcel_commpany),sCommpanyName

                );
        String strNum = String.format("%s:%s",
                activity.getResources().getString(R.string.activity_parcel_number),
                mValues.get(position).getsParcel2DBar());

        holder.textViewTime.setText(activity.getString(R.string.activity_parcel_time) + ":" + df.format(time));
        holder.textViewCommpany.setText(strCompany);
        holder.textViewNumber.setText(strNum);
        holder.textViewNote.setText(activity.getString(R.string.activity_parcel_note) + ":" + mValues.get(position).sNote);


      //  List<String> photoPathLists = new ArrayList<String>();
        MyParcel myParcel = mValues.get(position);
        List<String> photoPathLists = myParcel.getPhotoPathList();

        if (activity instanceof  ParcelActivity && photoPathLists.size() >= 1) {
            holder.listener = (ParcelActivity) activity;
            holder.recyclerViewPhotos.setLayoutManager(new GridLayoutManager(activity, 4));
            holder.recyclerViewPhotos.setAdapter(new MyPhotoRecyclerViewAdapter(activity,photoPathLists, holder.listener));
            holder.imageButtonPhoto.setVisibility(View.VISIBLE);
            holder.recyclerViewPhotos.setVisibility(View.GONE);
        }
        else
        {
            holder.imageButtonPhoto.setVisibility(View.GONE);
            holder.recyclerViewPhotos.setVisibility(View.GONE);
        }


      /*  holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteractionParcel(holder.mItem);
                }
            }
        });
*/

      holder.linearLayoutParcelInfo.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (null != mListener) {
                  // Notify the active callbacks interface (the activity, if the
                  // fragment is attached to one) that an item has been selected.
                  mListener.onListFragmentInteractionParcel(holder.mItem);
              }
          }
      });



      holder.imageButtonPhoto.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
             if(holder.recyclerViewPhotos.getVisibility() == View.GONE)
             {
                 holder.recyclerViewPhotos.setVisibility(View.VISIBLE);
             }
             else
             {
                 holder.recyclerViewPhotos.setVisibility(View.GONE);
             }
          }
      });



        holder.imageButtonOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    activity.getMenuInflater().inflate(R.menu.manu_manage, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_item_delete:
                                    MyParcel myParcelDelete = mValues.get(holder.getAdapterPosition());

                                    int nId = mValues.get(holder.getAdapterPosition()).nId;
                                    MainActivity.databaseCreator.getDatabase().parcelDao().remove(nId);
                                    mValues.remove(holder.getAdapterPosition());
                                    //delete file
                                    List<String>  photoPaths = myParcelDelete.getPhotoPathList();
                                    for (String path:photoPaths)
                                    {
                                        File photoFile = new File(path);
                                        if (photoFile.isFile() && photoFile.exists())
                                        {
                                            photoFile.delete();
                                        }
                                    }


                                    notifyItemRemoved(holder.getAdapterPosition());
                                    return true;
                                case R.id.menu_item_edit:
                                    Intent intent = new Intent(activity,AddParcelActivity.class);
                                    intent.putExtra(ParcelActivity.RETURN_VALUE_PARCEL,
                                            mValues.get(holder.getAdapterPosition()));
                                    intent.putExtra(AddParcelActivity.PARAM_CUSTOMER,((ParcelActivity)activity).getCustomer());
                                    activity.startActivityForResult(intent,ParcelActivity.ACTIVITY_CODE_EDIT_PARCEL);
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }

        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final LinearLayout linearLayoutParcelInfo;
        public final TextView textViewTime;
        public final TextView textViewNote;
        public final TextView textViewNumber;
        public final TextView textViewCommpany;
        public final ImageButton imageButtonOption;
        public final RecyclerView recyclerViewPhotos;
        public final ImageButton imageButtonPhoto;
        public MyParcel mItem;
        public MyPhotoRecyclerViewAdapter.OnListFragmentInteractionListener listener;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewTime = (TextView) view.findViewById(R.id.textView_time);
            textViewNote = (TextView) view.findViewById(R.id.textView_note);
            textViewNumber = (TextView) view.findViewById(R.id.textView_number);
            textViewCommpany = (TextView) view.findViewById(R.id.textView_commpany);
            linearLayoutParcelInfo = (LinearLayout) view.findViewById(R.id.linearLayout_parcel_info);
            recyclerViewPhotos = (RecyclerView) view.findViewById(R.id.recyclerView_photo);
            imageButtonPhoto = (ImageButton) view.findViewById(R.id.imageButton_parcel_photo);
            imageButtonOption = (ImageButton)view.findViewById(R.id.imageButton_parcel_option);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewNote.getText() + "'";
        }
    }
}
