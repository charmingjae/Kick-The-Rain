package com.inhatc.blockbusters.GetRatio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.inhatc.blockbusters.R;

public class A_getRatio extends AppCompatActivity implements View.OnClickListener {

    TextView numRetCnt, showStatus;

    Button btnUmbReturn;

    SQLiteDatabase myDB;
    ContentValues insertValue = new ContentValues();
    Cursor allRCD;
    int cntBorrow, cntReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getratio_a_getratio);

        numRetCnt = (TextView)findViewById(R.id.numRetCnt);
        showStatus = (TextView)findViewById(R.id.showStatus);

        btnUmbReturn = (Button)findViewById(R.id.btnUmbReturn);
        btnUmbReturn.setOnClickListener(this);

        try{
            // Create DB ( DB Name : UserInfo )
            myDB = this.openOrCreateDatabase("KTR", MODE_PRIVATE, null);
//          myDB.execSQL("DROP TABLE IF EXISTS tbRatio");


            myDB.execSQL("CREATE TABLE IF NOT EXISTS tbRatio(" +
                    "_id integer primary key autoincrement, " +
                    "cntBorrow integer DEFAULT 0, " +
                    "cntReturn integer DEFAULT 0);");

            String queryRaw = "SELECT * from tbRatio;";
            allRCD = myDB.rawQuery(queryRaw, null);
            allRCD.moveToFirst();
            cntBorrow = allRCD.getInt(1);
            cntReturn = allRCD.getInt(2);
//            Toast.makeText(this, cntBorrow + " " + cntReturn, Toast.LENGTH_SHORT).show();
            if(cntBorrow == 0){
                numRetCnt.setText("0퍼");
            }
            else{
                double ratio = ((double)cntReturn/(double)cntBorrow)*100.0;
                String strRatio;
                if(ratio == 100)
                {
                    strRatio = String.format("%.0f",ratio);
                }
                else{
                    strRatio = String.format("%.1f",ratio);
                }

                numRetCnt.setText(strRatio+"%");

                float flRatio = Float.parseFloat(strRatio);

                if(flRatio >=80)
                {
                    showStatus.setText("매우 좋습니다 😍");
                    numRetCnt.setTextColor(Color.parseColor("#008000"));
                }
                else if(flRatio >=40 && flRatio<80)
                {
                    showStatus.setText("양호합니다 🥴");
                    numRetCnt.setTextColor(Color.parseColor("#ff7f00"));
                }
                else
                {
                    showStatus.setText("매우 안 좋습니다.🥵");
                    numRetCnt.setTextColor(Color.parseColor("#ff0000"));
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v)
    {
        if(v == btnUmbReturn)
        {
            onBackPressed();
        }
    }
}
