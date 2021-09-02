package com.hap.checkinproc.SFA_Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hap.checkinproc.Activity_Hap.Cumulative_Order_Model;
import com.hap.checkinproc.Activity_Hap.SFA_Activity;
import com.hap.checkinproc.R;

import java.util.ArrayList;
import java.util.List;

public class BarChartFragment extends Fragment implements OnChartValueSelectedListener {

    private LineChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;
    private List<Cumulative_Order_Model> cumulative_order_modelList = new ArrayList<>();
    Context mContext;


    public BarChartFragment(Context context, String table1) {
        this.mContext=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for mContext fragment
        View view = inflater.inflate(R.layout.activity_barchart, container, false);

        BarChart barChart = (BarChart) view.findViewById(R.id.chart);


//        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//        dataSets.add(getXAxisValues());

        BarData data = new BarData(getDataSet());
        barChart.setData(data);
        // chart.setDescription("My Chart");
        barChart.animateXY(2000, 2000);
        barChart.invalidate();







        chart = view.findViewById(R.id.lineChart);
      //  chart.setOnChartValueSelectedListener((OnChartValueSelectedListener) mContext);

        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setDrawBorders(false);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setDrawAxisLine(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setDrawGridLines(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

//        seekBarX.setProgress(20);
//        seekBarY.setProgress(100);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);


        chart.resetTracking();


         ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        for (int z = 0; z < 3; z++) {

            ArrayList<Entry> values = new ArrayList<>();

            for (int i = 0; i < 100; i++) {
                double val = (Math.random() * 20) + 3;
                values.add(new Entry(i, (float) val));
            }

            LineDataSet d = new LineDataSet(values, "DataSet " + (z + 1));
            d.setLineWidth(2.5f);
            d.setCircleRadius(4f);

            int color = colors[z % colors.length];
            d.setColor(color);
            d.setCircleColor(color);
            dataSets.add(d);
        }

        // make the first DataSet dashed
        ((LineDataSet) dataSets.get(0)).enableDashedLine(10, 10, 0);
        ((LineDataSet) dataSets.get(0)).setColors(ColorTemplate.VORDIPLOM_COLORS);
        ((LineDataSet) dataSets.get(0)).setCircleColors(ColorTemplate.VORDIPLOM_COLORS);

        LineData data1 = new LineData(dataSets);
        chart.setData(data1);
        chart.invalidate();
        return view;
    }

    private final int[] colors = new int[] {
            ColorTemplate.VORDIPLOM_COLORS[0],
            ColorTemplate.VORDIPLOM_COLORS[1],
            ColorTemplate.VORDIPLOM_COLORS[2]
    };


    private ArrayList getDataSet() {
        ArrayList dataSets = null;


        cumulative_order_modelList.clear();


        cumulative_order_modelList.add(new Cumulative_Order_Model("DSO", 120, 40, 60, 20,280,190));

        cumulative_order_modelList.add(new Cumulative_Order_Model("DCO", 100, 60, 50, 30,100,160));

        dataSets = new ArrayList();
        int xPos = 10;


        for (int i = 0; i < cumulative_order_modelList.size(); i++) {


            ArrayList valueSet1 = new ArrayList();
            BarEntry v1e2 = new BarEntry(xPos, cumulative_order_modelList.get(i).getExisting()); // Feb
            valueSet1.add(v1e2);

            ArrayList valueSet2 = new ArrayList();
            BarEntry v2e1 = new BarEntry(xPos, cumulative_order_modelList.get(i).getNewCustomer()); // Jan
            valueSet2.add(v2e1);


            BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Existing");
            barDataSet1.setColor(Color.GREEN);
            BarDataSet barDataSet2 = new BarDataSet(valueSet2, "New");
            barDataSet2.setColors(Color.RED);

            dataSets.add(barDataSet1);
            dataSets.add(barDataSet2);


            xPos = 30;

        }


        int xPos2 = 20;

        for (int i = 0; i < cumulative_order_modelList.size(); i++) {

            ArrayList valueSet3 = new ArrayList();
            BarEntry v3e1 = new BarEntry(xPos2, cumulative_order_modelList.get(i).getTotalLtrs()); // Jan
            valueSet3.add(v3e1);

            ArrayList valueSet4 = new ArrayList();
            BarEntry v4e1 = new BarEntry(xPos2, cumulative_order_modelList.get(i).getNewOrderLtrs()); // Jan
            valueSet4.add(v4e1);

            BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Total Calls");
            barDataSet3.setColor(Color.BLUE);
            BarDataSet barDataSet4 = new BarDataSet(valueSet4, "New Calls");
            barDataSet4.setColors(Color.MAGENTA);


            dataSets.add(barDataSet3);
            dataSets.add(barDataSet4);

            xPos2 = 40;
        }


//        XAxis xAxis;
//        {   // // X-Axis Style // //
//            xAxis = chart.getXAxis();
//
//            // vertical grid lines
//            xAxis.enableGridDashedLine(10f, 10f, 0f);
//        }
//
//        YAxis yAxis;
//        {   // // Y-Axis Style // //
//            yAxis = chart.getAxisLeft();
//
//            // disable dual axis (only use LEFT axis)
//            chart.getAxisRight().setEnabled(false);
//
//            // horizontal grid lines
//            yAxis.enableGridDashedLine(10f, 10f, 0f);
//
//            // axis range
//            yAxis.setAxisMaximum(200f);
//            yAxis.setAxisMinimum(-50f);
//        }
//
//
//        {   // // Create Limit Lines // //
//            LimitLine llXAxis = new LimitLine(9f, "Index 10");
//            llXAxis.setLineWidth(4f);
//            llXAxis.enableDashedLine(10f, 10f, 0f);
//            llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//            llXAxis.setTextSize(10f);
//            // llXAxis.setTypeface(tfRegular);
//
//            LimitLine ll1 = new LimitLine(150f, "Upper Limit");
//            ll1.setLineWidth(4f);
//            ll1.enableDashedLine(10f, 10f, 0f);
//            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
//            ll1.setTextSize(10f);
//            // ll1.setTypeface(tfRegular);
//
//            LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
//            ll2.setLineWidth(4f);
//            ll2.enableDashedLine(10f, 10f, 0f);
//            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//            ll2.setTextSize(10f);
//            //ll2.setTypeface(tfRegular);
//
//            // draw limit lines behind data instead of on top
//            yAxis.setDrawLimitLinesBehindData(true);
//            xAxis.setDrawLimitLinesBehindData(true);
//
//            // add limit lines
//            yAxis.addLimitLine(ll1);
//            yAxis.addLimitLine(ll2);
//            //xAxis.addLimitLine(llXAxis);
//        }
//
//        Legend l = chart.getLegend();
//
//        // draw legend entries as lines
//        l.setForm(Legend.LegendForm.LINE);

        return dataSets;
    }

    private ArrayList getXAxisValues() {
        ArrayList xAxis = new ArrayList();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        return xAxis;
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
