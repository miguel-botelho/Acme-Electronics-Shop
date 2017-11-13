package com.cmov.tomislaaaav.acmeelectronicsshopprinter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by m_bot on 13/11/2017.
 */

public class XZingActivity extends AppCompatActivity{

    RestAPI restAPI = new RestAPI();
    String re = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentIntegrator integrator = new IntentIntegrator(XZingActivity.this);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            re = scanResult.getContents();
            Log.i("code", re);
            String[] strs = new String[2];
            strs[0] = "getOrderById";
            strs[1] = re;
            new XZingActivity.printerAPI().execute(strs);
        }
        // else continue with any other code you need in the method
    }

    public class printerAPI extends AsyncTask<String, Void, String> {

        public printerAPI() {
            super();
        }

        @Override
        protected String doInBackground(String... strings) {
            switch (strings[0]) {
                case "getOrderById":
                    return restAPI.retrievePrinterByUUID(strings[1]);
                default:
                    return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG, s);
            if (s.equals("\"Error\"")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            } else {
                JSONObject obj = null;
                ArrayList<Product> products = new ArrayList<Product>();
                User u = new User();
                Order o = new Order();
                try {
                    obj = new JSONObject(s);
                    o.setId(re);

                    String[] dates = obj.getString("day").split("-");
                    Date date = new Date(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]));
                    o.setDate(date);

                    u.setId(obj.getString("idUser"));
                    u.setAddress(obj.getString("address"));
                    u.setEmail(obj.getString("email"));
                    u.setName(obj.getString("name"));
                    u.setNIF(obj.getString("nif"));

                    JSONArray arr = obj.getJSONArray("products");

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj1 = arr.getJSONObject(i);
                        Product p = new Product(obj1.getInt("idProduct"), obj1.getString("maker"), obj1.getString("model"), obj1.getInt("price"), obj1.getString("description"), obj1.getInt("quantity"));
                        products.add(p);
                    }
                    Log.i(TAG, products.toString());
                    o.setProducts(products);

                    Intent intent = new Intent(
                            XZingActivity.this,
                            Printer.class);
                    intent.putExtra("order", o);
                    intent.putExtra("user", u);
                    startActivity(intent);
                    finish();
                } catch (JSONException ex) {
                    // edited, to include @Arthur's comment
                    // e.g. in case JSONArray is valid as well...
                }
            }
        }
    }
}
