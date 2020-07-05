package com.inhatc.blockbusters.A;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.inhatc.blockbusters.R;
import com.inhatc.blockbusters.login.LoginActivity;

public class rainshield extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_rainshield_activity);

        new SplashActivity().execute();
    }

    private class SplashActivity extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids){
            try{
                Thread.sleep(1500);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid){
            super.onPostExecute(avoid);

            startActivity(new Intent(rainshield.this, LoginActivity.class));
            finish();
        }
    }


}
