package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Vector;

public class DriveRecordDialog extends Dialog {

    private LineChart lineChart;
    private Button save_record_button, exit_button;
    private TextView targetCalorie, userCalorie;
    private View.OnClickListener mSaveListener;
    private View.OnClickListener mExitListener;
    Vector<PolylinePoint> polyline = CreateCourseActivity.polylineVector;
    Vector<PolylinePoint> targetRecord = CreateCourseActivity.targetRecordVector;
    Vector<PolylinePoint> userPostRecord = CreateCourseActivity.postUserRecordVector;
    private double targetSumDistance=0, userSumDistance=0;

    public DriveRecordDialog(Context context, View.OnClickListener saveListener, View.OnClickListener exitListener) {
        super(context);
        this.mSaveListener = saveListener;
        this.mExitListener = exitListener;
    }

    @Override
    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);


        setContentView(R.layout.dialog_drive_record);
        save_record_button = (Button) findViewById(R.id.save_record_button);
        exit_button = (Button) findViewById(R.id.exit_button);
        targetCalorie = (TextView) findViewById(R.id.textView2);
        userCalorie = (TextView) findViewById(R.id.textView3);



        save_record_button.setOnClickListener(mSaveListener);
        exit_button.setOnClickListener(mExitListener);

        lineChart = (LineChart) findViewById(R.id.chart);

        Log.i("DriveRecordDialog","recordVector size : " + polyline.size());
        Log.i("DriveRecordDialog","selectedTargetRecord size : " + targetRecord.size());
        Log.i("DriveRecordDialog","userPostRecord size : " + userPostRecord.size());

        ArrayList<Entry> user = new ArrayList<>();
        ArrayList<Entry> target = new ArrayList<>();
        ArrayList<Entry> postUser = new ArrayList<>();


        float count = 0;



        if(CreateCourseActivity.checkUsedCourse && !CreateCourseActivity.checkUserTarget) {
            if(polyline.size()<targetRecord.size())
                count = polyline.size();
            else
                count = targetRecord.size();
            if(count > userPostRecord.size())
                count = userPostRecord.size();

        } else {
            if(polyline.size()<targetRecord.size())
                count = polyline.size();
            else
                count = targetRecord.size();
        }



        Log.i("DriveRecordDialog","checkUsedCourse : " + Boolean.toString(CreateCourseActivity.checkUsedCourse));
        Log.i("DriveRecordDialog","checkUserTarget : " + Boolean.toString(CreateCourseActivity.checkUserTarget));
        Log.i("DriveRecordDialog","count : " + Float.toString(count));


        for(int i=0; i<count; i++) {
            float temp = (float) i;
            user.add(new Entry(polyline.get(i).distance, polyline.get(i).speed));
            userSumDistance+=polyline.get(i).distance;
            target.add(new Entry(targetRecord.get(i).distance, targetRecord.get(i).speed));
            targetSumDistance+=targetRecord.get(i).distance;
            if(CreateCourseActivity.checkUsedCourse && !CreateCourseActivity.checkUserTarget)
                postUser.add(new Entry(userPostRecord.get(i).distance, userPostRecord.get(i).speed));

        }

        targetCalorie.setText(441.0*targetSumDistance/20000 + "");
        userCalorie.setText(441.0*userSumDistance/20000 + "");

        LineData lineData = new LineData();

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        
        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setTextColor(Color.BLACK);
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        LineDataSet userDataset = new LineDataSet(user, "user");
        userDataset.setColor(Color.BLUE);
        LineDataSet targetDataset = new LineDataSet(target, "target");
        userDataset.setColor(Color.RED);
        if(CreateCourseActivity.checkUsedCourse && !CreateCourseActivity.checkUserTarget) {
            LineDataSet postUserDataset = new LineDataSet(postUser, "postUser");
            postUserDataset.setColor(Color.BLACK);
            lineData.addDataSet(postUserDataset);
        }


        lineData.addDataSet(userDataset);
        lineData.addDataSet(targetDataset);
        lineChart.getDescription().setEnabled(false);
        lineChart.setAutoScaleMinMaxEnabled(true);
        lineChart.setDrawMarkers(false);
        lineChart.setData(lineData);

    }



    private float timeToFloat(String str) {

        String[] strArr = str.split(":");
        float sum = 0;

        for(int i=0; i<strArr.length; i++) {
            if(strArr[i].charAt(0)=='0')
                strArr[i] = strArr[i].substring(1);

            switch(i) {
                case 0 :
                    sum += (Float.parseFloat(strArr[i]) * 3600);
                    break;
                case 1 :
                    sum += (Float.parseFloat(strArr[i]) * 60);
                    break;
                case 2 :
                    sum += Float.parseFloat(strArr[i]);
                    break;
            }
            Log.i("timeToFloat","sum : " + sum);
        }
        float result = (float) (sum / 60.0);
        return result;
    }

}

