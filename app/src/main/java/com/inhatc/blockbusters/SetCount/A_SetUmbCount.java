package com.inhatc.blockbusters.SetCount;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.inhatc.blockbusters.D.A_BorrowActivity;
import com.inhatc.blockbusters.R;

public class A_SetUmbCount extends AppCompatActivity implements View.OnClickListener{

    EditText edtUmbCnt;
    Button btnAcptUmbCnt;

    int getUmbCount;

    SQLiteDatabase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setcount_a_setumbcount);

        edtUmbCnt = (EditText)findViewById(R.id.edtUmbCnt);
        btnAcptUmbCnt = (Button)findViewById(R.id.btnAcptUmbCnt);
        btnAcptUmbCnt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        myDB = this.openOrCreateDatabase("KTR", MODE_PRIVATE, null);
        if(v == btnAcptUmbCnt)
        {
            getUmbCount = Integer.parseInt(edtUmbCnt.getText().toString());
            AlertDialog.Builder builder = new AlertDialog.Builder(A_SetUmbCount.this);
            builder.setTitle("확인!").setMessage("우산 개수가 " + getUmbCount + "개가 맞습니까?").setCancelable(false).
                    setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String query = "UPDATE tbUmbrella set umbEtc = " + getUmbCount + ";";
                            myDB.execSQL(query);
                            Toast.makeText(A_SetUmbCount.this, "우산 개수가 "+getUmbCount+"개로 변경되었습니다.‼️", Toast.LENGTH_SHORT).show();
                            edtUmbCnt.setText("");

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
