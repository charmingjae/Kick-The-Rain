package com.inhatc.blockbusters.G;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.inhatc.blockbusters.R;

import java.util.ArrayList;

public class A_overdue extends AppCompatActivity implements View.OnClickListener{

    public static Context CONTEXT;



    SQLiteDatabase myDB;
    SimpleAdapter myADT;
    ArrayList<String> aryMRBList;
    ArrayAdapter<String> adtMembers;
    ListView lstView;
    String strRecord = null;
    ContentValues insertValue;
    Cursor allRCD;
    EditText edtCarType, edtCarPower;
    String strSQL = null;

    String lstGetStuNo = null;

    Button btnUmbReturn;
    Button btnFnsOverdue;

    ImageView imgGoBorrowAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_a_overdue);

        CONTEXT = this;

        imgGoBorrowAct = (ImageView)findViewById(R.id.imgGoBr);
        imgGoBorrowAct.setOnClickListener(this);
        btnFnsOverdue = (Button)findViewById(R.id.btnFnsOverdue);
        btnFnsOverdue.setOnClickListener(this);
        lstView = (ListView)findViewById(R.id.lstOverdue);
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] strData = null;
                strData = aryMRBList.get(position).toString().split("\t\t\t\t\t\t\t\t\t\t\t");
                lstGetStuNo = strData[0];
                Toast.makeText(A_overdue.this, lstGetStuNo, Toast.LENGTH_SHORT).show();
            }
        });

        myDB = this.openOrCreateDatabase("KTR", MODE_PRIVATE, null);

        getDBData(null);

        if(myDB != null) myDB.close();
    }

    public void getDBData(String strWhere){

        // Get DB Data from Carlist table
        allRCD = myDB.query("tbOverdue", null, strWhere, null, null, null, "_id desc", null);

        // Create arrayList
        aryMRBList = new ArrayList<String>();
        if(allRCD != null){
            if(allRCD.moveToFirst()){
                do{
                    strRecord = allRCD.getString(1) +  "\t\t\t\t\t\t\t\t\t\t\t" + allRCD.getString(2).substring(5,10) + "\t\t\t\t\t\t\t\t\t\t\t" + allRCD.getString(3).substring(5,10);
                    aryMRBList.add(strRecord);
                }while(allRCD.moveToNext());
            }
        }
        adtMembers = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, aryMRBList);

        // Create ListView
        lstView.setAdapter(adtMembers);
        lstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onClick(View v)
    {
        myDB = this.openOrCreateDatabase("KTR", MODE_PRIVATE, null);
        if(v == btnUmbReturn)
        {
            if (lstGetStuNo == null)
            {
                Toast.makeText(this, "대상을 선택하세요.", Toast.LENGTH_SHORT).show();
            }
            else{
                // Delete process
                strSQL = "DELETE FROM tbBorrow";
                strSQL += " Where nameBorrower = '" + lstGetStuNo+ "';";
                myDB.execSQL(strSQL);

                // Update Umbrella count
                strSQL = "UPDATE tbUmbrella set umbEtc = umbEtc + 1;";
                myDB.execSQL(strSQL);

                Toast.makeText(this, lstGetStuNo + "의 우산이 정상적으로 반납되었습니다.", Toast.LENGTH_SHORT).show();
                getDBData(null);
            }
        }
        else if(v == btnFnsOverdue)
        {
            if(lstGetStuNo == null)
            {
                Toast.makeText(this, "대상을 선택하세요.", Toast.LENGTH_SHORT).show();
            }
            else{
                strSQL = "DELETE FROM tbOverdue";
                strSQL += " Where nameBorrower = '" + lstGetStuNo + "';";
                myDB.execSQL(strSQL);

                Toast.makeText(this, lstGetStuNo + "의 연체 종료 완료", Toast.LENGTH_SHORT).show();
                getDBData(null);
            }
        }
    }
}
