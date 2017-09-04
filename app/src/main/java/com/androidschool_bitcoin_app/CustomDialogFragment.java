package com.androidschool_bitcoin_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import static com.androidschool_bitcoin_app.activities.BitcoinListActivity.CONVERT;
import static com.androidschool_bitcoin_app.activities.BitcoinListActivity.globalData;
import static com.androidschool_bitcoin_app.activities.BitcoinListActivity.sp;

/**
 * Created by Эмиль on 25.08.2017.
 */

public class CustomDialogFragment extends DialogFragment{



    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater ltInflater = getActivity().getLayoutInflater();
        View dialogLayout = ltInflater.inflate(R.layout.dialog, null, false);

        TextView totalCap = (TextView) dialogLayout.findViewById(R.id.total_market_cap_value);
        if (sp.getString(CONVERT,null).equals("EUR"))
            totalCap.setText(String.valueOf(globalData.getTotalMarketCapEur())+ getString(R.string.euro));
        if (sp.getString(CONVERT,null).equals("RUB"))
            totalCap.setText(String.valueOf(globalData.getTotalMarketCapRub()) + getString(R.string.rub));
        if (sp.getString(CONVERT,null).equals("USD"))
            totalCap.setText(String.valueOf(globalData.getTotalMarketCapUsd()) + getString(R.string.dollar));

        TextView bitcoinPercent = (TextView) dialogLayout.findViewById(R.id.bitcoin_percent_value);
        bitcoinPercent.setText(String.valueOf(globalData.getBitcoinPercentage()) + getString(R.string.percent));

        TextView activeCur = (TextView) dialogLayout.findViewById(R.id.active_currencies_value);
        activeCur.setText(String.valueOf(globalData.getActiveCurrencies()));

        TextView activeMarkets = (TextView) dialogLayout.findViewById(R.id.active_markets_value);
        activeMarkets.setText(String.valueOf(globalData.getActiveMarkets()));


        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("Global data")
                .setView(dialogLayout)
                .setPositiveButton("OK", null)
                .create();
    }

}
