package com.fullcat.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fullcat.chat.view.ChartView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ChartView chartView;
    private List<ChartView.Data> dataSource = new ArrayList<>();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chartView = (ChartView) findViewById(R.id.chart_view);
        for (int i = 0; i < 10; i++) {
            ChartView.Data data = new ChartView.Data();
            data.dataX = "6月1日";
            data.dataY = new Random().nextInt(200);
            dataSource.add(data);
        }
        chartView.setSuffixY("元");
        chartView.setDataSource(dataSource);

    }
}
