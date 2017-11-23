package com.example.chala.group12_hw08;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class cityWeather extends AppCompatActivity implements cityAsyncTask.cityDetails,WeatherAsyncTask.IData,CurrentForecastAsyncTask.forecast,MyRecyclerViewAdapter.sendDataToCityWeather {
    String key,city,country;
    ProgressDialog pDialog;
    ArrayList<City> cty=new ArrayList<City>();
    ArrayList<fore> frcst=new ArrayList<fore>();
    TextView t,h,c1,c2,f,tmp,temp1,d,n;
    ImageView iv1,iv2;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    int noofcols=3;
    String keys;
    DatabaseReference database;
    Intent resultI;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        setTitle("Weather App");
        getSupportActionBar().setIcon(R.drawable.weather1);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        resultI=new Intent();

        t=(TextView) findViewById(R.id.title);
        h=(TextView) findViewById(R.id.headline);
        c1=(TextView) findViewById(R.id.click1);
        c2=(TextView) findViewById(R.id.click2);
        f=(TextView) findViewById(R.id.forecast);
        tmp=(TextView) findViewById(R.id.temp);
        d=(TextView) findViewById(R.id.daytxt);
        n=(TextView) findViewById(R.id.nyttxt);
        iv1=(ImageView) findViewById(R.id.dayImg);
        iv2=(ImageView) findViewById(R.id.nytImg);
        temp1=(TextView) findViewById(R.id.temp1);
        mRecyclerView=(RecyclerView) findViewById(R.id.recyc);
        mRecyclerView.setHasFixedSize(true);

        if(getIntent().getExtras()!=null){
            key=getIntent().getExtras().getString(MainActivity.KEY);
            city=getIntent().getExtras().getString(MainActivity.CKEY);
            country=getIntent().getExtras().getString(MainActivity.CYKEY);
        }

        pDialog=new ProgressDialog(this);
        pDialog.setMessage("Loading Data");
        pDialog.setCancelable(false);
        pDialog.show();
        new cityAsyncTask(this).execute("http://dataservice.accuweather.com/forecasts/v1/daily/5day/"+key+"?apikey=IyGin8oMHCdBgCdtuaNymVxPXbi7JSKt");


        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(cty.get(0).getForecast().get(0).getMobl()));
                startActivity(i);
            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(cty.get(0).getMob()));
                startActivity(i);
            }
        });

    }

    @Override
    public void cdetails(ArrayList<City> key) {
        pDialog.dismiss();
        cty=key;
        frcst=cty.get(0).getForecast();
        t.setText("Daily forecast for "+city+","+country);
        h.setText(cty.get(0).getHeadText());
        f.setText("Forecast on "+cty.get(0).getEdate());

       // mRecyclerView.setLayoutManager(new LinearLayoutManager(this,noofcols, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
      //  mRecyclerView.setLayoutManager(new GridLayoutManager(this, noofcols/*,GridLayoutManager.HORIZONTAL,false */));
        mAdapter = new MyRecyclerViewAdapter(frcst,this);
        mRecyclerView.setAdapter(mAdapter);

        String tr=sharedPreferences.getString("temperature","");
        Log.d("demo","temp in cityWe "+tr);

        if(tr.equals("F")){
            temp1.setVisibility(View.INVISIBLE);
            tmp.setVisibility(View.VISIBLE);
            tmp.setText("Temperatute: "+cty.get(0).getForecast().get(0).getMax()+"°/ "+cty.get(0).getForecast().get(0).getMin()+"°");
            Log.d("demo","temp in cityWe "+tr+ " "+tmp.getText());
        }else if(tr.equals("C")){
            tmp.setText("Temperatute: "+cty.get(0).getForecast().get(0).getMax()+"°/ "+cty.get(0).getForecast().get(0).getMin()+"°");
            temp1.setText("Temperatute: "+cty.get(0).getForecast().get(0).getMaxf()+"°/ "+cty.get(0).getForecast().get(0).getMinf()+"°");
            tmp.setVisibility(View.INVISIBLE);
            temp1.setVisibility(View.VISIBLE);
            Log.d("demo","temp in cityWe "+tr+ " "+tmp.getText());
        } else{
            tmp.setText("Temperatute: "+cty.get(0).getForecast().get(0).getMax()+"°/ "+cty.get(0).getForecast().get(0).getMin()+"°");
            temp1.setText("Temperatute: "+cty.get(0).getForecast().get(0).getMaxf()+"°/ "+cty.get(0).getForecast().get(0).getMinf()+"°");
            tmp.setVisibility(View.INVISIBLE);
            temp1.setVisibility(View.VISIBLE);
        }

        d.setText(cty.get(0).getForecast().get(0).getDay());
        n.setText(cty.get(0).getForecast().get(0).getNyt());
        int n1=cty.get(0).getForecast().get(0).getDay_icon();
        String nt=Integer.toString(n1);
        if(n1==0||n1==1||n1==2||n1==3||n1==4||n1==5||n1==6||n1==7||n1==8||n1==9){
            nt="0"+nt;
        }

        Picasso.with(this)
                .load("http://developer.accuweather.com/sites/default/files/"+nt+"-s.png")
                .into(iv1);
        int n2=cty.get(0).getForecast().get(0).getNyt_icon();
        String nt1=Integer.toString(n2);
        if(n2==0||n2==1||n2==2||n2==3||n2==4||n2==5||n2==6||n2==7||n2==8||n2==9){
            nt1="0"+nt1;
        }
        Picasso.with(this)
                .load("http://developer.accuweather.com/sites/default/files/"+nt1+"-s.png")
                .into(iv2);
    }

    @Override
    public void nocity() {
        Toast.makeText(this,"City not Found",Toast.LENGTH_LONG).show();
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cityweathermenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.savecity:
                new CurrentForecastAsyncTask(cityWeather.this).execute("http://dataservice.accuweather.com/currentconditions/v1/"+key+"?apikey=IyGin8oMHCdBgCdtuaNymVxPXbi7JSKt");
                return true;
            case R.id.setascurrentcity:
                new WeatherAsyncTask(cityWeather.this).execute("http://dataservice.accuweather.com/locations/v1/US/search?apikey=IyGin8oMHCdBgCdtuaNymVxPXbi7JSKt&q="+city);
                return true;
            case R.id.citysettings:
                Intent i=new Intent(this, SettingsActivity.class);
                startActivityForResult(i,100);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100) {
            if (resultCode == Activity.RESULT_OK) {
                String returnValue = data.getStringExtra("celfah");
                if(returnValue.equals("C")){
                    tmp.setVisibility(View.INVISIBLE);
                    temp1.setVisibility(View.VISIBLE);
                    temp1.setText("Temperatute: "+cty.get(0).getForecast().get(0).getMaxf()+"°/ "+cty.get(0).getForecast().get(0).getMinf()+"°");
                    resultI.putExtra("aboutTemp","tempar");
                    resultI.putExtra("keyreturned","kr");
                    setResult(Activity.RESULT_OK, resultI);
                }else if(returnValue.equals("F")){
                    temp1.setVisibility(View.INVISIBLE);
                    tmp.setVisibility(View.VISIBLE);
                    tmp.setText("Temperatute: "+cty.get(0).getForecast().get(0).getMax()+"°/ "+cty.get(0).getForecast().get(0).getMin()+"°");
                    resultI.putExtra("aboutTemp","tempar");
                    resultI.putExtra("keyreturned","kr");
                    setResult(Activity.RESULT_OK, resultI);
                } else {
                    Log.d("demo", "return value is " + returnValue);
                    resultI.putExtra("aboutTemp","at");
                    resultI.putExtra("keyreturned", returnValue);
                    setResult(Activity.RESULT_OK, resultI);
                }
            }
        }
    }


    @Override
    public void sendDataToCityActivity(fore fore) {
        final fore fl=fore;

        cty.get(0).getForecast().get(0).setMaxf(fl.getMaxf());
        cty.get(0).getForecast().get(0).setMax(fl.getMax());
        cty.get(0).getForecast().get(0).setMinf(fl.getMinf());
        cty.get(0).getForecast().get(0).setMin(fl.getMin());

        Log.d("demo",""+fl);
        f.setText("Forecast on "+fore.getDate());

        String tr=sharedPreferences.getString("temperature","");
        Log.d("demo","temp in cityWe "+tr);

        if(tr.equals("F")){
            temp1.setVisibility(View.INVISIBLE);
            tmp.setVisibility(View.VISIBLE);
            tmp.setText("Temperature: "+fore.getMax()+"°/ "+fore.getMin()+"°");
            Log.d("demo","temp in cityWe "+tr+ " "+tmp.getText());
        }else if(tr.equals("C")){
            tmp.setText("Temperature: "+fore.getMax()+"°/ "+fore.getMin()+"°");
            temp1.setText("Temperature: "+fore.getMaxf()+"°/ "+fore.getMinf()+"°");
            tmp.setVisibility(View.INVISIBLE);
            temp1.setVisibility(View.VISIBLE);
            Log.d("demo","temp in cityWe "+tr+ " "+tmp.getText());
        } else{
            tmp.setText("Temperature: "+fore.getMax()+"°/ "+fore.getMin()+"°");
            temp1.setText("Temperature: "+fore.getMaxf()+"°/ "+fore.getMinf()+"°");
            tmp.setVisibility(View.INVISIBLE);
            temp1.setVisibility(View.VISIBLE);
        }

        d.setText(fore.getDay());
        n.setText(fore.getNyt());
        int n1=fore.getDay_icon();
        String nt=Integer.toString(n1);
        if(n1==0||n1==1||n1==2||n1==3||n1==4||n1==5||n1==6||n1==7||n1==8||n1==9){
            nt="0"+nt;
        }
        Picasso.with(this)
                .load("http://developer.accuweather.com/sites/default/files/"+nt+"-s.png")
                .into(iv1);
        int n2=fore.getNyt_icon();
        String nt1=Integer.toString(n2);
        if(n2==0||n2==1||n2==2||n2==3||n2==4||n2==5||n2==6||n2==7||n2==8||n2==9){
            nt1="0"+nt1;
        }

        Log.d("demo","url "+"http://developer.accuweather.com/sites/default/files/"+nt1+"-s.png");
        Picasso.with(this)
                .load("http://developer.accuweather.com/sites/default/files/"+n2+"-s.png")
                .into(iv2);

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(fl.getMobl()));
                startActivity(i);
            }
        });
    }

    @Override
    public void currentFore(ArrayList<Weather> ky) {
        final ArrayList<Weather> kyst=ky;
        Log.d("demo","entered...");

        database = FirebaseDatabase.getInstance().getReference();
        //database.child("weather").child("temperature").child(key).setValue(ky.get(0).getMetric());
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child("weather").hasChild(key)) {
                    database.child("weather").child(key).child("temperature").setValue(kyst.get(0).getMetric());
                    Toast.makeText(cityWeather.this,"City Updated",Toast.LENGTH_LONG).show();
                }else{
                    database.child("weather").child(key).child("citykey").setValue(key);
                    database.child("weather").child(key).child("cityname").setValue(city);
                    database.child("weather").child(key).child("temperature").setValue(kyst.get(0).getMetric());
                    database.child("weather").child(key).child("country").setValue(country);
                    database.child("weather").child(key).child("favorite").setValue("FALSE");
                    database.child("weather").child(key).child("localObservationTime").setValue(kyst.get(0).getLocalObservationDateTime());

                    Toast.makeText(cityWeather.this,"City Saved",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void topApps(String key) {
        keys=key;
        String citysp="kjhsg";
        citysp=sharedPreferences.getString("key","");
        Log.d("demo","dskghrl"+citysp+" key "+keys);
        if(citysp.equals(keys)){
            editor.putString("city",city);
            editor.putString("country",country);
            editor.putString("key",keys);
            Log.d("demo","cityweather  "+keys);
            editor.apply();
            resultI.putExtra("keyreturned", keys);
            Toast.makeText(this,"Current city updated",Toast.LENGTH_LONG).show();
        }else{
            editor.putString("city",city);
            editor.putString("country",country);
            editor.putString("key",keys);
            Log.d("demo","cityweather k is "+keys);
            editor.apply();
            resultI.putExtra("keyreturned", keys);
            Toast.makeText(this,"Current city saved",Toast.LENGTH_LONG).show();
        }

        setResult(Activity.RESULT_OK, resultI);
    }

    @Override
    public void nothing() {
        //Toast.makeText(this,"Error in entering city name therefore current city not set",Toast.LENGTH_LONG).show();
    }
}
