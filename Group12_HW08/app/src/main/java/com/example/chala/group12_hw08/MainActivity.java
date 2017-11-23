package com.example.chala.group12_hw08;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements WeatherAsyncTask.IData,CurrentForecastAsyncTask.forecast,SearchAsyncTask.IDat {
    Button current;
    RelativeLayout r1,r2;
    ProgressBar pb;
    String city,country,ct,cy;
    TextView t1,t2,t3,t4,tv1,tv2,tv3;
    EditText ec,ecy;
    ImageView iv;
    ArrayList<Weather> wr;
    ArrayList<Weather> weat;
    Button search;
    String acKey=null,aky=null;
    final static String KEY="key";
    final static String CKEY="city_key";
    final static String CYKEY="country_key";
    int rec=0;
    ArrayList<String> cityf;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    DatabaseReference database;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //variables for retrieving data
    ArrayList<String> keys;
    ArrayList cityNames;
    ArrayList<String> cityCountries;
    ArrayList<String> cityFavourites;
    ArrayList<String> cityTemperature;
    ArrayList<String> cityLocalObservationTime;
    long counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Weather App");
        getSupportActionBar().setIcon(R.drawable.weather1);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        cityf=new ArrayList<String>();
        cityNames = new ArrayList<>();
        cityCountries = new ArrayList<String>();
        cityFavourites = new ArrayList<String>();
        cityTemperature = new ArrayList<String>();
        cityLocalObservationTime = new ArrayList<String>();

        mRecyclerView=(RecyclerView) findViewById(R.id.recycle_saved);
        search=(Button) findViewById(R.id.citySearch);
        ec=(EditText) findViewById(R.id.cityText);
        ecy=(EditText) findViewById(R.id.countryText);
        t1=(TextView) findViewById(R.id.cityCon);
        t2=(TextView) findViewById(R.id.weatext);
        t3=(TextView) findViewById(R.id.temp);
        t4=(TextView) findViewById(R.id.update);
        iv=(ImageView) findViewById(R.id.imageView);
        r1=(RelativeLayout) findViewById(R.id.rl1);
        r2=(RelativeLayout) findViewById(R.id.rl2);
        r2.setVisibility(View.INVISIBLE);
        pb=(ProgressBar) findViewById(R.id.pb);
        pb.setVisibility(View.INVISIBLE);
        current= (Button) findViewById(R.id.setCurrentCity);
        mRecyclerView.setVisibility(View.INVISIBLE);
        weat=new ArrayList<Weather>();
        wr=null;

        tv1=(TextView) findViewById(R.id.nocitiesText);
        tv2=(TextView) findViewById(R.id.nocities2);
        tv3=(TextView) findViewById(R.id.saved);

        keys=new ArrayList<String>();

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("weather");
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("demo","entered cityglobal");
                Log.d("demo","children count"+dataSnapshot.getChildrenCount());
                HashMap<String,String> new2;
                new2 = (HashMap<String, String>) dataSnapshot.getValue();
                if(new2!=null) {
                    Log.d("demo", "size of map" + new2.size()+"new2 is "+ new2);
                }
                counter=dataSnapshot.getChildrenCount();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ArrayList<String> keyset = new ArrayList<String>();
                    String key1 = child.getValue().toString();
                    keyset.add(key1);
                    keys.add(key1);
                    Log.d("demo","keyset " +key1);
                    Weather weather = new Weather();
                    Log.d("demo","datasnapshot "+child.getValue().toString()+" citykey "+child.child("citykey").getValue());
                    weather.setCity(child.child("cityname").getValue().toString());
                    weather.setCountry(child.child("country").getValue().toString());
                    weather.setFav(child.child("favorite").getValue().toString());
                    weather.setMetric(child.child("temperature").getValue().toString());
                    weather.setLocalObservationDateTime((String) child.child("localObservationTime").getValue());
                    weather.setKeyfb((String) child.child("citykey").getValue());
                    weat.add(weather);
                    Log.d("demo","size of arraylist"+weat.size()+" weat is "+ weat);
                }

                if(counter>0){
                    Log.d("demo","entered on data change");
                    tv1.setVisibility(View.INVISIBLE);
                    tv2.setVisibility(View.INVISIBLE);
                    tv3.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mLayoutManager = new LinearLayoutManager(MainActivity.this);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new MyAdapter(weat);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mAdapter);
                }else{
                    tv1.setVisibility(View.VISIBLE);
                    tv2.setVisibility(View.VISIBLE);
                    tv3.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        acKey=preferences.getString("key","");

        if(acKey==""){
            current.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                    final EditText inputCity = new EditText(MainActivity.this);
                    final EditText inputCountry = new EditText(MainActivity.this);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);

                    //lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    //inputCity.setLayoutParams(lp);
                    lp.addRule(RelativeLayout.BELOW, inputCity.getId());
                    inputCity.setHint("Enter your City");
                    inputCountry.setLayoutParams(lp);
                    inputCountry.setHint("Enter your Country");
                    LinearLayout l=new LinearLayout(MainActivity.this);
                    l.addView(inputCity);
                    l.addView(inputCountry);
                    builder.setTitle("Enter City Details").setCancelable(false)
                            .setPositiveButton("Set",new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which){
                                    new WeatherAsyncTask(MainActivity.this).execute("http://dataservice.accuweather.com/locations/v1/US/search?apikey=IyGin8oMHCdBgCdtuaNymVxPXbi7JSKt&q="+inputCity.getText());
                                    pb.setVisibility(View.VISIBLE);
                                    city=inputCity.getText().toString();
                                    country=inputCountry.getText().toString();
                                } })
                            .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which){
                                } });

                    builder.setView(l);
                    final AlertDialog simpleAlert= builder.create();
                    simpleAlert.show();
                }
            });
        }else{
            city=preferences.getString("city","");
            country=preferences.getString("country","");
            new CurrentForecastAsyncTask(MainActivity.this).execute("http://dataservice.accuweather.com/currentconditions/v1/"+preferences.getString("key","")+"?apikey=IyGin8oMHCdBgCdtuaNymVxPXbi7JSKt");
        }


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SearchAsyncTask(MainActivity.this).execute("http://dataservice.accuweather.com/locations/v1/US/search?apikey=IyGin8oMHCdBgCdtuaNymVxPXbi7JSKt&q="+ec.getText().toString());
                /*Intent i=new Intent(MainActivity.this,cityWeather.class);
                i.putExtra(KEY,acKey);
                i.putExtra(CKEY,ec.getText().toString());
                i.putExtra(CYKEY,ecy.getText().toString());
                startActivity(i);*/
                rec=1;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
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
        weat.clear();
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("weather");
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("demo","entered cityglobal");
                Log.d("demo","children count"+dataSnapshot.getChildrenCount());
                HashMap<String,String> new2;
                new2 = (HashMap<String, String>) dataSnapshot.getValue();
                if(new2!=null) {
                    Log.d("demo", "size of map" + new2.size()+"new2 is "+ new2);
                }
                counter=dataSnapshot.getChildrenCount();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    ArrayList<String> keyset = new ArrayList<String>();
                    String key1 = child.getValue().toString();
                    keyset.add(key1);
                    keys.add(key1);
                    Log.d("demo","keyset " +key1);
                    Weather weather = new Weather();
                    Log.d("demo","datasnapshot "+child.getValue().toString()+" citykey "+child.child("citykey").getValue());
                    weather.setCity(child.child("cityname").getValue().toString());
                    weather.setCountry(child.child("country").getValue().toString());
                    weather.setFav(child.child("favorite").getValue().toString());
                    weather.setMetric(child.child("temperature").getValue().toString());
                    weather.setLocalObservationDateTime((String) child.child("localObservationTime").getValue());
                    weather.setKeyfb((String) child.child("citykey").getValue());
                    weat.add(weather);
                    Log.d("demo","size of arraylist"+weat.size()+" weat is "+ weat);
                }

                if(counter>0){
                    Log.d("demo","entered on data change");
                    tv1.setVisibility(View.INVISIBLE);
                    tv2.setVisibility(View.INVISIBLE);
                    tv3.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mLayoutManager = new LinearLayoutManager(MainActivity.this);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new MyAdapter(weat);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mAdapter);
                    if(weat.size()==0){
                        tv1.setVisibility(View.VISIBLE);
                        tv2.setVisibility(View.VISIBLE);
                        tv3.setVisibility(View.INVISIBLE);
                        mRecyclerView.setVisibility(View.INVISIBLE);
                    }
                }else{
                    tv1.setVisibility(View.VISIBLE);
                    tv2.setVisibility(View.VISIBLE);
                    tv3.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        if(requestCode==100) {
                if (resultCode == Activity.RESULT_OK) {
                    String returnValue = data.getStringExtra("celfah");
                    if(returnValue.equals("C")){
                        t3.setText("Temperature: "+wr.get(0).getMetric());
                    }else if(returnValue.equals("F")){
                        t3.setText("Temperature: "+wr.get(0).getImp());
                    }else{
                        //asytas=0;
                        Log.d("demo","return value is "+returnValue);
                        if(returnValue.equals(null)){
                            Toast.makeText(this,"No city found",Toast.LENGTH_LONG).show();
                        }else{
                            new CurrentForecastAsyncTask(MainActivity.this).execute("http://dataservice.accuweather.com/currentconditions/v1/"+returnValue+"?apikey=IyGin8oMHCdBgCdtuaNymVxPXbi7JSKt");
                        }
                    }
                }
        }
        if(requestCode==200){
            if(resultCode== Activity.RESULT_OK){

                String tmpr=data.getStringExtra("aboutTemp");
                Log.d("demo","tmpr is "+tmpr);
                //asytas=0;
                if(tmpr!=null){
                    if(tmpr.equals("tempar")){
                        new CurrentForecastAsyncTask(MainActivity.this).execute("http://dataservice.accuweather.com/currentconditions/v1/"+preferences.getString("key","")+"?apikey=IyGin8oMHCdBgCdtuaNymVxPXbi7JSKt");
                    }else if(tmpr.equals("at")){

                    }
                }

                String g=data.getStringExtra("keyreturned");
                Log.d("demo","returnMain value is "+g+" "+tmpr);
                if(g.equals(null)){
                    Toast.makeText(this,"Current City Not Found",Toast.LENGTH_LONG).show();
                } else if(g.equals("kr")){

                }else {
                    new CurrentForecastAsyncTask(MainActivity.this).execute("http://dataservice.accuweather.com/currentconditions/v1/"+g+"?apikey=IyGin8oMHCdBgCdtuaNymVxPXbi7JSKt");
                }

            }
        }
    }

    @Override
    public void topApps(String key) {
        /*acKey=key;*/
        pb.setVisibility(View.GONE);
        if(key.equals("empty")){
            Log.d("demo",""+key);
            Toast.makeText(this,"City Not Found",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Current city details saved",Toast.LENGTH_LONG).show();
            editor.putString("key",key);
            editor.putString("city",city);
            editor.putString("country",country);
            editor.commit();
            //asytas=0;
            new CurrentForecastAsyncTask(MainActivity.this).execute("http://dataservice.accuweather.com/currentconditions/v1/"+key+"?apikey=IyGin8oMHCdBgCdtuaNymVxPXbi7JSKt");
        }
    }

    @Override
    public void nothing() {
        pb.setVisibility(View.GONE);
        Toast.makeText(this,"Error in entering city name",Toast.LENGTH_LONG).show();
    }

    @Override
    public void currentFore(ArrayList<Weather> key) {

            r1.setVisibility(View.INVISIBLE);
            r2.setVisibility(View.VISIBLE);
            wr=key;
            t1.setText(preferences.getString("city","")+","+preferences.getString("country",""));
            t2.setText(key.get(0).getWeatherText().toString());

            String tr=preferences.getString("temperature","");
            Log.d("demo","temp in main "+tr);
            if(tr.equals("F")){
                t3.setText("Temperature: "+key.get(0).getImp());
            }else if(tr.equals("C")){
                t3.setText("Temperature: "+key.get(0).getMetric());
            }else{
                t3.setText("Temperature: "+key.get(0).getMetric());
            }
            //pretty
            String dateString=key.get(0).getLocalObservationDateTime().toString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");

            Date convertedDate = new Date();

            try {
                convertedDate = dateFormat.parse(dateString);
            }  catch (ParseException e) {
                e.printStackTrace();
            }
            PrettyTime p  = new PrettyTime();
            String datetime= p.format(convertedDate);
            t4.setText("Updated "+datetime);

            //t4.setText(key.get(0).getLocalObservationDateTime());
            int n=key.get(0).getWeatherIcon();
            String nt=Integer.toString(n);
            if(n==0||n==1||n==2||n==3||n==4||n==5||n==6||n==7||n==8||n==9){
                nt="0"+nt;
            }
            Picasso.with(this)
                    .load("http://developer.accuweather.com/sites/default/files/"+nt+"-s.png")
                    .into(iv);

        }

    @Override
    public void keyOb(String key) {
        aky=key;
        Log.d("demo","key in search: "+key);
        if(key.equals("empty")){
            Log.d("demo",""+key);
            Toast.makeText(this,"City Not Found",Toast.LENGTH_LONG).show();
        }else{
            //asytas=1;
            ct=ec.getText().toString();
            cy=ecy.getText().toString();
            new CurrentForecastAsyncTask(MainActivity.this).execute("http://dataservice.accuweather.com/currentconditions/v1/"+key+"?apikey=IyGin8oMHCdBgCdtuaNymVxPXbi7JSKt");
            Intent i=new Intent(MainActivity.this,cityWeather.class);
            i.putExtra(KEY,key);
            i.putExtra(CKEY,ec.getText().toString());
            i.putExtra(CYKEY,ecy.getText().toString());
            startActivityForResult(i,200);
        }
    }

    @Override
    public void searchNothing() {
        Toast.makeText(this,"Error in entering city name",Toast.LENGTH_LONG).show();
    }

}
