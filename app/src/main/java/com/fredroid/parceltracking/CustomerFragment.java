package com.fredroid.parceltracking;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.fredroid.parceltracking.db.entity.Customer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CustomerFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<Customer> customerList;

    @BindView(R.id.recyclerView_customer)
    RecyclerView recyclerView;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CustomerFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CustomerFragment newInstance(int columnCount) {
        CustomerFragment fragment = new CustomerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_list, container, false);
        ButterKnife.bind(this,view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        customerList = MainActivity.databaseCreator.getDatabase().customerDao().getAll();
        recyclerView.setAdapter(new MyCustomerRecyclerViewAdapter(getActivity(), customerList, mListener));
        recyclerView.setNestedScrollingEnabled(false);
        //registerForContextMenu(recyclerView);


        // Set the adapter
        /*if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyCustomerRecyclerViewAdapter(customerList, mListener));
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
        void onListFragmentInteraction(Customer item);
    }

    public void addCustomer(Customer customer)
    {
       // customerList.add(customer);
        customerList = MainActivity.databaseCreator.getDatabase().customerDao().getAll();
        recyclerView.setAdapter(new MyCustomerRecyclerViewAdapter(getActivity(), customerList, mListener));
        //recyclerView.getAdapter().notifyDataSetChanged();

    }

    public void updateCustomer(Customer customer)
    {
        int len = customerList.size();
        for (int i = 0; i < len; i++)
        {
            if (customerList.get(i).nId == customer.nId)
            {
                customerList.get(i).sName = customer.sName;
                customerList.get(i).sAddress = customer.sAddress;
                customerList.get(i).sNote = customer.sNote;
                break;
            }
        }

       // customerList.add(customer);

        recyclerView.getAdapter().notifyDataSetChanged();

    }

    public void removeCustomer(Customer customer)
    {
        customerList.remove(customer);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

   /* @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.manu_manage, menu);
    }*/

 /*   @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_item_edit:
                Log.d(TAG, "onContextItemSelected: edit");
                return true;
            case R.id.menu_item_delete:
                Log.d(TAG, "onContextItemSelected: delete");
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }*/

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = ((MyCustomerRecyclerViewAdapter)recyclerView.getAdapter()).getPosition();
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.menu_item_delete:
                // do your stuff
                Log.d(TAG, "onContextItemSelected: delete");
                break;
            case R.id.menu_item_edit:
                // do your stuff
                Log.d(TAG, "onContextItemSelected: edit");
                break;
        }
        return super.onContextItemSelected(item);
    }
    



}
