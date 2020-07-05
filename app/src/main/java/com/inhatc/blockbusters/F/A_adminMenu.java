package com.inhatc.blockbusters.F;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.inhatc.blockbusters.E.A_listBorrower;
import com.inhatc.blockbusters.G.A_overdue;
import com.inhatc.blockbusters.GetRatio.A_getRatio;
import com.inhatc.blockbusters.R;
import com.inhatc.blockbusters.SetCount.A_SetUmbCount;


public class A_adminMenu extends AppCompatActivity implements View.OnClickListener{

    Button btnGolst, btnLstOverdue, btnSetUmbCnt, btnShowRatio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_a_adminmenu);

        btnGolst = (Button)findViewById(R.id.btnGolst);
        btnGolst.setOnClickListener(this);

        btnLstOverdue = (Button)findViewById(R.id.btnLstOverdue);
        btnLstOverdue.setOnClickListener(this);

        btnSetUmbCnt = (Button)findViewById(R.id.btnSetUmbCnt);
        btnSetUmbCnt.setOnClickListener(this);

        btnShowRatio = (Button)findViewById(R.id.btnShowRatio);
        btnShowRatio.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if(v == btnGolst)
        {
            Intent intent = new Intent(this, A_listBorrower.class);
            startActivity(intent);
        }
        else if(v == btnLstOverdue)
        {
            Intent intent = new Intent(this, A_overdue.class);
            startActivity(intent);
        }
        else if(v == btnSetUmbCnt)
        {
            Intent intent = new Intent( this, A_SetUmbCount.class);
            startActivity(intent);
        }
        else if(v == btnShowRatio)
        {
            Intent intent = new Intent(this, A_getRatio.class);
            startActivity(intent);
        }
    }
}
