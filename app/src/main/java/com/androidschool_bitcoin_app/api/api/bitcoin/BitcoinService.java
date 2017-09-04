package com.androidschool_bitcoin_app.api.api.bitcoin;

import com.androidschool_bitcoin_app.models.Bitcoin;
import com.androidschool_bitcoin_app.models.BitcoinHistory;
import com.androidschool_bitcoin_app.models.GlobalData;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Эмиль on 23.08.2017.
 */

public interface BitcoinService{
    /** https://coinmarketcap.com/api/ **/

    @GET("ticker")
    Call<List<Bitcoin>> fetchBitcoinsWithCurrencyAndLimit(@Query("convert") String currency,
                                                         @Query("limit") Integer limitBitcoins);

    @GET("global")
    Call<GlobalData> fetchGlobalData(@Query("convert") String currency);

    /** https://min-api.cryptocompare.com/ **/
    @GET("histoday")
    Call<BitcoinHistory> fetchBitcoinHistoryWithParams(@Query("fsym") String symbol, @Query("tsym") String currency,
                                                       @Query("limit") Integer limit);

    @GET("coinlist")
    Call<ResponseBody> fetchBitcoinList();
}
