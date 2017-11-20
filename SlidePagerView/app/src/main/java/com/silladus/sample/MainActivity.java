package com.silladus.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.silladus.slidepagerview.SlidePagerView;

public class MainActivity extends AppCompatActivity {
    private int[] btnBgArr = new int[]{
            R.drawable.shape_btn_authentication_1,
            R.drawable.shape_btn_authentication_2,
            R.drawable.shape_btn_authentication_3,
            R.drawable.shape_btn_authentication_4
    };
    private String[] titleArr = new String[]{
            "身份信息（必填）",
            "个人信息（必填）",
            "手机认证（必填）",
            "芝麻信用（必填）"
    };
    private String[] title2ndArr = new String[]{
            "展现真实的你",
            "让我们更熟悉",
            "方便随时联系",
            "因为信任 所以简单"
    };
    private int[] pageImageContentArr = new int[]{
            R.mipmap.img_identity_information_3x,
            R.mipmap.img_personal_information_3x,
            R.mipmap.img_text_message_3x,
            R.mipmap.img_sesame_credit_3x
    };
    private String[] btnNameArr = new String[]{
            "表明身份",
            "人际关系",
            "联系方式",
            "芝麻开门"
    };
    private int[] btnTextColorArr = new int[]{
            0XFF8F8EFF,
            0XFFFFB201,
            0XFF51BA2D,
            0XFF12C3AF
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SlidePagerView root = new SlidePagerView(this);
        root.setBackgroundColor(Color.WHITE);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int margin = screenWidth * 3 / 14;
        for (int i = 0; i < 4; i++) {
            View view = View.inflate(this, R.layout.item_pager_authentication, null);
            Button btnRequest = (Button) view.findViewById(R.id.btn_request);
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
            TextView tvTitle2nd = (TextView) view.findViewById(R.id.tv_title_2nd);
            tvTitle.setText(titleArr[i]);
            tvTitle2nd.setText(title2ndArr[i]);
            btnRequest.setText(btnNameArr[i]);
            btnRequest.setTextColor(btnTextColorArr[i]);
            btnRequest.setBackgroundResource(btnBgArr[i]);
            ImageView imgContent = (ImageView) view.findViewById(R.id.image_content);
            imgContent.setImageResource(pageImageContentArr[i]);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(screenWidth *4 / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER_VERTICAL;
            if (i == 0) {
                lp.setMargins(margin, 0, 0, 0);
            } else if (i == 3) {
                lp.setMargins(margin * 3 / 5, 0, margin, 0);
            } else {
                lp.setMargins(margin * 3 / 5, 0, 0, 0);
            }
            root.addView(view, lp);
        }
        setContentView(root);
        root.setOnSelectListener(new SlidePagerView.OnSelectListener() {
            @Override
            public void selectIndex(int index) {
                Log.e("selectIndex: ", index + "");
            }
        });
    }
}
