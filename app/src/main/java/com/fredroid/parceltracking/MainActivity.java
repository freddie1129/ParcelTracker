package com.fredroid.parceltracking;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.fredroid.parceltracking.db.DatabaseCreator;
import com.fredroid.parceltracking.db.entity.Customer;
import com.fredroid.parceltracking.db.entity.MyParcel;
import com.fredroid.parceltracking.zxing.client.android.CaptureActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements
CustomerFragment.OnListFragmentInteractionListener{
    private static final String TAG = "MainActivity";
    public static String PARAM_CUSTOMER_ID = "customer-id";
    public static String RETURN_VALUE_CUSTOMER = "return-customer";
    public static final int ACTIVITY_RESULT_CODE_ADD_CUSTOMER = 101;
    public static final int ACTIVITY_RESULT_CODE_EDIT_CUSTOMER = 102;
    public static final int  ACTIVITY_RESULT_CODE_SCANNER = 103;

    public static final int REQUEST_CAMERA_PERMISSION = 1;
    public static DatabaseCreator databaseCreator;

    public static final String MAIN_CODE_STRING = "scan-result";

    public static  int SCREEN_WIDTH;
    public static  int SCREEN_HEIGHT;


    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @OnClick(R.id.fab)
    public void onClickAdd() {
        Intent intent = new Intent(this, AddCustomerActivity.class);
        startActivityForResult(intent, ACTIVITY_RESULT_CODE_ADD_CUSTOMER);
    }

   @OnClick(R.id.button_http_test)
    public void onClickTest()
    {

    //    Intent intent = new Intent(this,AddCustomerActivity.class);
     //   startActivityForResult(intent, ACTIVITY_RESULT_CODE_ADD_CUSTOMER);

      //  MyTask_StarEx myTask = new MyTask_StarEx();
      //    myTask.execute();
      //  MyTask_fq myTask_fq = new MyTask_fq();
      //  myTask_fq.execute();

        MyTask_fastgo myTask_fastgo = new MyTask_fastgo();
        myTask_fastgo.execute();

       //((WebView)findViewById(R.id.webView)).loadUrl("https://www.ewe.com.au/track?cno=b978379135b#track-result");
     /*  Retrofit retrofitClient = RetrofitClient.getClient_aa();
        Call<ResponseBody> authResponse = retrofitClient.create(APIInterface.class).httpTrackInfo("b978379135b");//ation.glbClient_secret, mEmail, mPassword);
        authResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try{
                      //  DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                      //  InputSource is = new InputSource();
                     //   is.setCharacterStream(new StringReader(response.body().string()));

                     //   Document doc = db.parse(is);
                     //   Document document =


                        if (response.body().string().contains("In transit to airport"))
                        {
                            Log.d(TAG, "onResponse:  layout");
                        }
                        Log.d(TAG, "onResponse: " + response.body().string());   //.body().string()
                    }
                    catch (Exception io)
                    {
                        Log.d(TAG, "onResponse: " + io.getMessage()
                        );
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Error code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });*/


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.activity_customer_title));
        }


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        SCREEN_WIDTH = displayMetrics.heightPixels;
        SCREEN_HEIGHT = displayMetrics.widthPixels;

      //  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      //  fab.setOnClickListener(new View.OnClickListener() {
     //       @Override
    //        public void onClick(View view) {
      //          Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
      //                  .setAction("Action", null).show();
        //    }
      //  });


       if (databaseCreator == null) {
           databaseCreator = DatabaseCreator.getInstance(this.getApplication());
           databaseCreator.createDb(this.getApplication());
           // add some test data
          // for (int i = 0; i < 5; i++)
          // {
          //     Customer customer = new Customer("Freddie" + i, "Babarra" + i,"note");
          //     databaseCreator.getDatabase().customerDao().insertAll(customer);
          // }
       }

       Intent intent = new Intent(this,WelcomeActivity.class);
       startActivity(intent);

       CustomerFragment customerFragment = CustomerFragment.newInstance(1);
       getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_customer,customerFragment).commit();




    }

    @Override
    public boolean onSupportNavigateUp() {
       // return super.onSupportNavigateUp();
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            /*  AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setMessage("need perssion")
                        .setPositiveButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                              //  finish();
                                return;

                            }
                        })
                        .setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                if (ContextCompat.checkSelfPermission(MainActivity.this,
                                        Manifest.permission.READ_CONTACTS)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.CAMERA)) {
                                    } else {
                                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA},
                                                REQUEST_CAMERA_PERMISSION);
                                        return;
                                    }
                                }
                            }
                        })
                        .show();*/
              return;
            } else {
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, ACTIVITY_RESULT_CODE_SCANNER);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,AboutActivity.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.action_scanning)
        {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

                    showMessageOKCancel(getString(R.string.request_permission_camera),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA},
                                            REQUEST_CAMERA_PERMISSION);
                                }
                            });
                    super.onOptionsItemSelected(item);

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA_PERMISSION);
                    super.onOptionsItemSelected(item);
                }
            }
            else {
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, ACTIVITY_RESULT_CODE_SCANNER);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onListFragmentInteraction(Customer item) {
        Intent intent = new Intent(this, ParcelActivity.class);

        intent.putExtra(MainActivity.PARAM_CUSTOMER_ID, item);

        startActivity(intent); //, MainActivity.ACTIVITY_RESULT_JOB_DETAIL);

    }


    private class MyTask_StarEx extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String title ="";
            Document doc;
            try {

                doc = Jsoup.connect("http://www.starex.com.au/cgi-bin/GInfo.dll?EmmisTrack")
                        .data("cno", "SE0133111AU")
                        .data("w","starex")
// and other hidden fields which are being passed in post request.
                    //    .userAgent("Mozilla")
                        .post();

                Log.d(TAG, "doInBackground: ");

                Elements elements  = doc.select("#oDetail table tr");
                
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


             //   doc = Jsoup.connect("https://www.ewe.com.au/track?cno=b978379135b#track-result").get();
               // Elements info = doc.select(".track_right ");
              // Elements info = doc.toggleClass(".track-right").getElementsByTag("tbody").get(0).getElementsByTag("tr");  //  .tagName("tbody"); //   .selectFirst(".track-right").
             //  Elements tra = info.getElementsByTag("table").  tagName("tbody").tagName("tr");
             //   Elements element = info.tagName("tbody");

             //   Element element = doc.getElementsByClass("track-right");
             //   Elements re = element.tagName("tbody");
           //     Element trackElement =
            //    Log.d(TAG, "doInBackground: Start");
             //   for (Element e : info)
             //   {
            //        Log.d(TAG, "*****************************************");
             //       Elements data  = e.getElementsByTag("td");
             //       for (Element txt: data)
             //       {
             //           Log.d(TAG, "doInBackground: " + txt.html());
              //      }
             //   }
             //   Log.d(TAG, "doInBackground: End");


               // title = doc.title();
               // System.out.print(title);
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

    private class MyTask_fq extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String title ="";
            Document doc;
            try {

                doc = Jsoup.connect("http://www.freakyquick.com.au/chaxun.php")
                        .data("numid", "FQ618632")
                      //  .data("w","starex")
// and other hidden fields which are being passed in post request.
                        //    .userAgent("Mozilla")
                        .post();

                Log.d(TAG, "doInBackground: ");

                Elements elements  = doc.select(".i-middle1 table tr");

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


                //   doc = Jsoup.connect("https://www.ewe.com.au/track?cno=b978379135b#track-result").get();
                // Elements info = doc.select(".track_right ");
                // Elements info = doc.toggleClass(".track-right").getElementsByTag("tbody").get(0).getElementsByTag("tr");  //  .tagName("tbody"); //   .selectFirst(".track-right").
                //  Elements tra = info.getElementsByTag("table").  tagName("tbody").tagName("tr");
                //   Elements element = info.tagName("tbody");

                //   Element element = doc.getElementsByClass("track-right");
                //   Elements re = element.tagName("tbody");
                //     Element trackElement =
                //    Log.d(TAG, "doInBackground: Start");
                //   for (Element e : info)
                //   {
                //        Log.d(TAG, "*****************************************");
                //       Elements data  = e.getElementsByTag("td");
                //       for (Element txt: data)
                //       {
                //           Log.d(TAG, "doInBackground: " + txt.html());
                //      }
                //   }
                //   Log.d(TAG, "doInBackground: End");


                // title = doc.title();
                // System.out.print(title);
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


    private class MyTask_fastgo extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String title ="";
            Document doc;
            try {

                doc = Jsoup.connect("http://www.fastgo.com.au/TrackNum.aspx?dh=FG0067366AU")
                        // .data("numid", "FQ618632")
                        //  .data("w","starex")
// and other hidden fields which are being passed in post request.
                        //    .userAgent("Mozilla")
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


                //   doc = Jsoup.connect("https://www.ewe.com.au/track?cno=b978379135b#track-result").get();
                // Elements info = doc.select(".track_right ");
                // Elements info = doc.toggleClass(".track-right").getElementsByTag("tbody").get(0).getElementsByTag("tr");  //  .tagName("tbody"); //   .selectFirst(".track-right").
                //  Elements tra = info.getElementsByTag("table").  tagName("tbody").tagName("tr");
                //   Elements element = info.tagName("tbody");

                //   Element element = doc.getElementsByClass("track-right");
                //   Elements re = element.tagName("tbody");
                //     Element trackElement =
                //    Log.d(TAG, "doInBackground: Start");
                //   for (Element e : info)
                //   {
                //        Log.d(TAG, "*****************************************");
                //       Elements data  = e.getElementsByTag("td");
                //       for (Element txt: data)
                //       {
                //           Log.d(TAG, "doInBackground: " + txt.html());
                //      }
                //   }
                //   Log.d(TAG, "doInBackground: End");


                // title = doc.title();
                // System.out.print(title);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_RESULT_CODE_ADD_CUSTOMER) {
            if (resultCode == RESULT_OK) {
                Customer customer = data.getParcelableExtra(RETURN_VALUE_CUSTOMER);

                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout_customer);
                if (fragment instanceof CustomerFragment)
                {

                    MainActivity.databaseCreator.getDatabase().customerDao().insertAll(customer);
                    List<Customer> listCustomer = MainActivity.databaseCreator.getDatabase().customerDao().getAll();
                    ((CustomerFragment)fragment).addCustomer(customer);
                    Log.d(TAG, "onActivityResult: ");
                }

            }
        }
        if (requestCode == ACTIVITY_RESULT_CODE_EDIT_CUSTOMER)
        {
            if (resultCode == RESULT_OK) {
                Customer customer = data.getParcelableExtra(RETURN_VALUE_CUSTOMER);

                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout_customer);
                if (fragment instanceof CustomerFragment)
                {
                    ((CustomerFragment)fragment).updateCustomer(customer);
                    MainActivity.databaseCreator.getDatabase().customerDao().insertAll(customer);
                    //MainActivity.databaseCreator.getDatabase().customerDao().getAll();

                }

            }
        }
        if (requestCode == ACTIVITY_RESULT_CODE_SCANNER && resultCode == RESULT_OK)
        {
            String sCode = data.getStringExtra(MAIN_CODE_STRING);
            //  ((TextView)findViewById(R.id.textView_code)).setText(sCode);
            //  String sCode = result.getContents();
            int nCommpany = ParcelConstant.getCommpanyId(sCode);
            MyParcel parcel = new MyParcel();
            parcel.setsParcel2DBar(sCode);
            parcel.setnCommpany(nCommpany);

            Intent intent = new Intent(this,RecordActivity.class);
            intent.putExtra(RecordActivity.PARAM_PARCEL,parcel);
            startActivity(intent);

        }
    }
}
