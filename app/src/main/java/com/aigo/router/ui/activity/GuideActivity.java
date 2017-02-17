package com.aigo.router.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aigo.router.R;
import com.aigo.router.bussiness.db.SPManager;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.aigo.router.R.id.relative_layout_understand;

public class GuideActivity extends AppCompatActivity {

    @Bind(R.id.iv_tip_guide)
    ImageView ivTipGuide;
    @Bind(R.id.btn_understand)
    ImageView btnUnderstand;
    @Bind(relative_layout_understand)
    RelativeLayout rlUnderstand;

    private int clickNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        btnUnderstand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlUnderstand.setVisibility(View.GONE);
                clickNum ++;
                if(clickNum==3){

                    SPManager.getInstance().setIsGuide("guide");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }else if(clickNum == 1){
                    ivTipGuide.setImageResource(R.drawable.drw_1_tip_guide_2);
                }else {

                    rlUnderstand.setVisibility(View.VISIBLE);
                    btnUnderstand.setVisibility(View.GONE);
                    ivTipGuide.setImageResource(R.drawable.drw_1_tip_guide_3);
                }
            }
        });

        rlUnderstand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
