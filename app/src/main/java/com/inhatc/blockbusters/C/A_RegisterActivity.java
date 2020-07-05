package com.inhatc.blockbusters.C;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.inhatc.blockbusters.R;

import java.util.ArrayList;

public class A_RegisterActivity extends AppCompatActivity implements View.OnClickListener{


    // Database
    SQLiteDatabase myDB;
    ContentValues insertValue = new ContentValues();
    Cursor allRCD;
    String strSQL = null;
    ArrayList<String> aryMBRList;
    ArrayAdapter<String> adtMembers;
    ListView lstView;
    String strRecord = null;

    // UI Component List
    ImageView imgGoBack;
    ImageView imgCancelStuNo;
    ImageView imgCancelPassword;

    EditText regStuNo;
    EditText regPassword;
    Button btnRegister;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_a_registeractivity);


        imgGoBack = (ImageView)findViewById(R.id.imgGoBr);
        imgGoBack.setOnClickListener(this);
        imgCancelStuNo = (ImageView)findViewById(R.id.imgCancelStuNo);
        imgCancelStuNo.setOnClickListener(this);
        imgCancelPassword = (ImageView)findViewById(R.id.imgCancelPassword);
        imgCancelPassword.setOnClickListener(this);
        regStuNo = (EditText)findViewById(R.id.regStuNo);
        regPassword = (EditText)findViewById(R.id.regPassword);
        btnRegister = (Button)findViewById(R.id.btnUmbReturn);
        btnRegister.setOnClickListener(this);

        // Create DB ( DB Name : UserInfo )
        myDB = this.openOrCreateDatabase("KTR", MODE_PRIVATE, null);
//        myDB.execSQL("DROP TABLE IF EXISTS UserInfo");

        // Create Table ( Table name : UserInfo);
        myDB.execSQL("CREATE TABLE IF NOT EXISTS UserInfo(" +
                "stuno text primary key, " +
                "password text not null);");

        // Get Record Data from members table
        Cursor allRCD = myDB.query("UserInfo", null, null, null, null, null, null, null);

        // Create ArrayList
        aryMBRList = new ArrayList<String>();
        if(allRCD != null){
            if(allRCD.moveToFirst()){
                do{
                    strRecord = allRCD.getString(0) + "\t\t" +allRCD.getString(1);
                    aryMBRList.add(strRecord);
                }while(allRCD.moveToNext());
            }
        }

        adtMembers = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, aryMBRList);

        // Create ListView
        lstView = (ListView)findViewById(R.id.lstMember);
        lstView.setAdapter(adtMembers);
        lstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        if(myDB != null) myDB.close(); // Close DB
    }

    @Override
    public void onClick(View v){

        myDB = this.openOrCreateDatabase("KTR", MODE_PRIVATE, null);
        if(v == imgCancelStuNo){
            regStuNo.setText("");
        }
        else if(v == imgCancelPassword){
            regPassword.setText("");
        }
        else if(v == imgGoBack){
            onBackPressed();
        }
        else if(v == btnRegister){
                String getStuNo = regStuNo.getText().toString();
                String getPassword = regPassword.getText().toString();

                if(chkUserInfo(getStuNo)==1)
                {
                    Toast.makeText(this.getApplicationContext(), "이미 가입된 아이디입니다." , Toast.LENGTH_LONG).show();
                }
                else{
                    insertValue = new ContentValues();
                    insertValue.put("stuno", getStuNo);
                    insertValue.put("password", getPassword);

                    // Insert Data into UserInfo table
                    myDB.insert("UserInfo", null, insertValue);

                    Toast.makeText(this.getApplicationContext(), "회원가입 완료." , Toast.LENGTH_LONG).show();
                }

                getDBData(null);
        }
        if(myDB != null) myDB.close();
    }

    public void getDBData(String strWhere){
        // Get DB Data from UserInfo table
        allRCD = myDB.query("UserInfo", null, strWhere, null, null, null, null, null);

        // Create arrayList
        aryMBRList = new ArrayList<String>();
        if(allRCD != null){
            if(allRCD.moveToFirst()){
                do{
                    strRecord = allRCD.getString(0) +  "\t\t" + allRCD.getString(1);
                    aryMBRList.add(strRecord);
                }while(allRCD.moveToNext());
            }
        }
        adtMembers = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, aryMBRList);

        // Create ListView
        lstView.setAdapter(adtMembers);
        lstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }


    public int chkUserInfo(String stuNo){
        String strSQL = "SELECT COUNT(*) FROM userInfo where stuno = '" + stuNo + "';";
        allRCD = myDB.rawQuery(strSQL, null);
        allRCD.moveToFirst();
        int cntResult = allRCD.getInt(0);
        if(cntResult >0){
            return 1;
        }
        else{
            return 0;
        }

    }

}
