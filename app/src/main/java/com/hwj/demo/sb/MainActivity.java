package com.hwj.demo.sb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.hwj.junmeng.sb.SegmentBar;

public class MainActivity extends AppCompatActivity {

    SegmentBar sbTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sbTab = (SegmentBar) findViewById(R.id.sb_tab);
        sbTab.setOnItemClickListener(new SegmentBar.OnItemClickListener() {
            @Override
            public void onClick(int index) {
                Toast.makeText(MainActivity.this, "位置" + index + "被点击", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
