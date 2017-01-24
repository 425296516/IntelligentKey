package com.aigo.router.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.aigo.router.R;
import com.aigo.router.bussiness.db.SPManager;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GuideActivity extends AppCompatActivity {

    @Bind(R.id.iv_tip_guide)
    ImageView ivTipGuide;
    @Bind(R.id.btn_understand)
    Button btnUnderstand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        btnUnderstand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onBackPressed();
                SPManager.getInstance().setIsGuide("guide");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {


    }
}
