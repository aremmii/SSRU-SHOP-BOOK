package ssru.aremmii.ssrushopbook;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Request;
import org.json.JSONArray;
import org.json.JSONObject;

public class ProductListView extends AppCompatActivity {
    //Explicit
    private TextView nameTextView, surnameTextView, moneyTextView;
    private ListView listView;
    private String[] loginStrings, nameStrings, priceStrings,
            coverStrings, eBookStrings, e;
    private String urlJSON = "http://swiftcodingthai.com/ssru/get_product.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_view);
        //Bind Widget
        nameTextView = (TextView) findViewById(R.id.textView7);
        surnameTextView = (TextView) findViewById(R.id.textView8);
        moneyTextView = (TextView) findViewById(R.id.textView9);
        listView = (ListView) findViewById(R.id.listView);
        //Receive Value From Intent
        loginStrings = getIntent().getStringArrayExtra("Login");
        //Show View
        nameTextView.setText(loginStrings[1]);
        surnameTextView.setText(loginStrings[2]);
        moneyTextView.setText(loginStrings[5] + " THB.");
        SynchronizeProduct synchronizeProduct = new SynchronizeProduct(this, urlJSON);
        synchronizeProduct.execute();
    }   // Main Method
    private class SynchronizeProduct extends AsyncTask<Void, Void, String> {
        private Context context;
        private String urlString;
        private ProgressDialog progressDialog;
        public SynchronizeProduct(Context context,
                                  String urlString) {
            this.context = context;
            this.urlString = urlString;
        } //Constructor
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(context, "Load Product",
                    "Load Product Process . . . .");
        }   // onPre
        @Override
        protected String doInBackground(Void... params) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                com.squareup.okhttp.Request.Builder builder = new com.squareup.okhttp.Request.Builder();
                com.squareup.okhttp.Request request = builder.url(urlString).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                Log.d("1JuneV1", "doIn e == " + e.toString());
            }
            return null;
        } //Do In Back
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.length() != 0) {
                progressDialog.dismiss();
                Log.d("1JuneV1", "s ==> " + s);
            }
            try {
                JSONArray jsonArray = new JSONArray(s);
                nameStrings = new String[jsonArray.length()];
                priceStrings = new String[jsonArray.length()];
                coverStrings = new String[jsonArray.length()];
                eBookStrings = new String[jsonArray.length()];
                for (int i=0 ; i<jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    nameStrings[i] = jsonObject.getString("Name");
                    priceStrings[i] = jsonObject.getString("Price");
                    coverStrings[i] = jsonObject.getString("Cover");
                    eBookStrings[i] = jsonObject.getString("Ebook");
                } //for
                final MyAdapter myAdapter = new MyAdapter(context, nameStrings,
                        priceStrings, coverStrings);
                listView.setAdapter(myAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                        if (checkMoney(priceStrings[i])) {
                            confirmDialog(nameStrings[i], priceStrings[i]);
                        } else {
                            MyAlert myAlert = new MyAlert();
                            myAlert.myDialog(context, "เงินไม่พอ", "กรุณาเลือกเล่มใหม่");
                        }
                    }   //OnitemClick
                });
            } catch (Exception e) {
                Log.d("1JuneV2", "onPost e ==>  " + e.toString());
            }
        }   //onPost
        private boolean checkMoney(String pricString) {
            int intMyMoney = Integer.parseInt(loginStrings[5]);
            int intPrice = Integer.parseInt(pricString);
            if (intMyMoney >= intPrice) {
                return true;
            } else {
                return false;
            }
        }
    } //Syn Class
    private void confirmDialog(String nameString, String priceString) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setIcon(R.drawable.icon_myaccount);
        builder.setCancelable(false);
        builder.setTitle("Confirm Order");
        builder.setMessage(nameString + " ราคา " + priceStrings + " THB . " + "\n" + "จริง ๆ หรือ ? ");
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }   //confirm



}   // Main Class