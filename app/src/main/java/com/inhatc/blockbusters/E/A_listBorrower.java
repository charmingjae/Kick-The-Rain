package com.inhatc.blockbusters.E;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.inhatc.blockbusters.D.A_BorrowActivity;
import com.inhatc.blockbusters.R;
import com.inhatc.blockbusters.login.LoginActivity;

import java.util.ArrayList;

public class A_listBorrower extends AppCompatActivity implements View.OnClickListener{

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
    Button btnSetOverdue;

    ImageView imgGoBorrowAct;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e_a_listborrower);


        CONTEXT = this;


        btnSetOverdue = (Button)findViewById(R.id.btnSetOverdue);
        btnSetOverdue.setOnClickListener(this);

        imgGoBorrowAct = (ImageView)findViewById(R.id.imgGoBr);
        imgGoBorrowAct.setOnClickListener(this);
        btnUmbReturn = (Button)findViewById(R.id.btnUmbReturn);
        btnUmbReturn.setOnClickListener(this);
        lstView = (ListView)findViewById(R.id.lstBorrower);
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] strData = null;
                strData = aryMRBList.get(position).toString().split("\t\t\t\t\t\t");
                lstGetStuNo = strData[0];
                Toast.makeText(A_listBorrower.this, lstGetStuNo, Toast.LENGTH_SHORT).show();
            }
        });

        try{
            // Create DB ( DB Name : UserInfo )
            myDB = this.openOrCreateDatabase("KTR", MODE_PRIVATE, null);
            //            myDB.execSQL("DROP TABLE IF EXISTS tbUmbrella");


            myDB.execSQL("CREATE TABLE IF NOT EXISTS tbOverdue(" +
                    "_id integer primary key autoincrement, " +
                    "nameBorrower text not null, " +
                    "startOverdue DATETIME DEFAULT (datetime('now','localtime')), " +
                    "finishOverdue DATETIME DEFAULT (datetime('now', '+5 days')));");

        }catch(Exception e)
        {
            e.printStackTrace();
        }

        getDBData(null);

        if(myDB != null) myDB.close();
    }

    public void getDBData(String strWhere){

        // Get DB Data from Carlist table
        allRCD = myDB.query("tbBorrow", null, strWhere, null, null, null, "_id desc", null);

        // Create arrayList
        aryMRBList = new ArrayList<String>();
        if(allRCD != null){
            if(allRCD.moveToFirst()){
                do{
                    strRecord = allRCD.getString(1) +  "\t\t\t\t\t\t" + allRCD.getString(2) + "\t\t\t\t\t\t" + allRCD.getString(3).substring(5,10) + "\t\t\t\t\t\t" + allRCD.getString(4).substring(5,10);
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
            if(lstGetStuNo == null)
            {
                Toast.makeText(this, "대상을 선택하세요.", Toast.LENGTH_SHORT).show();
                getDBData(null);
            }
            else{
                // Delete process
                strSQL = "DELETE FROM tbBorrow";
                strSQL += " Where nameBorrower = '" + lstGetStuNo+ "';";
                myDB.execSQL(strSQL);

                // Update Umbrella count
                strSQL = "UPDATE tbUmbrella set umbEtc = umbEtc + 1;";
                myDB.execSQL(strSQL);

                String query = "UPDATE tbRatio set cntReturn = cntReturn + 1;";
                myDB.execSQL(query);

                Toast.makeText(this, lstGetStuNo + "의 우산이 정상적으로 반납되었습니다.", Toast.LENGTH_SHORT).show();
                getDBData(null);
            }
        }
        else if(v == btnSetOverdue)
        {
            if(lstGetStuNo == null)
            {
                Toast.makeText(this, "대상을 선택하세요.", Toast.LENGTH_SHORT).show();
            }
            else{

                if(statusOverdue(lstGetStuNo))
                {
                    Toast.makeText(this, lstGetStuNo+"는 이미 연체중 입니다.",Toast.LENGTH_LONG).show();
                }
                else{
                    String insertOverdue = "INSERT INTO tbOverdue ( nameBorrower ) VALUES ('"+lstGetStuNo+"');";
                    myDB.execSQL(insertOverdue);
                    Toast.makeText(this, lstGetStuNo + "이 연체 처리 되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public boolean statusOverdue(String stuNo){
        strSQL = "SELECT COUNT(*) FROM tbOverdue where nameBorrower = '"+stuNo+"';";
        allRCD = myDB.rawQuery(strSQL, null);
        allRCD.moveToFirst();
        int cntUmb = Integer.parseInt(allRCD.getString(0));
        if(cntUmb >=1)
        {
            return true;
        }
        else{
            return false;
        }
    }
}
