package com.cmov.tomislaaaav.acme_electronics_shop;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static android.content.ContentValues.TAG;

/**
 * Created by m_bot on 09/11/2017.
 */

public class RestAPI {

    private final String url_api = "http://c0dab501.ngrok.io/";

    public RestAPI() {
    }

    public String createConnection (String urlS, String methodInvoked, String patchBody, String postBody, String putBody){
        URL url;
        BufferedReader br = null;
        String toBeReturned="";
        String error = "";
        try {
            Log.i(TAG, urlS);
            url = new URL(urlS);
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override

                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            Log.i(TAG, urlS);
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                            return myTrustedAnchors;
                        }
                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }
                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            Log.i(TAG, urlS);
            // Create an SSLContext that uses our TrustManager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, null);


            Log.i(TAG, urlS);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);

            Log.i(TAG, urlS);


            if (patchBody  != null ){
                Log.i(TAG, " createConnection with PATH with body" );
                connection.setRequestMethod("PATCH");
                connection.setRequestProperty("data",patchBody);
                connection.addRequestProperty("Content-Type", "application/json");
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(patchBody);
                dStream.flush();
                dStream.close();
            }
            if (methodInvoked.equalsIgnoreCase("PATCH") && patchBody == null ){
                Log.i(TAG, " createConnection with PATH without body" );
                connection.setRequestMethod("PATCH");
//              connection.addRequestProperty("Content-Type", "application/json");
//              connection.setDoOutput(true);
            }
            if (postBody != null){
                Log.i(TAG, " createConnection with POST with body" );
                connection.setRequestMethod("POST");
                connection.addRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(postBody);
                dStream.flush();
                dStream.close();
            }

            if (methodInvoked.equalsIgnoreCase("POST") && postBody == null ){
                Log.i(TAG, " createConnection with POST without body" );
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //connection.addRequestProperty("Content-Type", "application/json");
            }

            if (putBody != null){
                Log.i(TAG, " createConnection with PUT with body" );
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);
                connection.addRequestProperty("Content-Type", "application/json");
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(putBody);
                dStream.flush();
                dStream.close();
            }


            int responseCode = connection.getResponseCode();
            InputStream in= null;
            if(responseCode >= HttpsURLConnection.HTTP_BAD_REQUEST)
            {

                in = connection.getErrorStream();
                br = new BufferedReader( new InputStreamReader(connection.getErrorStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                String toBeReturned_1 = sb.toString();
                Log.i(TAG, " createConnetion error received " +  responseCode  + "  " + toBeReturned_1) ;

            }
            else{
                br = new BufferedReader( new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                toBeReturned = sb.toString();
            }

        } catch (MalformedURLException e) {
            error = e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            error = e.getMessage();
            e.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            try {
                if (br!=null)
                    br.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        Log.i(TAG, " createConnetion  finally returned " +  toBeReturned );
        return toBeReturned;
    }

    public String getProductByID(String id) {
        String response = createConnection(url_api + "product/" + id, "GET", null, null, null);
        return response;
    }

    public String getAllProducts() {
        String response = createConnection(url_api + "product/", "GET", null, null, null);
        return response;
    }

    public String getCartByUser(String id) {
        String response = createConnection(url_api + "cart/" + id, "GET", null, null, null);
        return response;
    }

    public String addProductToCart(String idUser, String idProduct) {
        String response = createConnection(url_api + "cart/add/" + idUser  + "/" + idProduct, "GET", null, null, null);
        return response;
    }

    public String removeProductFromCart(String idUser, String idProduct) {
        String response = createConnection(url_api + "cart/remove/" + idUser  + "/" + idProduct, "GET", null, null, null);
        return response;
    }

    public String addQuantityOfProductToCart(String idUser, String idProduct, String quantity) {
        String response = createConnection(url_api + "cart/add/" + idUser  + "/" + idProduct + "/" + quantity, "GET", null, null, null);
        return response;
    }

    public String removeQuantityOfProductFromCart(String idUser, String idProduct, String quantity) {
        String response = createConnection(url_api + "cart/remove/" + idUser  + "/" + idProduct + "/" + quantity, "GET", null, null, null);
        return response;
    }

    public String retrieveOrderByUUID(String uuid) {
        String response = createConnection(url_api + "order/" + uuid, "GET", null, null, null);
        return response;
    }

    public String retrievePrinterByUUID(String uuid) {
        String response = createConnection(url_api + "order/printer/" + uuid, "GET", null, null, null);
        return response;
    }

    public String retrievePreviousOrdersByID(String id) {
        String response = createConnection(url_api + "order/previous/" + id, "GET", null, null, null);
        return response;
    }

    public String registerUser(JSONObject us) {
        String response = createConnection(url_api+"register/", "POST", null, us.toString(), null);
        return response;
    }

}
