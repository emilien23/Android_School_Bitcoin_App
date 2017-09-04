package com.androidschool_bitcoin_app.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidschool_bitcoin_app.R;
import com.androidschool_bitcoin_app.api.api.bitcoin.BitcoinService;
import com.androidschool_bitcoin_app.api.api.bitcoin.bitcoindetailed.ApiFactory;
import com.androidschool_bitcoin_app.models.Bitcoin;
import com.androidschool_bitcoin_app.models.BitcoinHistory;
import com.androidschool_bitcoin_app.models.Data;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Эмиль on 22.08.2017.
 */

public class BitcoinDetailedActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.bitcoin_title_detailed)
    TextView textViewTitle;

    @BindView(R.id.current_course_value)
    TextView textViewCurrentCourse;

    @BindView(R.id.in_developing_text)
    TextView textViewDevelopText;

    @BindView(R.id.bitcoin_chart)
    LineChart lineChart;
    @BindView(R.id.one_d_image)
    ImageView oneDayImage;
    @BindView(R.id.one_h_image)
    ImageView oneHImage;
    @BindView(R.id.seven_d_image)
    ImageView sevenDayImage;
    @BindView(R.id.one_d_value)
    TextView oneDayTextValue;
    @BindView(R.id.one_h_value)
    TextView oneHtextValue;
    @BindView(R.id.seven_d_value)
    TextView sevenDayTextValue;
    @BindView(R.id.last_modified_value)
    TextView lastModifiedTextValue;
    @BindView(R.id.exit_image_button)
    ImageButton exitButton;

    BitcoinService bitcoinService;
    BitcoinHistory bitcoinHistory;
    Bitcoin bitcoin;

    String convertCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_bitcoin_detailed);
        ButterKnife.bind(this);

        bitcoinService = ApiFactory.getRetrofitInstance().create(BitcoinService.class);

        bitcoin = getIntent().getParcelableExtra("BitcoinClass");


        textViewTitle.setText(bitcoin.getName() + "(" + bitcoin.getSymbol() + ")");

        convertCurrency = getIntent().getStringExtra("PrefConvert");
        if (convertCurrency.equals("RUB"))
            textViewCurrentCourse.setText("1 " + bitcoin.getSymbol() + " = " + Float.valueOf(bitcoin.getPriceRub()).intValue() + " ₽");
        if (convertCurrency.equals("EUR"))
            textViewCurrentCourse.setText("1 " + bitcoin.getSymbol() + " = " + Float.valueOf(bitcoin.getPriceEur()).intValue() + " €");
        if (convertCurrency.equals("USD"))
            textViewCurrentCourse.setText("1 " + bitcoin.getSymbol() + " = " + Float.valueOf(bitcoin.getPriceUsd()).intValue() + " $");

        Long last = Long.valueOf(bitcoin.getLastUpdated()) * 1000;
        lastModifiedTextValue.setText(new SimpleDateFormat("dd.MM.yy : kk:mm" )
                .format(new Date(last)));
        showChangePercentData();


        fetchBitcoinHistoryWithParams(bitcoin.getSymbol(), convertCurrency, 6);

        setupChart();
        exitButton.setOnClickListener(this);
    }

    public void onClick(View v){
        if (v.getId() == R.id.exit_image_button)
            finish();
    }


    private void fetchBitcoinHistoryWithParams(String symbol, String currency, Integer limit) {

        Call<BitcoinHistory> call = bitcoinService.fetchBitcoinHistoryWithParams(symbol, currency, limit);
        call.enqueue(new Callback<BitcoinHistory>() {

            @Override
            public void onResponse(@NonNull Call<BitcoinHistory> call, @NonNull Response<BitcoinHistory> response) {
                if (response.isSuccessful()) {

                    bitcoinHistory = response.body();
                    addDataToChart();
                } else {
                    Toast.makeText(BitcoinDetailedActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<BitcoinHistory> call, @NonNull Throwable t) {

                Toast.makeText(BitcoinDetailedActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                Log.d("Error", t.getMessage());
            }
        });
    }
    private void setupChart() {
        lineChart.getDescription().setEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setMaxVisibleValueCount(60);
    }
    private void addDataToChart() {

        List<Entry> price = new ArrayList<>();
        int index = 1;
        if (bitcoinHistory.getResponse().equals("Success")) {
            for (Data dt : bitcoinHistory.getData()) {
                price.add(new Entry(index, dt.getClose()));
                index++;
            }
            LineDataSet setMorn = new LineDataSet(price, "Price");
            setMorn.setColor(Color.GREEN);

            LineData data = new LineData(setMorn);

            lineChart.setData(data);
            lineChart.invalidate();
        }
        else {
            textViewDevelopText.setVisibility(View.VISIBLE);
            lineChart.setVisibility(View.INVISIBLE);
        }
    }
    public void showChangePercentData(){
        Float hour = Float.valueOf(bitcoin.getPercentChange1h());
        Float day = Float.valueOf(bitcoin.getPercentChange24h());
        Float week = Float.valueOf(bitcoin.getPercentChange7d());

        oneDayTextValue.setText(bitcoin.getPercentChange24h() + getString(R.string.percent));
        oneHtextValue.setText(bitcoin.getPercentChange1h() + getString(R.string.percent));
        sevenDayTextValue.setText(bitcoin.getPercentChange7d() + getString(R.string.percent));

        setColor(hour, oneHtextValue, oneHImage);
        setColor(day, oneDayTextValue, oneDayImage);
        setColor(week, sevenDayTextValue, sevenDayImage);

    }
    public void setColor(Float period, TextView text, ImageView image) {
        if (period > 0) {
            text.setTextColor(Color.GREEN);
            image.setImageResource(R.drawable.ic_arrow_drop_up_24dp);
        } else if (period < 0) {
            text.setTextColor(Color.RED);
            image.setImageResource(R.drawable.ic_arrow_drop_down_24dp);
        }
        else{
            text.setTextColor(Color.GRAY);
            image.setImageResource(R.drawable.ic_remove_24dp);
        }
    }
}
