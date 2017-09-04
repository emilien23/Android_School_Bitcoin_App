package com.androidschool_bitcoin_app.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidschool_bitcoin_app.CustomDialogFragment;
import com.androidschool_bitcoin_app.R;
import com.androidschool_bitcoin_app.api.api.bitcoin.BitcoinService;
import com.androidschool_bitcoin_app.api.api.bitcoin.bitcoinlist.ApiFactory;
import com.androidschool_bitcoin_app.api.api.bitcoin.bitcoinlistewithimage.ApiFactoryWithImage;
import com.androidschool_bitcoin_app.models.Bitcoin;
import com.androidschool_bitcoin_app.models.GlobalData;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BitcoinListActivity extends AppCompatActivity {


    public static final String LIMIT = "pref_nameOfLimit";
    public static final String CONVERT = "pref_typeOfConvert";
    private boolean preferencesChanged = false;
    public static SharedPreferences sp;

    BitcoinService bitcoinService;
    BitcoinService bitcoinServiceWithImage;
    BitcoinService bitcoinServiceGlobalData;
    RecyclerView bitcoinListView;
    List<Bitcoin> bitcoins;
    String bitcoinsWithImage;

    public static GlobalData globalData;

    public static Integer count = 0;
    private ProgressDialog loadingDialog;
    private SwipeRefreshLayout swipeContainer;


    final String urlForLoadImage = "https://www.cryptocompare.com";

     BitcoinListActivity.BitcoinAdapter bitcoinAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.androidschool_bitcoin_app.R.layout.ac_bitcoin_list);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        PreferenceManager.getDefaultSharedPreferences(this).
                registerOnSharedPreferenceChangeListener(preferencesChangeListener);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync();
            }
        });

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);

        loadingDialog = ProgressDialog.show(this, "", getString(R.string.download_data), true);

        bitcoinService = ApiFactory.getRetrofitInstance().create(BitcoinService.class);

        bitcoinServiceWithImage = ApiFactoryWithImage.getRetrofitInstance().create(BitcoinService.class);

        bitcoinServiceGlobalData = ApiFactory.getRetrofitInstance().create(BitcoinService.class);

        bitcoinListView = (RecyclerView) findViewById(R.id.recycler_view_bitcoin);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        bitcoinListView.setLayoutManager(layoutManager);

        fetchBitcoinsWithCurrencyAndLimit(sp.getString(CONVERT, null), sp.getString(LIMIT,null));
        fetchBitcoinList();

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        bitcoinListView.addItemDecoration(itemDecoration);


        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        bitcoinListView.setItemAnimator(itemAnimator);


    }
    @Override
    protected void onStart() {
        super.onStart();
        if (preferencesChanged) {

            fetchTimelineAsync();
            preferencesChanged = false;
        }
    }

    public void fetchTimelineAsync() {

                bitcoinAdapter.clear();
                fetchBitcoinsWithCurrencyAndLimit(sp.getString(CONVERT, null), sp.getString(LIMIT,null));
                fetchBitcoinList();
                bitcoinAdapter.addAll(bitcoins);

                swipeContainer.setRefreshing(false);
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", getString(R.string.timeline_error) + e.toString());
            }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bitcoin_list, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_filter:
                startActivity(new Intent(this, PrefActivity.class));
                break;
            case R.id.action_global:
                fetchGlobalData(sp.getString(CONVERT,null));
                break;
        }
        return true;
    }
    public void showDialog() {

        CustomDialogFragment myDialogFragment = new CustomDialogFragment();
        FragmentManager manager = getSupportFragmentManager();
        myDialogFragment.show(manager, "dialog");

    }

    private void fetchBitcoinsWithCurrencyAndLimit(String currency, String limit) {
        Integer lmt;
        if (limit.equals("No limit"))
            lmt = -1;
        else lmt = Integer.valueOf(limit);

        Call<List<Bitcoin>> call = bitcoinService.fetchBitcoinsWithCurrencyAndLimit(currency, lmt);
        count++;
        loadingDialog.show();

        call.enqueue(new Callback<List<Bitcoin>>() {

            @Override
            public void onResponse(@NonNull Call<List<Bitcoin>> call, @NonNull Response<List<Bitcoin>> response) {
                if (response.isSuccessful()) {
                    count--;
                    bitcoins = response.body();

                    if (count == 0)
                        outputList();

                } else {
                    // Если пришел код ошибки, то обрабатываем её
                    Toast.makeText(BitcoinListActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Bitcoin>> call, @NonNull Throwable t) {

                Toast.makeText(BitcoinListActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                Log.d("Error", t.getMessage());
            }
        });
    }
    private void fetchGlobalData(String currency) {

        Call<GlobalData> call = bitcoinServiceGlobalData.fetchGlobalData(currency);

        loadingDialog.show();
        call.enqueue(new Callback<GlobalData>() {

            @Override
            public void onResponse(@NonNull Call<GlobalData> call, @NonNull Response<GlobalData> response) {
                if (response.isSuccessful()) {
                    globalData = response.body();

                } else {
                    Toast.makeText(BitcoinListActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
                showDialog();
            }

            @Override
            public void onFailure(@NonNull Call<GlobalData> call, @NonNull Throwable t) {
                loadingDialog.dismiss();
                Toast.makeText(BitcoinListActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                Log.d("Error", t.getMessage());
            }
        });
    }
    private void fetchBitcoinList() {


        Call<ResponseBody> call = bitcoinServiceWithImage.fetchBitcoinList();
        count++;

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        count--;
                        bitcoinsWithImage = response.body().string();
                        if (count == 0)
                            outputList();
                    }catch(IOException e){
                        Log.e("TAG", e.toString());
                    }

                    loadingDialog.dismiss();

                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(BitcoinListActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                Toast.makeText(BitcoinListActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                Log.d("Error", t.getMessage());
            }
        });
    }
    private void outputList() {
        try {
            JSONObject dataJson = new JSONObject(bitcoinsWithImage);
            List<Bitcoin> bitcoinList = new ArrayList<>();
            JSONObject data = dataJson.getJSONObject("Data");
        for(Bitcoin bit: bitcoins) {
            if(data.has(bit.getSymbol()) && data.getJSONObject(bit.getSymbol()).has(getString(R.string.image_url)))
                    bit.setImage(data.getJSONObject(bit.getSymbol()).getString(getString(R.string.image_url)));
            else
                bit.setImage("null");
            bitcoinList.add(bit);
        }
            bitcoinAdapter = new BitcoinListActivity.BitcoinAdapter(bitcoinList);
            bitcoinListView.setAdapter(bitcoinAdapter);
        }catch(JSONException e){Log.e("TAG", e.toString());}

    }
    public class BitcoinViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView subtitle;
        ImageView image;
        ImageView chevron;
        RelativeLayout bodyBitcoin;

        public BitcoinViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_text);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle_text);
            image = (ImageView) itemView.findViewById(R.id.image_bitcoin);
            chevron = (ImageView) itemView.findViewById(R.id.image_chevron_right);
            bodyBitcoin = (RelativeLayout) itemView.findViewById(R.id.body_bitcoin);
        }
    }
    public class BitcoinAdapter extends RecyclerView.Adapter<BitcoinListActivity.BitcoinViewHolder> {

        List<Bitcoin> bitcoin;

        public  BitcoinAdapter(List<Bitcoin> bitcoin) {
            this.bitcoin = bitcoin;
        }

        @Override
        public BitcoinListActivity.BitcoinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View rowView = layoutInflater.inflate(R.layout.bitcoin_rows, parent, false);
            return new BitcoinListActivity.BitcoinViewHolder(rowView);
        }

        @Override
        public void onBindViewHolder(BitcoinListActivity.BitcoinViewHolder holder, int position) {
            final int pos = position;
            TextView textViewTitle = holder.title;
            textViewTitle.setText(bitcoin.get(position).getName());


            TextView textViewSymbol = holder.subtitle;
            textViewSymbol.setText(bitcoin.get(position).getSymbol());

            ImageView imageViewBitcoin = holder.image;
            if(!bitcoin.get(position).getImage().equals("null")) {
                Picasso.with(getApplicationContext())
                        .load(urlForLoadImage + bitcoin.get(position).getImage())
                        .into(imageViewBitcoin);
            }
            else
                imageViewBitcoin.setImageResource(R.drawable.ic_camera_alt_24dp);

            ImageView  imageViewChevron = holder.chevron;
            imageViewChevron.setImageResource(R.drawable.ic_chevron_right_black_24dp);


            holder.bodyBitcoin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(BitcoinListActivity.this, BitcoinDetailedActivity.class);
                    intent.putExtra("BitcoinClass", bitcoin.get(pos));
                    intent.putExtra("PrefConvert", sp.getString(CONVERT, null));
                    startActivity(intent);
                }
            });
        }
        @Override
        public int getItemCount() {
            return bitcoin.size();
        }

        public void add(int i, List<Object> newlist) {
            notifyItemRangeInserted(i, newlist.size());
        }

        public void clear() {
            bitcoins.clear();
            notifyDataSetChanged();
        }

        public void addAll(List<Bitcoin> list) {
            bitcoins.addAll(list);
            notifyDataSetChanged();
        }
    }

    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                // Вызывается при изменении настроек приложения
                @Override
                public void onSharedPreferenceChanged(
                        SharedPreferences sharedPreferences, String key) {
                    preferencesChanged = true;

                    if (key.equals(LIMIT)) {
                        fetchTimelineAsync();

                    } else if (key.equals(CONVERT)) {
                        fetchTimelineAsync();
                    }
                }

            };
    }
