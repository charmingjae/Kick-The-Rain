package com.inhatc.blockbusters.login;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.inhatc.blockbusters.C.A_RegisterActivity;
import com.inhatc.blockbusters.D.A_BorrowActivity;
import com.inhatc.blockbusters.R;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    // Database
    SQLiteDatabase myDB;
    ContentValues insertValue = new ContentValues();
    Cursor allRCD;
    String strSQL = null;
    ArrayList<String> aryMBRList;
    ArrayAdapter<String> adtMembers;
    ListView lstView;
    String strRecord = null;

    private EditText editText_stuno;
    private EditText editText_password;
    private Button btn_login;
    private TextView textView_signup;
    private ImageView btn_password;
    private int images [] = {R.drawable.ic_visibility_off_black_24dp, R.drawable.ic_beach_access_black_24dp};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_loginactivity);

        editText_stuno = findViewById(R.id.editText_stuno);
        editText_password = findViewById(R.id.editText_password);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        textView_signup = findViewById(R.id.textView_signup);
        btn_password = findViewById(R.id.btn_password);
        btn_password.setOnClickListener(this);

        editText_password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        btn_password.setImageResource(images[1]);

        textView_signup.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        myDB = this.openOrCreateDatabase("KTR", MODE_PRIVATE, null);
        if(v==btn_password){
            if(editText_password.getInputType() == ((InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD))){
                editText_password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btn_password.setImageResource(images[0]);
            }
            else if(editText_password.getInputType() == ((InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD))){
                editText_password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btn_password.setImageResource(images[1]);
            }
        }
        else if(v==textView_signup)
        {
            Intent intent = new Intent(this, A_RegisterActivity.class);
            startActivity(intent);
        }
        else if(v==btn_login)
        {
            strSQL = "stuno = '" + editText_stuno.getText().toString() + "' and password = '" + editText_password.getText().toString() + "'";
            allRCD = myDB.query("UserInfo", null, strSQL, null, null, null, null, null);
            if(allRCD.getCount() != 1)
            {
                Toast toast = Toast.makeText(LoginActivity.this, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            else{
                Intent intent = new Intent(this, A_BorrowActivity.class);
                intent.putExtra("stuno",editText_stuno.getText().toString());
                startActivity(intent);
            }
        }
    }
}
