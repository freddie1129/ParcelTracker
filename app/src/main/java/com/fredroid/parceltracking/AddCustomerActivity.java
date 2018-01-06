package com.fredroid.parceltracking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fredroid.parceltracking.db.entity.Customer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddCustomerActivity extends AppCompatActivity {

    public static String PARAM_CUSTOMER = "current-customer";

    private Customer customer;

    @BindView(R.id.button_add_customer_add)
    Button buttonAdd;

    @BindView(R.id.editText_add_customer_name)
    EditText editTextName;

    @BindView(R.id.editText_add_customer_address)
    EditText editTextAddress;

    @BindView(R.id.editText_add_customer_phone)
    EditText editTextPhone;
    @BindView(R.id.editText_add_customer_note)
    EditText editTextNote;

    @OnClick(R.id.button_add_customer_add)
    public void onClickAddCustomer()
    {
      //  Customer customer = new Customer(editTextName.getText().toString(),
       //         editTextAddress.getText().toString(),
      //         editTextNote.getText().toString());
        if (customer == null)
        {
            customer = new Customer(editTextName.getText().toString(),
                             editTextAddress.getText().toString(),
                             editTextPhone.getText().toString(),
                             editTextNote.getText().toString());
        }
        else {
            customer.sName = editTextName.getText().toString();
            customer.sAddress = editTextAddress.getText().toString();
            customer.sPhone = editTextPhone.getText().toString();
            customer.sNote = editTextNote.getText().toString();
        }
        Intent intent = new Intent();
        intent.putExtra(MainActivity.RETURN_VALUE_CUSTOMER,customer);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        ButterKnife.bind(this);
        if (getIntent() != null)
        {
            customer = getIntent().getParcelableExtra(AddCustomerActivity.PARAM_CUSTOMER);
            if (customer != null) {
                editTextName.setText(customer.sName);
                editTextAddress.setText(customer.sAddress);
                editTextPhone.setText(customer.sPhone);
                editTextNote.setText(customer.sNote);
                buttonAdd.setText(getString(R.string.activity_add_customer_confirm));
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            String title = (customer == null) ? getString(R.string.activity_add_customer_title) : customer.sName;
            if (title.equals(""))
            {
                title = getString(R.string.activity_customer_none_name);
            }
            getSupportActionBar().setTitle(title);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        //return super.onSupportNavigateUp();
        onBackPressed();
        return true;
    }
}
