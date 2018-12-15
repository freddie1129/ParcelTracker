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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.fredroid.parceltracking.api.ParcelApiClient;
import com.fredroid.parceltracking.api.ParcelService;
import com.fredroid.parceltracking.db.entity.MyParcel;
import com.fredroid.parceltracking.zxing.client.android.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class RecordActivity extends AppCompatActivity implements
RecordFragment.OnListFragmentInteractionListener{

    private static final String TAG = "RecordActivity";

    public static String PARAM_PARCEL = "enquire-parcel";
    public MyParcel myParcel;


    @BindView(R.id.frameLayout_record)
    FrameLayout mFormView;

    @BindView(R.id.progressdBar_download)
    ProgressBar progressBar;

    public class TaskQuery extends TimerTask{
        @Override
        public void run() {
            if (myParcel != null) {
                String sCode = myParcel.getsParcel2DBar();
                if (ParcelConstant.getCommpanyId(sCode) == ParcelConstant.COMMPANY_other) {



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder;
                            String sInfo = getString(R.string.dialog_info_invalid);
                            String [] commpany = getResources().getStringArray(R.array.commpany_array);
                            String sCommpanyList = "\n";

                            for (int i = 0; i < commpany.length; i++) {
                                sCommpanyList += "  " +  (i + 1) + "." +  commpany[i] + "\n";
                            }
                            sInfo += sCommpanyList;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(RecordActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(RecordActivity.this);
                            }
                            builder.setMessage(sInfo)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();

                                        }
                                    })
                                    .show();
                            // finish();

                        }
                    });
                    return;

                }

            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showProgress(true);
                }
            });

            TaskTracking taskTracking = new TaskTracking(myParcel.getsParcel2DBar(),myParcel.getnCommpany());
            taskTracking.execute();
        }
    }

    TaskQuery task = new TaskQuery();/* {
        @Override
        public void run() {
            if (myParcel != null) {
                String sCode = myParcel.getsParcel2DBar();
                if (ParcelConstant.getCommpanyId(sCode) == ParcelConstant.COMMPANY_other) {



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder;
                            String sInfo = getString(R.string.dialog_info_invalid);
                            String [] commpany = getResources().getStringArray(R.array.commpany_array);
                            String sCommpanyList = "\n";

                            for (int i = 0; i < commpany.length; i++) {
                                sCommpanyList += "  " +  (i + 1) + "." +  commpany[i] + "\n";
                            }
                            sInfo += sCommpanyList;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(RecordActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(RecordActivity.this);
                            }
                            builder.setMessage(sInfo)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();

                                        }
                                    })
                                    .show();
                           // finish();

                        }
                    });
                    return;

                }

            }

            showProgress(true);
            TaskTracking taskTracking = new TaskTracking(myParcel.getsParcel2DBar(),myParcel.getnCommpany());
            taskTracking.execute();
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        String sTitle = getString(R.string.activity_record_title);
        if (null != getIntent())
        {
            myParcel = getIntent().getParcelableExtra(PARAM_PARCEL);
            if (myParcel != null)
            {
                sTitle = myParcel.getsParcel2DBar();
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(sTitle);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent = new Intent(RecordActivity.this, CaptureActivity.class);
                startActivityForResult(intent, MainActivity.ACTIVITY_RESULT_CODE_SCANNER);
            }
        });

        Timer timer = new Timer();
        timer.schedule(task, 20);




        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode ==  MainActivity.ACTIVITY_RESULT_CODE_SCANNER && resultCode == RESULT_OK)
        {
            String sCode = data.getStringExtra(MainActivity.MAIN_CODE_STRING);
            //  ((TextView)findViewById(R.id.textView_code)).setText(sCode);
            //  String sCode = result.getContents();
            int nCommpany = ParcelConstant.getCommpanyId(sCode);
            myParcel = new MyParcel();
            //MyParcel parcel = new MyParcel();
            myParcel.setsParcel2DBar(sCode);
            myParcel.setnCommpany(nCommpany);

            if (nCommpany == ParcelConstant.COMMPANY_other) {




                        AlertDialog.Builder builder;
                        String sInfo = getString(R.string.dialog_info_invalid);
                        String[] commpany = getResources().getStringArray(R.array.commpany_array);
                        String sCommpanyList = "\n";

                        for (int i = 0; i < commpany.length; i++) {
                            sCommpanyList += "  " + (i + 1) + "." + commpany[i] + "\n";
                        }
                        sInfo += sCommpanyList;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(RecordActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(RecordActivity.this);
                        }
                        builder.setMessage(sInfo)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();

                                    }
                                })
                                .show();

                    }

            showProgress(true);
            TaskTracking taskTracking = new TaskTracking(myParcel.getsParcel2DBar(),myParcel.getnCommpany());
            taskTracking.execute();


           // TaskQuery task = new TaskQuery();
           // Timer timer = new Timer();
           // timer.schedule(task, 20);

           // Intent intent = new Intent(this,RecordActivity.class);
           // intent.putExtra(RecordActivity.PARAM_PARCEL,parcel);
           // startActivity(intent);


        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            // floatingActionButton.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            // floatingActionButton.setVisibility(show ? View.GONE : View.VISIBLE);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
       // return super.onSupportNavigateUp();
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    private class TaskTracking extends AsyncTask<Void, Void, ArrayList<String>> {


        private int nCommpany;
        private String sCode;

        TaskTracking(String sCode, int nCommpany)
        {
            this.nCommpany =  nCommpany; //ParcelConstant.getCommpanyId(sCode);
            this.sCode = sCode;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> recordList = new ArrayList<String>();
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
//                    }catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    break;
//                case ParcelConstant.COMMPANY_freaky_quick:
//                    try {
//
//                        doc = Jsoup.connect("http://www.freakyquick.com.au/chaxun.php")
//                                .data("numid", sCode)
//                                .post();
//                        Elements elements  = doc.select(".i-middle1 table tr");
//                        for (int i = 1; i < elements.size();i++)
//                        {
//                            String s = "\n";
//                            Element e = elements.get(i);
//                            Log.d(TAG, "doInBackground: ***************");
//                            Elements info = e.getElementsByTag("td"); // .select("td");
//                            for (Element item:info)
//                            {
//                                s += item.html().replaceAll("&nbsp;","") + "\n";
//
//                            }
//                            recordList.add(s);
//                        }
//                    }catch (IOException e) {
//                        e.printStackTrace();
//                    }
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
                    builder = new AlertDialog.Builder(RecordActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(RecordActivity.this);
                }
                builder.setTitle(RecordActivity.this.getString(R.string.dialog_title_error))
                        .setMessage(RecordActivity.this.getString(R.string.dialog_info_failure))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                return;
            }



            //floatingActionButton.setVisibility(View.GONE);
            RecordFragment recordFragment = RecordFragment.newInstance(result);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_record, recordFragment).addToBackStack(null).commit();

        }
    }

    @Override
    public void onListFragmentInteractionRecordEwe(String item) {

    }



    public class TimerStart extends TimerTask{
        @Override
        public void run() {

        }
    }
}
