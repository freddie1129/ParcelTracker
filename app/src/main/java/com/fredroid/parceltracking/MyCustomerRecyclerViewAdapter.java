package com.fredroid.parceltracking;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fredroid.parceltracking.CustomerFragment.OnListFragmentInteractionListener;
import com.fredroid.parceltracking.db.entity.Customer;
import com.fredroid.parceltracking.db.entity.MyParcel;

import java.io.File;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MyCustomerRecyclerViewAdapter extends RecyclerView.Adapter<MyCustomerRecyclerViewAdapter.ViewHolder> {

    private final List<Customer> mValues;
    private final OnListFragmentInteractionListener mListener;

    private  Activity activity;

    public MyCustomerRecyclerViewAdapter(Activity activity, List<Customer> items, OnListFragmentInteractionListener listener) {
        this.activity = activity;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.itemView.setOnLongClickListener(null);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String sName;
        if (mValues.get(position).sName.equals(""))
        {
            sName = activity.getString(R.string.activity_customer_none_name);
        }
        else
        {
            sName = mValues.get(position).sName;
        }
            holder.textViewName.setText(sName);
        holder.textViewAddress.setText(activity.getString(R.string.activity_customer_address) + ":" + mValues.get(position).sAddress);
        holder.textViewPhoneNum.setText(activity.getString(R.string.activity_customer_phone) + ":" + mValues.get(position).sPhone);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
                                int nId = mValues.get(holder.getAdapterPosition()).nId;
                                List<MyParcel> myParcelList = MainActivity.databaseCreator.getDatabase().parcelDao().getFromCustomer(nId);
                                for (MyParcel parcel: myParcelList)
                                {
                                    List<String> photoPathList = parcel.getPhotoPathList();
                                    for (String path: photoPathList)
                                    {
                                        File file = new File(path);
                                        if (file.exists() && file.isFile())
                                        {
                                            file.delete();
                                        }
                                    }
                                }
                                MainActivity.databaseCreator.getDatabase().customerDao().remove(nId);
                                mValues.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                return true;
                            case R.id.menu_item_edit:
                                Intent intent = new Intent(activity,AddCustomerActivity.class);
                                intent.putExtra(AddCustomerActivity.PARAM_CUSTOMER,
                                        mValues.get(holder.getAdapterPosition()));
                                activity.startActivityForResult(intent,MainActivity.ACTIVITY_RESULT_CODE_EDIT_CUSTOMER);
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

      //  holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
      //      @Override
           // public boolean onLongClick(View v) {
              //  setPosition(holder.getAdapterPosition());


             //   return false;
           // }
       // });



    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
   {
        public final View mView;
        public final TextView textViewName;
        public final TextView textViewAddress;
        public final TextView textViewPhoneNum;
        public final ImageButton imageButtonOption;
        public Customer mItem;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            textViewName = (TextView) view.findViewById(R.id.textView_name);
            textViewAddress = (TextView)view.findViewById(R.id.textView_address);
            textViewPhoneNum = (TextView)view.findViewById(R.id.textView_phoneNum);
            imageButtonOption = (ImageButton)view.findViewById(R.id.imageButton_customer_option);
        }





       @Override
        public String toString() {
            return super.toString() + " '" + textViewName.getText() + "'";
        }



    }

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


}
