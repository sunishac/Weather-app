package com.example.chala.group12_hw08;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener,WeatherAsyncTask.IData {
    Button d;
    RadioButton r1,r2;
    Intent resultIntent;
    String t,nt;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String c=null,cn=null,k=null;
    EditText inputCity,inputCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        setTitle("Preferences");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        c = sharedPreferences.getString("city", "") ;
        cn= sharedPreferences.getString("country", "") ;
        k=sharedPreferences.getString("key", "") ;
        Log.d("demo",""+c+" "+cn);
    }

    SharedPreferences.OnSharedPreferenceChangeListener spChanged = new
            SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                      String key) {

                }
            };

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void topApps(String key) {
        k=key;
        if(key.equals("empty")){
            Log.d("demo",""+key);
            Toast.makeText(this,"City Not Found",Toast.LENGTH_LONG).show();
        }else{
            editor.putString("city",c);
            editor.putString("country",cn);
            editor.putString("key",k);
            Log.d("demo","now k is "+k);
            editor.apply();
            resultIntent.putExtra("aboutTemp","at");
            resultIntent.putExtra("celfah",k);
            setResult(Activity.RESULT_OK, resultIntent);
        }
    }

    @Override
    public void nothing() {
        Toast.makeText(this,"Error in entering city name",Toast.LENGTH_LONG).show();
    }


    public class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            resultIntent = new Intent();

            Preference myprt =  getPreferenceManager().findPreference("temp_unit");
            myprt.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String key = preference.getKey();
                    Log.d("demo"," preference"+key);
                    final Dialog dialog = new Dialog(SettingsActivity.this);
                    dialog.setTitle("Choose Temperature Unit ");

                    LayoutInflater layoutInflater = LayoutInflater.from(SettingsActivity.this);
                    View promptView = layoutInflater.inflate(R.layout.radiobutton_dialog, null);
                    dialog.setContentView(promptView);
                    dialog.setCancelable(false);

                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.gravity = Gravity.CENTER;

                    dialog.getWindow().setAttributes(lp);

                    dialog.show();
                    d=(Button) promptView.findViewById(R.id.done);
                    r1=(RadioButton) promptView.findViewById(R.id.cel);
                    r2=(RadioButton) promptView.findViewById(R.id.fah);

                    d.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Toast.makeText(SettingsActivity.this,"Temperature unit has been changed from °"+nt+ " to °"+t ,Toast.LENGTH_LONG).show();
                        }
                    });

                    r1.setOnClickListener(new View.OnClickListener() {
                        @Override
                            public void onClick(View v) {
                            t="C"; nt="F";
                            editor.putString("temperature",t);
                            editor.apply();
                            resultIntent.putExtra("celfah", "C");
                            setResult(Activity.RESULT_OK, resultIntent);
                            }
                        });
                        r2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                nt="C"; t="F";
                                editor.putString("temperature",t);
                                editor.apply();
                                resultIntent.putExtra("celfah", "F");
                                setResult(Activity.RESULT_OK, resultIntent);
                            }
                        });

                    return true;
                }
            });

            Preference mypre =  getPreferenceManager().findPreference("current_city");
            mypre.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(SettingsActivity.this);
                    AlertDialog.Builder builder1=new AlertDialog.Builder(SettingsActivity.this);
                     inputCity = new EditText(SettingsActivity.this);
                      inputCountry = new EditText(SettingsActivity.this);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);


                    lp.addRule(RelativeLayout.BELOW, inputCity.getId());
                    inputCity.setHint("Enter your city");
                    inputCountry.setLayoutParams(lp);
                    inputCountry.setHint("Enter your Country");
                    LinearLayout l=new LinearLayout(SettingsActivity.this);
                    l.addView(inputCity);
                    l.addView(inputCountry);
                    builder.setTitle("Enter City Details").setCancelable(false)
                            .setPositiveButton("Set",new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which){
                                    if(inputCity.getText().toString().equals(null)){
                                        Toast.makeText(SettingsActivity.this,"Current city is not set",Toast.LENGTH_LONG).show();
                                    }else{
                                        editor.putString("city",inputCity.getText().toString());
                                        editor.putString("country",inputCountry.getText().toString());
                                        editor.commit();
                                        c=inputCity.getText().toString();
                                        cn=inputCountry.getText().toString();
                                        new WeatherAsyncTask(SettingsActivity.this).execute("http://dataservice.accuweather.com/locations/v1/US/search?apikey=IyGin8oMHCdBgCdtuaNymVxPXbi7JSKt&q="+inputCity.getText().toString());
                                    }
                                } })
                            .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which){
                                } });
                    builder1.setTitle("Enter City Details").setCancelable(false)
                            .setPositiveButton("Update",new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which){
                                    if(inputCity.getText().toString().equals(null)){
                                        Toast.makeText(SettingsActivity.this,"Current city is not set",Toast.LENGTH_LONG).show();
                                    }else{
                                        c=inputCity.getText().toString();
                                        cn=inputCountry.getText().toString();
                                        Log.d("demo","first k is "+k+ " c is "+c);
                                        new WeatherAsyncTask(SettingsActivity.this).execute("http://dataservice.accuweather.com/locations/v1/US/search?apikey=IyGin8oMHCdBgCdtuaNymVxPXbi7JSKt&q="+inputCity.getText().toString());
                                    }
                                } })
                            .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which){
                                } });

                    builder.setView(l);
                    builder1.setView(l);
                    final AlertDialog simpleAlert= builder.create();
                    final AlertDialog simpleAlert1= builder1.create();

                    if(c==" "){
                        simpleAlert.show();
                    }else{
                        inputCity.setText(c);
                        inputCountry.setText(cn);
                        simpleAlert1.show();
                    }


                    return true;
                }
            });

        }
    }
}
