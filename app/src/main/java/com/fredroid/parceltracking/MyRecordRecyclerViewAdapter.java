package com.fredroid.parceltracking;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fredroid.parceltracking.RecordFragment.OnListFragmentInteractionListener;
import com.fredroid.parceltracking.db.entity.RecordEwe;

import java.util.List;

public class MyRecordRecyclerViewAdapter extends RecyclerView.Adapter<MyRecordRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyRecordRecyclerViewAdapter(List<String> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
       // holder.textViewTime.setText(mValues.get(position).sTime);
       // holder.textViewSite.setText(mValues.get(position).sLocation);
        holder.textViewRecord.setText(mValues.get(position));


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                   // mListener.onListFragmentInteractionRecordEwe(holder.mItem);
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
       // public final TextView textViewTime;
      //  public final TextView textViewSite;
      //  public final TextView textViewRecord;
        public final TextView textViewRecord;


        public String mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
          //  textViewTime = (TextView) view.findViewById(R.id.textView_record_time);
          //  textViewSite = (TextView) view.findViewById(R.id.textView_record_site);
          //  textViewRecord = (TextView) view.findViewById(R.id.textView_record_record);
            textViewRecord = (TextView) view.findViewById(R.id.textView_record);


        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewRecord.getText() + "'";
        }
    }
}
