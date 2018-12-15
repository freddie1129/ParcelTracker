package com.fredroid.parceltracking;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.fredroid.parceltracking.api.ParcelApiClient;
import com.fredroid.parceltracking.api.ParcelService;
import com.fredroid.parceltracking.db.entity.Customer;
import com.fredroid.parceltracking.db.entity.MyParcel;
import com.fredroid.parceltracking.db.entity.RecordEwe;
import com.fredroid.parceltracking.db.entity.ParcelStatus;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ParcelActivity extends AppCompatActivity implements
ParcelFragment.OnListFragmentInteractionListener,
RecordFragment.OnListFragmentInteractionListener,
MyPhotoRecyclerViewAdapter.OnListFragmentInteractionListener{

    private static final String TAG = "ParcelActivity";
    public static final int ACTIVITY_CODE_ADD_PARCEL = 101;
    public static final int ACTIVITY_CODE_EDIT_PARCEL = 102;

    public static  String RETURN_VALUE_PARCEL = "add-parcel";

    private Customer customer;

    @BindView(R.id.frameLayut_parcel_activity_content)
    FrameLayout mLoginFormView;

    @BindView(R.id.progressdBar_download)
    ProgressBar progressBar;

    @BindView(R.id.floatingActionButton_parcel_add)
    FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null)
        {
            customer = getIntent().getParcelableExtra(MainActivity.PARAM_CUSTOMER_ID);
        }
        setContentView(R.layout.activity_parcel);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(customer.sName.equals("") ? getString(R.string.activity_customer_none_name):customer.sName);
        }

      //  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
              //          .setAction("Action", null).show();
                Intent addIntent = new Intent(ParcelActivity.this, AddParcelActivity.class);
              addIntent.putExtra(AddParcelActivity.PARAM_CUSTOMER,customer);
                //  addIntent.putExtra(CompleteActivity.ACTIVITY_RECORD_ID, nRecordID);
              //  pickContactIntent.putExtra(CaptyaConstants.ACTIVITY_PARAM_JOB_ID, item.getId());
              //  pickContactIntent.putExtra(MainActivity.ACTIVITY_CAMERA_MODE, CaptyaConstants.MODE_CAMERA_SUBMIT);
                startActivityForResult(addIntent, ParcelActivity.ACTIVITY_CODE_ADD_PARCEL);
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameLayout_Parcel,ParcelFragment.newInstance(customer))
                .addToBackStack(null)
                .commit();


    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean onSupportNavigateUp() {
       // return super.onSupportNavigateUp();
        int nCount = getSupportFragmentManager().getBackStackEntryCount();
        if (nCount == 1)
        {
            finish();

        }
        else {
            getSupportFragmentManager().popBackStack();
            floatingActionButton.setVisibility(View.VISIBLE);

        }
        return true;


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        int nCount = getSupportFragmentManager().getBackStackEntryCount();
        if (nCount == 1)
        {
            finish();

        }
        else {
            getSupportFragmentManager().popBackStack();
            floatingActionButton.setVisibility(View.VISIBLE);


        }
    }

    @Override
    public void onListFragmentInteractionParcel(MyParcel item) {
      // item.sParcel2DBar = "https://www.ewe.com.au/track?cno=b978379135b#track-result";
        //b978379135b

        String barcode = item.sParcel2DBar;
      // String strUrl = String.format("https://www.ewe.com.au/track?cno=%s#track-result",barcode);
     //   DownloadTrack downloadTrack = new DownloadTrack(strUrl);
        String sCode = item.getsParcel2DBar();
        //int nCommpany = ParcelConstant.getCommpanyId(sCode);
        /*0;
        if (sCode.matches("FQ\\d\\d\\d\\d\\d\\d"))
        {
            nCommpany = ParcelConstant.COMMPANY_freaky_quick;
        }
        if (sCode.matches("FG\\d{7}AU"))
        {
            nCommpany = ParcelConstant.COMMPANY_fast_go;
        }
        if (sCode.matches("SE\\d{7}AU"))
        {
            nCommpany = ParcelConstant.COMMPANY_star_ex;
        }
        if (sCode.matches("B\\d{9}B"))
        {
            nCommpany = ParcelConstant.COMMPANY_ewe;
        }

*/

        if (item.getnCommpany() == ParcelConstant.COMMPANY_other)
        {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(ParcelActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(ParcelActivity.this);
            }
            builder.setTitle(ParcelActivity.this.getString(R.string.dialog_title_error))
                    .setMessage(ParcelActivity.this.getString(R.string.dialog_info_failure))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
            return;
        }
        TaskTracking taskTracking = new TaskTracking(item.getnCommpany(), sCode);
        showProgress(true);
        taskTracking.execute();
      //downloadTrack.execute();




    }


    private class MyTask_fastgo extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String title ="";
            Document doc;
            try {

                doc = Jsoup.connect("http://www.fastgo.com.au/TrackNum.aspx?dh=FG0067366AU")
                        .get();

                Log.d(TAG, "doInBackground: ");

                Elements elements  = doc.select(".s_box table tr");

                for (int i = 1; i < elements.size();i++)
                {

                    Element e = elements.get(i);
                    Log.d(TAG, "doInBackground: ***************");
                    Elements info = e.getElementsByTag("td"); // .select("td");
                    for (Element item:info)
                    {
                        Log.d(TAG, "doInBackground: " + item.html());
                    }
                    Log.d(TAG, "doInBackground: ****************");

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return title;
        }
        @Override
        protected void onPostExecute(String result) {
            //if you had a ui element, you could display the title
            // ((TextView)findViewById (R.id.myTextView)).setText (result);
            Log.d(TAG, "onPostExecute: " + result);
            Log.d(TAG, "onPostExecute: ");
        }
    }


    private class TaskTracking extends AsyncTask<Void, Void, ArrayList<String>> {

        private int nCommpany;
        private String sCode;

        TaskTracking(int nCommpany, String sCode)
        {
            this.nCommpany = nCommpany;
            this.sCode = sCode;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            final ArrayList<String> recordList = new ArrayList<String>();
            Document doc;
            switch (nCommpany)
            {
                case ParcelConstant.COMMPANY_transrush:
                    try {
                        Response<ResponseBody> response = ParcelApiClient.getClient(ParcelConstant.COMMPANY_transrush).create(ParcelService.class).getStatusRes_transrush_Original(sCode).execute();
                        if (response.isSuccessful())
                        {
                            JSONObject jsonObjectAll = new JSONObject(response.body().string());
                            JSONArray jsonObject = jsonObjectAll.getJSONArray("data").getJSONObject(0).getJSONArray("tracks"); //  getJSONObject(0).getJSONArray("Details");
                            int s = jsonObject.length();
                            for (int i = 0; i < s; i++)
                            {
                                JSONObject o = jsonObject.getJSONObject(i);
                                String string = String.format("\n%s\n%s\n",
                                        o.getString("CreateDate"),o.getString("TrackContent"));
                                recordList.add(string);
                            }
                        }
                        else
                        {

                        }
                    }
                    catch (Exception e)
                    {

                    }
                    break;
                case ParcelConstant.COMMPANY_ewe:
                    try {
                        Response<ResponseBody> response = ParcelApiClient.getClient(ParcelConstant.COMMPANY_ewe).create(ParcelService.class).getStatusRes_Ewe_Original(sCode).execute();
                        if (response.isSuccessful())
                        {
                            JSONObject jsonObjectAll = new JSONObject(response.body().string());
                            JSONArray jsonObject = jsonObjectAll.getJSONArray("Payload").getJSONObject(0).getJSONArray("Details");
                            int s = jsonObject.length();
                            for (int i = 0; i < s; i++)
                            {
                                JSONObject o = jsonObject.getJSONObject(i);
                                String string = String.format("\n%s\n%s\n%s\n",
                                        o.getString("DateString"),o.getString("Place"),o.getString("Message"));
                                recordList.add(string);
                            }
                        }
                        else
                        {

                        }
                    }
                    catch (Exception e)
                    {

                    }

                    /*ParcelApiClient.getParcelStatus(new ParcelApiClient.IRequestCallback<List<ParcelStatus>>() {
                        @Override
                        public void onResponse(List<ParcelStatus> model) {

                            for (ParcelStatus record: model)
                            {
                                String s = String.format("\n%s\n%s\n%s",
                                        record.getDate(),record.getLocation(),record.getStatus());
                                recordList.add(s);
                            }
                        }
                        @Override
                        public void onFailure(int responseCode, String msg, Throwable t) {

                        }
                    }, sCode);*/

                    /*try {

                        String sUrl = String.format("https://www.ewe.com.au/track?cno=%s#track-result",sCode);
                        // doc = Jsoup.connect("https://www.ewe.com.au/track?cno=b978379135b#track-result").get();
                        doc = Jsoup.connect(sUrl).get();
                        Elements elements = doc.select(".track-right tbody tr");
                        for (Element track : elements) {
                            String s = "\n";
                            Elements data = track.getElementsByTag("td");
                           // String[] strings = new String[3];
                            for (int i = 0; i < data.size(); i++) {
                                s += data.get(i).html() + "\n";
                            }
                            recordList.add(s);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    break;
                case ParcelConstant.COMMPANY_fast_go:
                    try {
                        Response<ResponseBody> response = ParcelApiClient.getClient(ParcelConstant.COMMPANY_fast_go).create(ParcelService.class).getStatusRes_fastgo_Original(sCode).execute();
                        if (response.isSuccessful())
                        {
                            JSONObject jsonObjectAll = new JSONObject(response.body().string());
                            JSONArray jsonObject = jsonObjectAll.getJSONObject("result").getJSONArray("list");
                            int s = jsonObject.length();
                            for (int i = 0; i < s; i++)
                            {
                                JSONObject o = jsonObject.getJSONObject(i);
                                String string = String.format("\n%s\n%s\n",
                                        o.getString("time"),o.getString("remark"));
                                recordList.add(string);
                            }
                        }
                        else
                        {

                        }
                    }
                    catch (Exception e)
                    {

                    }
                    break;

//                    try {
//                        String sUrl = String.format("http://www.fastgo.com.au/TrackNum2.aspx?dh=%s",sCode);
//                        //   doc = Jsoup.connect("http://www.fastgo.com.au/TrackNum.aspx?dh=FG0067366AU")
//                        //           .get();
//                        doc = Jsoup.connect(sUrl)
//                                .get();
//                        Elements elements  = doc.select(".s_box table tr");
//                        for (int i = 1; i < elements.size();i++)
//                        {
//                            String s = "\n";
//                            Element e = elements.get(i);
//                            Elements info = e.getElementsByTag("td"); // .select("td");
//                            for (Element item:info)
//                            {
//                                s += item.html() + "\n";
//                            }
//                            recordList.add(s);
//                        }
//
//                        recordList.add("\n" + doc.select(".waicha .tigong").get(0).html() +"\n");
//                        Elements elements_china = doc.select(".ickd_return tr");
//                        for (int i = 1; i < elements_china.size(); i++)
//                        {
//                            String s = "\n";
//                            Element e = elements_china.get(i);
//                            Elements info = e.getElementsByTag("td");
//                            for (Element item:info)
//                            {
//                                s += item.html() + "\n";
//                            }
//                            recordList.add(s);
//                        }
//
//
//
//                    }catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    break;

                case ParcelConstant.COMMPANY_star_ex:
                    try{

                    doc = Jsoup.connect("http://www.starex.com.au/cgi-bin/GInfo.dll?EmmisTrack")
                            .data("cno", sCode)
                            .data("w","starex")
                            .post();
                    Elements elements  = doc.select("#oDetail table tr");
                    for (int i = 1; i < elements.size();i++)
                    {
                        String s = "\n";
                        Element e = elements.get(i);
                        Elements info = e.getElementsByTag("td"); // .select("td");
                        for (Element item:info)
                        {
                            s += item.html() + "\n";
                        }
                        recordList.add(s);
                    }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            return recordList;
        }
        @Override
        protected void onPostExecute(ArrayList<String> result) {
            //if you had a ui element, you could display the title
            // ((TextView)findViewById (R.id.myTextView)).setText (result);
            showProgress(false);

            if (result.size() == 0)
            {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(ParcelActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(ParcelActivity.this);
                }
                builder.setTitle(ParcelActivity.this.getString(R.string.dialog_title_error))
                        .setMessage(ParcelActivity.this.getString(R.string.dialog_info_failure))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                return;
            }

            Log.d(TAG, "onPostExecute: " + result);
            Log.d(TAG, "onPostExecute: ");

            floatingActionButton.setVisibility(View.GONE);
            RecordFragment recordFragment = RecordFragment.newInstance(result);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_Parcel, recordFragment).addToBackStack(null).commit();

        }
    }




    private class DownloadTrack extends AsyncTask<Void, Void, Elements> {

        String sUrl;

        DownloadTrack(String sUrl)
        {
            this.sUrl = sUrl;
        }

        @Override
        protected Elements doInBackground(Void... params) {
            String title ="";
            Document doc;
            try {
               // doc = Jsoup.connect("https://www.ewe.com.au/track?cno=b978379135b#track-result").get();
                doc = Jsoup.connect(sUrl).get();

                Elements elements = doc.select(".track-right tbody tr");
                        //getElementsByAttributeValue("class","track-right").get(0).get;

                //Log.d(TAG, "doInBackground: ");
                return elements;
                //return doc.getElementsByAttributeValue("class","track-right").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");  //  .tagName("tbody"); //   .selectFirst(".track-right").
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }


        @Override
        protected void onPostExecute(Elements body) {
            //ArrayList<RecordEwe> recordEweList) {
            //if you had a ui element, you could display the title
            // ((TextView)findViewById (R.id.myTextView)).setText (result);
           // Log.d(TAG, "onPostExecute: " + result);
           // Log.d(TAG, "onPostExecute: ");

          //  RecordFragment.newInstance(recordEweList);
            showProgress(false);


            if (body == null || body.size() == 0)
            {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(ParcelActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(ParcelActivity.this);
                }
                builder.setTitle(ParcelActivity.this.getString(R.string.dialog_title_error))
                        .setMessage(ParcelActivity.this.getString(R.string.dialog_info_failure))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
            else {

                ArrayList<RecordEwe> recordEwes = new ArrayList<RecordEwe>();
                String sTime, sSite, sRecord;
                for (Element track : body) {
                    Log.d(TAG, "*****************************************");
                    Elements data = track.getElementsByTag("td");
                    String[] strings = new String[3];
                    for (int i = 0; i < data.size(); i++) {
                        strings[i] = data.get(i).html();
                    }
                    recordEwes.add(new RecordEwe(strings[0], strings[1], strings[2]));
                }
                floatingActionButton.setVisibility(View.GONE);

               // RecordFragment recordFragment = RecordFragment.newInstance(recordEwes);
              //  getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_Parcel, recordFragment).addToBackStack(null).commit();

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ACTIVITY_CODE_ADD_PARCEL) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                MyParcel myParcel =  data.getParcelableExtra(RETURN_VALUE_PARCEL);

                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout_Parcel);
                if (fragment instanceof ParcelFragment)
                {
                   List<Customer> customerList =  MainActivity.databaseCreator.getDatabase().customerDao().getAll();
                    myParcel.nCustomerId = customer.nId;
                    MainActivity.databaseCreator.getDatabase().parcelDao().insertAll(myParcel);
                    ((ParcelFragment)fragment).addParcel(myParcel);
                }

            }
        }
        if (requestCode == ACTIVITY_CODE_EDIT_PARCEL) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                MyParcel myParcel =  data.getParcelableExtra(RETURN_VALUE_PARCEL);

                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout_Parcel);
                if (fragment instanceof ParcelFragment)
                {
                    myParcel.nCustomerId = customer.nId;
                    MainActivity.databaseCreator.getDatabase().parcelDao().insertAll(myParcel);
                    ((ParcelFragment)fragment).updateParcel(myParcel);
                }

            }
        }

       // super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onListFragmentInteractionRecordEwe(String item) {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
           // floatingActionButton.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                  //  floatingActionButton.setVisibility(show ? View.GONE : View.VISIBLE);


                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
           // floatingActionButton.setVisibility(show ? View.GONE : View.VISIBLE);

        }
    }

    @Override
    public void onListFragmentInteractionPhoto(String item) {
        Intent intent = new Intent(this, ShowPhotoActivity.class);
        intent.putExtra(ShowPhotoActivity.PARAM_SHOW_PHOTO,item);
        startActivity(intent);
    }
}
