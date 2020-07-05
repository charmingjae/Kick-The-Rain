package com.inhatc.blockbusters.MyInfo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.inhatc.blockbusters.D.A_BorrowActivity;
import com.inhatc.blockbusters.F.A_adminMenu;
import com.inhatc.blockbusters.R;

public class A_MyInfo extends AppCompatActivity implements View.OnClickListener{

    // Database
    SQLiteDatabase myDB;
    ContentValues insertValue = new ContentValues();
    Cursor allRCD;
    int intRecord;
    TextView txtShowBorrowDate;
    TextView txtShowReturnDate;
    Button btnUmbReturn;
    RelativeLayout layNotBorrowUmb;
    TextView flagShowReturnDate;
    TextView flagShowBorrowDate;
    TextView txtNotBorrowUmb;

    String getStuNo;
    String getBorrowDate;
    String getReturnDate;



    Button btnActBrost;

    int selItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo_a_myinfo);

        layNotBorrowUmb = (RelativeLayout)findViewById(R.id.layNotBorrowUmb);
        txtNotBorrowUmb = (TextView)findViewById(R.id.txtNotBorrowUmb);

        txtShowBorrowDate = (TextView)findViewById(R.id.txtShowBorrowDate);
        txtShowReturnDate = (TextView)findViewById(R.id.txtShowReturnDate);

        flagShowBorrowDate = (TextView)findViewById(R.id.flagShowBorrowDate);
        flagShowReturnDate = (TextView)findViewById(R.id.flagShowReturnDate);

        btnActBrost = (Button)findViewById(R.id.btnActBrost);
        btnActBrost.setOnClickListener(this);

        btnUmbReturn = (Button)findViewById(R.id.btnUmbReturn);
        btnUmbReturn.setOnClickListener(this);

        Intent intent = getIntent();
        getStuNo = intent.getExtras().getString("stuno");

        // Create DB ( DB Name : UserInfo )
        myDB = this.openOrCreateDatabase("KTR", MODE_PRIVATE, null);

        String queryRaw = "SELECT * from tbBorrow where nameBorrower = '" + getStuNo + "';";
        allRCD = myDB.rawQuery(queryRaw, null);

        if(allRCD != null && allRCD.moveToFirst()){
            allRCD.moveToFirst();

            getBorrowDate = allRCD.getString(3).substring(5, 10);
            getReturnDate = allRCD.getString(4).substring(5, 10);

            layNotBorrowUmb.setVisibility(View.GONE);
            txtNotBorrowUmb.setVisibility(View.GONE);

            txtShowReturnDate.setVisibility(View.VISIBLE);
            txtShowBorrowDate.setVisibility(View.VISIBLE);
            flagShowReturnDate.setVisibility(View.VISIBLE);
            flagShowBorrowDate.setVisibility(View.VISIBLE);

            txtShowBorrowDate.setText(getBorrowDate);
            txtShowReturnDate.setText(getReturnDate);

            btnUmbReturn.setVisibility(View.GONE);
            btnActBrost.setVisibility(View.VISIBLE);
        }
        else{
            layNotBorrowUmb.setVisibility(View.VISIBLE);
            txtNotBorrowUmb.setVisibility(View.VISIBLE);

            txtShowReturnDate.setVisibility(View.GONE);
            txtShowBorrowDate.setVisibility(View.GONE);
            flagShowReturnDate.setVisibility(View.GONE);
            flagShowBorrowDate.setVisibility(View.GONE);


            btnUmbReturn.setVisibility(View.VISIBLE);
            btnActBrost.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v){
        myDB = this.openOrCreateDatabase("KTR", MODE_PRIVATE, null);
        final CharSequence[] oItems = {"고장", "분실"};
        if(v == btnActBrost){
            // 고장 / 분실 신고 버튼 눌렀을 때 고장 , 분실 중 선택하게 dialog 생성

            String queryRaw = "SELECT * from tbBorrow where nameBorrower = '" + getStuNo + "';";
            allRCD = myDB.rawQuery(queryRaw, null);
            if(allRCD != null && allRCD.moveToFirst()){
                AlertDialog.Builder oDialog = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

                oDialog.setTitle("유형을 선택하세요.")
                        .setSingleChoiceItems(oItems, 0, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                selItem = which;
                            }
                        })
                        .setNeutralButton("신고하기", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                String query = "UPDATE tbBorrow set status = '" + oItems[selItem] + "' where nameBorrower = '" + getStuNo+ "';";
                                myDB.execSQL(query);
                                Toast.makeText(A_MyInfo.this, oItems[selItem].toString()+"처리가 완료되었습니다.", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setCancelable(true)
                        .show();
            }
            else{
                Toast.makeText(A_MyInfo.this, "빌린 우산이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v == btnUmbReturn){
            onBackPressed();
        }
    }
}
