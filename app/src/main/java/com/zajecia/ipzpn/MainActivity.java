package com.zajecia.ipzpn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    Button p1, p2;
    TextView tekst;
    CheckBox boks;
    EditText tekst_id;
    EditText tekst_name;
    String data="";
    ProgressBar progres;
    private android.os.Handler mHandl= new android.os.Handler();
        int id=1;
    ListView lista;
    ArrayList<HashMap<String, String>>  listaDanych = new ArrayList<HashMap<String, String>>();
    private SimpleAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        p1 = findViewById(R.id.p1);
        p2 = (Button) findViewById(R.id.p2);
        tekst = findViewById(R.id.textView);
        boks = findViewById(R.id.checkBox);
        tekst_id = findViewById(R.id.editTextNumber);
        progres = (ProgressBar) findViewById(R.id.progressBar);
        lista = findViewById(R.id.liastOfData);
        tekst_name = findViewById(R.id.ePerson);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),"Click on "+ String.valueOf(i),Toast.LENGTH_LONG).show();

                final AsyncUpdate asyncTask = new AsyncUpdate();
                asyncTask.execute("tekst");
            }
        });

        p2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "To jest toast!!!",Toast.LENGTH_LONG).show();
                sendLista();
                //sendR2();
               // sendR(tekst_id.getText().toString());
            }
        });

        p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("INFO","jestesmy przed if");

                try {
                    sendAddNew(tekst_name.getText().toString(), tekst_id.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (boks.isChecked())
               {
                   tekst.setText("Zaznaczony");
                    Log.d("INFO","jestesmy w zaznaczony");
               }
               else
               {
                   tekst.setText("Nie");
                   Log.d("INFO","jestesmy w  false zaznaczony");
               }

            }
        });


    }





    private  void sendLista() // zestaw wyników  - tabela
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Log.d("INFO", "przed odpowiedzi");
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, "https://ipz.pythonanywhere.com/", null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    // String data="";
                     HashMap<String,String> item ;
                    Log.d("INFO", "w odpowiedzi");
                    for(int i=0;i<response.length();i++){
                        JSONObject dane = response.getJSONObject(i);
                        //data= data + dane.getString("full_name")+"    " + dane.getString("created_at")+"\n";
                        item = new HashMap<String, String>();
                        item.put("line_a", dane.getString("full_name"));
                        item.put("line_b",  dane.getString("created_at"));
                        listaDanych.add(item);
                    }

                    sa = new SimpleAdapter(getApplicationContext(), listaDanych, R.layout.twolines, new String[] {"line_a", "line_b"},
                            new int[] {R.id.line_a, R.id.line_b});

                    lista.setAdapter(sa);
                    //((ListView))findViewById(R.id.liastOfData).setAdapter(sa);
                    //daneG=data;
                    //mHandl.postDelayed(addProgres,100);
                    //tekst.setText(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // findViewById(R.id.progressBar).setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //txtShowTextResult.setText("An Error occured while making the request");
                Log.d("INFO bład", error.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);

    }


    private  void sendR2() // zestaw wyników  - tabela
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, "https://ipz.pythonanywhere.com/", null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                   // String data="";
                   // HashMap<String,String> item;
                    for(int i=0;i<response.length();i++){
                        JSONObject dane = response.getJSONObject(i);
                        data= data + dane.getString("full_name")+"    " + dane.getString("created_at")+"\n";

                    }
                    //daneG=data;
                    //mHandl.postDelayed(addProgres,100);
                    tekst.setText(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // findViewById(R.id.progressBar).setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //txtShowTextResult.setText("An Error occured while making the request");
                Log.d("bład", error.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    private  void sendR(String id) // zestaw wyników  - tabela
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://ipz.pythonanywhere.com/"+id, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {


                        data= data + response.getString("full_name")+"    " + response.getString("created_at")+"\n";


                    mHandl.postDelayed(addProgres,100);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // findViewById(R.id.progressBar).setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //txtShowTextResult.setText("An Error occured while making the request");
                Log.d("bład", error.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);

    }



    private Runnable addProgres= new Runnable() {
        @Override
        public void run() {
            tekst.setText(data);

            progres.setProgress(id*10);
            id++;
           // progre.setProgress(progresV.intValue());

        }
    };



    private void sendAddNew(String name, String id) throws JSONException// jeden wynik
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //    JSONObject parameters = new JSONObject();
        final Map<String, String> params = new HashMap();

        params.put("full_name", name);
        params.put("country_code", id);

        JSONObject parameters = new JSONObject(params);


        String url= "https://ipz.pythonanywhere.com/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                    Log.d("bład", response.toString());
                  //  t1.setText(response.getString("full_name")+ "  " +response.getString("created_at")+ "  " +response.getString("country_code"));



                // findViewById(R.id.progressBar).setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //txtShowTextResult.setText("An Error occured while making the request");
                Log.d("bład", error.toString());
                //Log.d("bład", error.getMessage());

            }
        })   {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };

        requestQueue.add(jsonObjectRequest);
        //Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);


    }


    class AsyncUpdate extends AsyncTask<String, String, String>
    {


        @Override
        protected String doInBackground(String... strings) {


            for (int i =1; i<10;i++)
            {

                tekst.setText("Licznik "+i);
                try {
                    sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }




            return null;
        }
    }

}