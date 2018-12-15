package com.fredroid.parceltracking;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fredroid.parceltracking.db.entity.Customer;
import com.fredroid.parceltracking.db.entity.MyParcel;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ParcelFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_CUSTOMER = "customer";

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<MyParcel> parcelList;
    private Customer customer;



    @BindView(R.id.recyclerView_parcel)
    RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ParcelFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ParcelFragment newInstance(Customer customer) {
        ParcelFragment fragment = new ParcelFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CUSTOMER, customer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            customer = getArguments().getParcelable(ARG_CUSTOMER);
            //mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parcel_list, container, false);
        ButterKnife.bind(this, view);

        parcelList =   MainActivity.databaseCreator.getDatabase().parcelDao().getFromCustomer(customer.nId); // .getAll();

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new MyParcelRecyclerViewAdapter(getActivity(), parcelList, mListener));
        recyclerView.setNestedScrollingEnabled(false);

        // Set the adapter
        /*if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyParcelRecyclerViewAdapter(parcelList, mListener));
        }*/
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteractionParcel(MyParcel item);
    }

    public void addParcel(MyParcel myParcel)
    {
        parcelList = MainActivity.databaseCreator.getDatabase().parcelDao().getFromCustomer(customer.nId);
        recyclerView.setAdapter(new MyParcelRecyclerViewAdapter(getActivity(), parcelList, mListener));
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void removeParcel(MyParcel myParcel)
    {
        parcelList.remove(myParcel); // .add(myParcel);

        recyclerView.getAdapter().notifyDataSetChanged();
    }


    public void updateParcel(MyParcel myParcel)
    {
        int len = parcelList.size();
        for (int i = 0; i < len; i++)
        {
            if (parcelList.get(i).nId == myParcel.nId)
            {
                parcelList.get(i).sParcel2DBar = myParcel.sParcel2DBar;
                parcelList.get(i).time = myParcel.time;
                parcelList.get(i).sNote = myParcel.sNote;
                parcelList.get(i).setsPhotoPath(myParcel.sPhotoPath);
                break;
            }
        }

        // customerList.add(customer);

        recyclerView.getAdapter().notifyDataSetChanged();

    }
}
