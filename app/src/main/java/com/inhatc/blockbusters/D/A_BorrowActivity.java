package com.inhatc.blockbusters.D;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.inhatc.blockbusters.E.A_listBorrower;
import com.inhatc.blockbusters.F.A_adminMenu;
import com.inhatc.blockbusters.MyInfo.A_MyInfo;
import com.inhatc.blockbusters.OverdueInfo.A_OverdueInfo;
import com.inhatc.blockbusters.R;

import java.util.ArrayList;

public class A_BorrowActivity extends AppCompatActivity implements View.OnClickListener{


    // Database
    SQLiteDatabase myDB;
    ContentValues insertValue = new ContentValues();
    Cursor allRCD;
    int intRecord;
    TextView chkAdmin;
    Button btnBorrow;
    Button btnGoAdminMenu;
    Button btnMyInfo;
//    Intent intent = getIntent();
//    String getStuNo = intent.getExtras().getString("stuno");
    String getStuNo;



    private TextView numUmb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_a_borrowactivity);

        btnMyInfo = (Button)findViewById(R.id.btnMyInfo);
        btnMyInfo.setOnClickListener(this);
        chkAdmin = (TextView)findViewById(R.id.chkAdmin);
        numUmb = (TextView)findViewById(R.id.numUmb);
        btnBorrow = (Button)findViewById(R.id.btnUmbReturn);
        btnBorrow.setOnClickListener(this);
        btnGoAdminMenu = (Button)findViewById(R.id.btnGoAdminMenu);
        btnGoAdminMenu.setOnClickListener(this);

        Intent intent = getIntent();

        getStuNo = intent.getExtras().getString("stuno");

        if(getStuNo.equals("000000000") || getStuNo.equals("0")){
            chkAdmin.setVisibility(View.VISIBLE);
            btnGoAdminMenu.setVisibility(View.VISIBLE);
            btnMyInfo.setVisibility(View.GONE);
        }
        else{
            btnMyInfo.setVisibility(View.VISIBLE);
            chkAdmin.setVisibility(View.GONE);
            btnGoAdminMenu.setVisibility(View.INVISIBLE);
        }


            try{
            // Create DB ( DB Name : UserInfo )
                myDB = this.openOrCreateDatabase("KTR", MODE_PRIVATE, null);
    //            myDB.execSQL("DROP TABLE IF EXISTS tbUmbrella");

                myDB.execSQL("CREATE TABLE IF NOT EXISTS tbUmbrella(" +
                        "umbName text primary key, " +
                        "umbEtc integer not null);");

//                myDB.execSQL("DROP TABLE IF EXISTS tbBorrow");


                myDB.execSQL("CREATE TABLE IF NOT EXISTS tbBorrow(" +
                        "_id integer primary key autoincrement, " +
                        "nameBorrower text not null, " +
                        "status text DEFAULT '정상', " +
                        "startDate DATETIME DEFAULT (datetime('now','localtime')), " +
                        "returnDate DATETIME DEFAULT (datetime('now', '+5 days')));");


            String queryRaw = "SELECT * from tbUmbrella;";
            allRCD = myDB.rawQuery(queryRaw, null);
            allRCD.moveToFirst();
            intRecord = allRCD.getInt(1);
//            Toast.makeText(this, "우산 개수 : " + intRecord, Toast.LENGTH_SHORT).show();
            numUmb.setText(String.valueOf(intRecord));

        }catch(Exception e)
        {

            e.printStackTrace();
        }
    }

    @Override
    public void onResume(){
        super.onResume();


        numUmb = (TextView)findViewById(R.id.numUmb);
        btnBorrow = (Button)findViewById(R.id.btnUmbReturn);
        btnBorrow.setOnClickListener(this);
        btnGoAdminMenu = (Button)findViewById(R.id.btnGoAdminMenu);
        btnGoAdminMenu.setOnClickListener(this);

        Intent intent = getIntent();

        getStuNo = intent.getExtras().getString("stuno");


        try{
            // Create DB ( DB Name : UserInfo )
            myDB = this.openOrCreateDatabase("KTR", MODE_PRIVATE, null);
            //            myDB.execSQL("DROP TABLE IF EXISTS tbUmbrella");

            myDB.execSQL("CREATE TABLE IF NOT EXISTS tbUmbrella(" +
                    "umbName text primary key, " +
                    "umbEtc integer not null);");


            myDB.execSQL("CREATE TABLE IF NOT EXISTS tbBorrow(" +
                    "_id integer primary key autoincrement, " +
                    "nameBorrower text not null, " +
                    "status text DEFAULT '정상', " +
                    "startDate DATETIME DEFAULT (datetime('now','localtime')), " +
                    "returnDate DATETIME DEFAULT (datetime('now', '+5 days')));");

            String queryRaw = "SELECT * from tbUmbrella;";
            allRCD = myDB.rawQuery(queryRaw, null);
            allRCD.moveToFirst();
            intRecord = allRCD.getInt(1);
            numUmb.setText(String.valueOf(intRecord));

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v)
    {
        myDB = this.openOrCreateDatabase("KTR", MODE_PRIVATE, null);
        if(v==btnBorrow)
        {

            // 우산이 1개 미만인지 아닌지 검사
            int getUmbEtc = getUmbCount();

            if(getUmbEtc == 0){
                Toast.makeText(this,"현재 빌릴 수 있는 우산이 없습니다.",Toast.LENGTH_LONG).show();
            }
            else{
                // tbOverdue에 자신의 정보가 있는지 없는지 검사
                // 검사 후 자신의 학번이 tbOverdue에 있다면 빌리지 못하고 자신의 연체 정보를 볼 수 있는 화면으로 넘어간다.

                if(getOverdueDBData(getStuNo))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(A_BorrowActivity.this);
                    builder.setTitle("알림").setMessage("연체 정보가 있어 대여를 할 수 없습니다.\n연체 정보를 보시겠습니까?").setCancelable(false).
                            setPositiveButton("네",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton){
                                    Intent intent = new Intent(A_BorrowActivity.this, A_OverdueInfo.class);
                                    intent.putExtra("stuno",getStuNo);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("아니요", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int whichButton){
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(A_BorrowActivity.this);
                    builder.setTitle("앗! 갑자기 비가오네..").setMessage("우산 쓰고 가실래요?").setCancelable(false).
                            setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String queryRaw = "SELECT Count(*) FROM tbBorrow where nameBorrower = '"+getStuNo+"';";
                                    allRCD = myDB.rawQuery(queryRaw, null);
                                    allRCD.moveToFirst();
                                    intRecord = allRCD.getInt(0);
                                    if(intRecord >= 1)
                                    {
                                        Toast.makeText(A_BorrowActivity.this, "이미 우산을 빌리셨어요! 😭", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        insertValue = new ContentValues();
                                        insertValue.put("nameBorrower", getStuNo);
                                        myDB.insert("tbBorrow", null, insertValue);
                                        Toast.makeText(A_BorrowActivity.this, "정상적으로 처리되었습니다. 😍",Toast.LENGTH_LONG).show();
                                        String umbUpdate = "UPDATE tbUmbrella set umbetc = umbetc -1";
                                        myDB.execSQL(umbUpdate);
                                        String getUmbCount = "SELECT * from tbUmbrella;";
                                        allRCD = myDB.rawQuery(getUmbCount, null);
                                        allRCD.moveToFirst();
                                        intRecord = allRCD.getInt(1);
                                        numUmb.setText(String.valueOf(intRecord));
                                        String query = "UPDATE tbRatio set cntBorrow = cntBorrow + 1;";
                                        myDB.execSQL(query);

                                    }
                                }
                            })
                            .setNegativeButton("아니요", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int whichButton){
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        }
        else if(v == btnGoAdminMenu)
        {
            Intent intent = new Intent(this, A_adminMenu.class);
            startActivity(intent);
        }
        else if(v == btnMyInfo)
        {
            Intent intent = new Intent(this, A_MyInfo.class);
            intent.putExtra("stuno",getStuNo);
            startActivity(intent);
        }

    }

    public boolean getOverdueDBData(String stuNo){

        String query = "SELECT COUNT(*) FROM tbOverdue where nameborrower = '" + stuNo + "';";
        boolean flag = true;

        allRCD = myDB.rawQuery(query, null);

        allRCD.moveToFirst();

        if(Integer.parseInt(allRCD.getString(0)) >= 1){
            flag = true;
        }
        else{
            flag = false;
        }
        return flag;
    }

    public int getUmbCount(){
        int cntUmb = 9999;
        String query = "SELECT umbEtc FROM tbUmbrella;";
        allRCD = myDB.rawQuery(query, null);
        allRCD.moveToFirst();
        cntUmb = Integer.parseInt(allRCD.getString(0));
        if(cntUmb==0){
            return 0;
        }
        else{
            return 1;
        }
    }
}
