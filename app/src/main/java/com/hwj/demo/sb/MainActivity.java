package com.hwj.demo.sb;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.hwj.demo.sb.databinding.ActivityMainBinding;
import com.hwj.junmeng.sb.SegmentBar;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();

    }

    private void init() {
        initTab1();
        initTab2();
    }

    private void initTab2() {
        binding.sbTab2.setUnreadTextSize(10);
    }

    private void initTab1() {
        binding.sbTab.setOnItemClickListener(new SegmentBar.OnItemClickListener() {
            @Override
            public void onClick(int index) {
                Toast.makeText(MainActivity.this, "位置" + index + "被点击", Toast.LENGTH_SHORT).show();
            }
        });
        binding.sbTab.setUnreadBackgroundColor(Color.YELLOW);
        binding.sbTab.setUnreadTextSize(4);
        binding.sbTab.setUnreadPadding(2);
        binding.sbTab.setUnreadMarginRight(8);
        binding.sbTab.setUnreadMarginTop(5);
        binding.sbTab.setUnreadTextColor(Color.YELLOW);
        binding.sbTab.setCornerRadius(5);
        //binding.sbTab.setLabelUnreadCount(0,2);
        binding.sbTab.setLabelUnreadCount(1,-1);
        //binding.sbTab.setLabelUnreadCount(2,-1);
    }

    public void onClickChangeUnread(View view) {
        for (int i = 0; i < binding.sbTab2.getLabelCount(); i++) {
            binding.sbTab2.setLabelUnreadCount(i, (int) (Math.random() * 120) - 1);
        }
    }

    public void onClickChangeLabel(View view) {
        int count=(int)(Math.random()*3)+1;
        String[] labels=new String[count];
        for(int i=0;i<count;i++){
            labels[i]="标签"+(i+1);
        }
        binding.sbTab3.setLabelTextArray(labels);
    }
}
