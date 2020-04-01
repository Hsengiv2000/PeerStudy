package com.example.peerstudy;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


class BackgroundWorker extends AsyncTask<String, String, String> {
    Context context;
    String responseString;
    public  BackgroundWorker(Context ct){context = ct;}
    @Override
    protected String doInBackground(String... params) {
  /*
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        */
        String name = params[0];
        Log.i("jinsdf", "fsgdfg");

        try {
            /*
            URL url = new URL(myurl);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
          // httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode(myurl,"UTF-8");
            Log.i("POSTDATA", post_data);
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            Log.i("hero","h");

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i("hero","g");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("hero","i");
        }
        return new String("Done");*/

            Log.i("works i guess","i guess");
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.start();
            String url = name;

// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                          responseString = response;
                       //   MainActivity.roomcode = response;
                            // Display the first 500 characters of the response string.
                            Log.i("hehehehehehe", "Responsed is: " + response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("the error is " , error.getMessage());
                    Log.i("here", "only");
                }
            });
            queue.add(stringRequest);

// Add the request to the RequestQueue.

        } catch (Exception e) {

            Log.i("fff", "d");
        }

        return responseString;
    }
}
