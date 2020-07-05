package com.inhatc.blockbusters.OverdueInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.inhatc.blockbusters.R;

public class A_OverdueInfo extends AppCompatActivity implements View.OnClickListener{

    // Database
    SQLiteDatabase myDB;
    ContentValues insertValue = new ContentValues();
    Cursor allRCD;
    int intRecord;
    TextView txtShowOverdueStart;
    TextView txtShowOverdueFinish;
    TextView flagShowOverdueStart;
    TextView flagShowOverdueFinish;

    String getStuNo;
    String getBorrowDate;
    String getReturnDate;



    Button btnUmbBorrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overdueinfo_a_overdueinfo);

        txtShowOverdueStart = (TextView)findViewById(R.id.txtShowOverdueStart);
        txtShowOverdueFinish = (TextView)findViewById(R.id.txtShowOverdueFinish);

        flagShowOverdueStart = (TextView)findViewById(R.id.flagShowOverdueStart);
        flagShowOverdueFinish = (TextView)findViewById(R.id.flagShowOverdueFinish);

        btnUmbBorrow = (Button)findViewById(R.id.btnUmbBorrow);
        btnUmbBorrow.setOnClickListener(this);

        Intent intent = getIntent();
        getStuNo = intent.getExtras().getString("stuno");

        // Create DB ( DB Name : UserInfo )
        myDB = this.openOrCreateDatabase("KTR", MODE_PRIVATE, null);

        String queryRaw = "SELECT * from tbOverdue where nameBorrower = '" + getStuNo + "';";
        allRCD = myDB.rawQuery(queryRaw, null);

        allRCD.moveToFirst();

        getBorrowDate = allRCD.getString(2).substring(5, 10);
        getReturnDate = allRCD.getString(3).substring(5, 10);


        txtShowOverdueFinish.setVisibility(View.VISIBLE);
        txtShowOverdueStart.setVisibility(View.VISIBLE);
        flagShowOverdueStart.setVisibility(View.VISIBLE);
        flagShowOverdueFinish.setVisibility(View.VISIBLE);

        txtShowOverdueStart.setText(getBorrowDate);
        txtShowOverdueFinish.setText(getReturnDate);

    }

    @Override
    public void onClick(View v){
        if(v == btnUmbBorrow){
            onBackPressed();
        }
    }
}
