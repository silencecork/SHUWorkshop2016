package com.example.tembooproject;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.temboo.Library.Google.Spreadsheets.RetrieveWorksheet;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private LineChart mChart;
    private ProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChart = (LineChart) findViewById(R.id.chart1);

        // Setting Chart
        mChart.setDescription("Temboo Demo");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");
        mChart.setTouchEnabled(true);
        mChart.setDragDecelerationFrictionCoef(0.9f);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);
        mChart.setPinchZoom(true);
        mChart.setBackgroundColor(Color.LTGRAY);

        AsyncTask<Void, Void, LineData> task = new AsyncTask<Void, Void, LineData>() {
            @Override
            protected void onPreExecute() {
                mProgressBar = ProgressDialog.show(MainActivity.this, "", "Please wait", false, true);
            }

            @Override
            protected void onPostExecute(LineData data) {
                mProgressBar.dismiss();
                mChart.setData(data);
                mChart.animateX(2500);
            }

            @Override
            protected LineData doInBackground(Void... params) {
                String response = retrieveWorksheetData();
                Log.d(TAG, "response " + response);
                return createChartData(response);
            }
        };
        task.execute();
    }

    private String retrieveWorksheetData() {
        try {
            TembooSession session = new TembooSession("shuworkshop2016", "myFirstApp", "VZ0xp2ScaDLPgbhwp91DWMeXpCW6R5CZ");
            RetrieveWorksheet retrieveWorksheetChoreo = new RetrieveWorksheet(session);

            // Get an InputSet object for the choreo
            RetrieveWorksheet.RetrieveWorksheetInputSet retrieveWorksheetInputs = retrieveWorksheetChoreo.newInputSet();

            // Set inputs
            retrieveWorksheetInputs.set_WorksheetId("od6");
            retrieveWorksheetInputs.set_RefreshToken("1/jMbHz9LYRNRamCNV1PFyJxiNmrpWo94-wMphy0VrThcMEudVrK5jSpoR30zcRFq6");
            retrieveWorksheetInputs.set_ClientSecret("tAIBipRSCgj5QsioLhMPUZhE");
            retrieveWorksheetInputs.set_ClientID("211822648353-ssq3rvtm3tthh8o0ks6ekn96lfv07c4d.apps.googleusercontent.com");
            retrieveWorksheetInputs.set_SpreadsheetKey("1tgiEzAUIsNoN71Tnqb0OacMBvv6SC_kb0LgLzgm7ulg");

            // Execute Choreo
            RetrieveWorksheet.RetrieveWorksheetResultSet retrieveWorksheetResults = retrieveWorksheetChoreo.execute(retrieveWorksheetInputs);
            String response = retrieveWorksheetResults.get_Response();

            return response;
        } catch (TembooException e) {
            e.printStackTrace();
        }

        return null;
    }

    private LineData createChartData(String response) {
        ArrayList<Entry> humidityList = new ArrayList<Entry>();
        ArrayList<Entry> temperatureList = new ArrayList<Entry>();
        ArrayList<Entry> lightList = new ArrayList<Entry>();
        ArrayList<String> timeList = new ArrayList<String>();

        String[] lines = response.split("\n");
        String[] newLines = new String[lines.length - 1];
        System.arraycopy(lines, 1, newLines, 0, newLines.length);
        for (int i = 0; i < newLines.length; i++) {
            String line = newLines[i];
            String datas[] = line.split(",");
            String time = datas[0];
            timeList.add(time);

            String humidity = datas[1];
            humidityList.add(new Entry(Integer.parseInt(humidity), i));

            String  temperature = datas[2];
            temperatureList.add(new Entry(Integer.parseInt( temperature), i));

            String light = datas[3];
            lightList.add(new Entry(Integer.parseInt(light), i));
        }

        LineDataSet humDataSet = createDataSet(humidityList, "濕度", ColorTemplate.JOYFUL_COLORS[0]);
        LineDataSet tempDataSet = createDataSet(temperatureList, "溫度", ColorTemplate.JOYFUL_COLORS[1]);
        LineDataSet lightDataSet = createDataSet(lightList, "亮度", ColorTemplate.JOYFUL_COLORS[2]);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(humDataSet);
        dataSets.add(tempDataSet);
        dataSets.add(lightDataSet);

        LineData data = new LineData(timeList, dataSets);
        data.setValueTextSize(8f);

        return data;
    }

    private LineDataSet createDataSet(ArrayList<Entry> dataEntry, String dataSetName, int color) {
        LineDataSet dataSet = new LineDataSet(dataEntry, dataSetName);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setColor(color);
        dataSet.setCircleColor(Color.WHITE);
        dataSet.setLineWidth(1f);
        dataSet.setCircleRadius(3f);
        dataSet.setFillAlpha(65);
        dataSet.setDrawCircleHole(false);

        return dataSet;
    }

}
