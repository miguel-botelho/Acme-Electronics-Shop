package com.cmov.tomislaaaav.acme_electronics_shop;

/**
 * Created by m_bot on 09/11/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    private EditText signupInputName, signupInputEmail, signupInputPassword, signupInputNIF, signupInputAddress, signupInputNumberCreditCard, signupInputValidityCreditCard;
    private Button btnRegister;
    private RadioGroup typeRadioGroup;

    private RestAPI restAPI = new RestAPI();
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        signupInputName = (EditText) findViewById(R.id.signup_input_name);
        signupInputEmail = (EditText) findViewById(R.id.signup_input_email);
        signupInputPassword = (EditText) findViewById(R.id.signup_input_password);
        signupInputNIF = (EditText) findViewById(R.id.signup_input_nif);
        signupInputAddress = (EditText) findViewById(R.id.signup_input_address);
        signupInputNumberCreditCard = (EditText) findViewById(R.id.signup_input_number_credit_card);
        signupInputValidityCreditCard = (EditText) findViewById(R.id.signup_input_validity_credit_card);

        btnRegister = (Button) findViewById(R.id.btn_register);

        typeRadioGroup = (RadioGroup) findViewById(R.id.type_radio_group);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }

    private void submitForm() {

        int selectedId = typeRadioGroup.getCheckedRadioButtonId();
        String type;
        if(selectedId == R.id.visa_radio_btn)
            type = "Visa";
        else
            type = "Master Card";

        /*
        Pattern p = Pattern.compile("[0-9][0-9]/[0-9][0-9]");
        Matcher m = p.matcher(signupInputValidityCreditCard.getText().toString());

        if (!m.matches()) {
            Toast.makeText(getApplicationContext(), "Credit Card Validity must be mm/yy!", Toast.LENGTH_LONG).show();
        } if (signupInputName.getText().toString().equals("") || signupInputEmail.getText().toString().equals("") ||
                signupInputPassword.getText().toString().equals("") || signupInputNIF.getText().toString().equals("") ||
                signupInputNumberCreditCard.getText().toString().equals("") || signupInputValidityCreditCard.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "You can't have any empty fields!", Toast.LENGTH_LONG).show();
        }
        else {
        */
            registerUser(signupInputName.getText().toString(),
                    signupInputEmail.getText().toString(),
                    signupInputPassword.getText().toString(),
                    signupInputAddress.getText().toString(),
                    signupInputNIF.getText().toString(),
                    signupInputNumberCreditCard.getText().toString(),
                    signupInputValidityCreditCard.getText().toString(),
                    type);
        //}
    }

    private void registerUser(final String name,  final String email, final String password,
                              final String address, final String nif, final String number,
                              final String validity, final String type) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        Log.i(TAG, "ddada");
        progressDialog.setMessage("Registring ...");
        showDialog();

        try {
            Log.i(TAG, "enrr");
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            keyPairGenerator.initialize(368, random);

            // generate a keypair
            KeyPair pair = keyPairGenerator.generateKeyPair();
            PrivateKey priv = pair.getPrivate();
            PublicKey pub = pair.getPublic();

            String[] str = new String[10];
            user = new User(name, address, nif, type, email, validity, number, pub.toString(), priv.toString());
            str[0] = "register";
            str[1] = email;
            str[2] = name;
            str[3] = password;
            str[4] = nif;
            str[5] = address;
            str[6] = type;
            str[7] = number;
            str[8] = validity;
            str[9] = pub.toString();
            new registerAPI().execute(str);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private class registerAPI extends AsyncTask<String, Void, String> {

        private String name = "";

        public registerAPI() {
            super();
        }

        @Override
        protected String doInBackground(String... strings) {
            switch (strings[0]) {
                case "register":
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("email", strings[1]);
                    map.put("name", strings[2]);
                    name = strings[2];
                    map.put("password", strings[3]);
                    map.put("nif", strings[4]);
                    map.put("address", strings[5]);
                    map.put("type", strings[6]);
                    map.put("number", strings[7]);
                    map.put("validity", strings[8]);
                    map.put("publicKey", strings[9]);

                    JSONObject us = new JSONObject(map);
                    return restAPI.registerUser(us);
                default:
                    return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG, s);
            if (s.equals("Error")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            } else {
                user.saveObject(getFilesDir());

                Toast.makeText(getApplicationContext(), "Hi " + name + ", You are successfully Added!", Toast.LENGTH_SHORT).show();

                // Launch login activity
                Intent intent = new Intent(
                        RegisterActivity.this,
                        FrontPage.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        }
    }
}
