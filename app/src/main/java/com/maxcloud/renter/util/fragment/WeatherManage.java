package com.maxcloud.renter.util.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxcloud.renter.R;
import com.maxcloud.renter.util.FileHelper;
import com.maxcloud.renter.util.L;
import com.maxcloud.renter.util.file.TextFileReader;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 描    述：天气管理类，提供天气数据加载和显示功能。
 * 作    者：向晓阳
 * 时    间：2016/2/17
 * 版    权：迈斯云门禁网络科技有限公司
 */
public class WeatherManage {
    private TextView txvTodayWeather;
    private ImageView imgTodayWeather;
    private ImageView imgTomorrowWeather;
    private TextView txvTip;

    private TextView txvTodayTemp;
    private TextView txvTomorrowWeather;
    private TextView txvTomorrowTemp;
    private TextView txvTodayUmbrella;
    private TextView txvTodayClothes;

    public WeatherManage(View view) {
        txvTip = (TextView) view.findViewById(R.id.txvTip);

        imgTodayWeather = (ImageView) view.findViewById(R.id.imgTodayWeather);
        txvTodayWeather = (TextView) view.findViewById(R.id.txvTodayWeather);
        txvTodayTemp = (TextView) view.findViewById(R.id.txvTodayTemp);
        imgTomorrowWeather = (ImageView) view.findViewById(R.id.imgTomorrowWeather);
        txvTomorrowWeather = (TextView) view.findViewById(R.id.txvTomorrowWeather);
        txvTomorrowTemp = (TextView) view.findViewById(R.id.txvTomorrowTemp);
        txvTodayUmbrella = (TextView) view.findViewById(R.id.txvTodayUmbrella);
        txvTodayClothes = (TextView) view.findViewById(R.id.txvTodayClothes);
    }

    public void loadLocalWeather() throws IOException {
        if (!FileHelper.existsWeather()) {
            return;
        }

        String todayTempMax = "----";
        String todayTempMin = "----";
        String tomorrowTempMax = "----";
        String tomorrowTempMin = "----";
        int todayUmbrella = 0;
        String todayClothes = "";

        int todayWeatherId = 100;
        int tomorrowWeatherId = 100;
        Date curDate = new Date();

        TextFileReader weatherRd = null;

        try {
            weatherRd = new TextFileReader(new File(FileHelper.getWeather()));

            String line = weatherRd.readLine();
            if (null != line) {
                try {
                    JSONObject jsonItem = new JSONObject(line);

                    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String date = jsonItem.optString("date", dateFmt.format(new Date()));
                    todayTempMax = jsonItem.optString("today_max", "----");
                    todayTempMin = jsonItem.optString("today_min", "----");

                    tomorrowTempMax = jsonItem.optString("tomorrow_max", "----");
                    tomorrowTempMin = jsonItem.optString("tomorrow_min", "----");

                    todayWeatherId = jsonItem.optInt("now_weather_id", 0);
                    tomorrowWeatherId = jsonItem.optInt("tomorrow_weather_id", 0);
                    todayClothes = jsonItem.optString("today_wear_short", "");
                    todayUmbrella = jsonItem.optInt("today_pop", 0);

                    curDate = dateFmt.parse(date);
                } catch (Exception e) {
                    L.e("reloadWeather", e);
                }

                txvTip.setText(String.format("%1$tm月%1$td日 %1$tA", curDate));
                txvTodayTemp.setText(String.format("%s/%s℃", todayTempMin, todayTempMax));
                txvTodayWeather.setText(getWeatherText(todayWeatherId));
                txvTomorrowWeather.setText(getWeatherText(tomorrowWeatherId));
                txvTomorrowTemp.setText(String.format("%s/%s℃", tomorrowTempMin, tomorrowTempMax));
                txvTodayUmbrella.setText(String.format("降雨概率：%d%%", todayUmbrella));
                txvTodayClothes.setText(todayClothes);

                imgTodayWeather.setImageResource(getWeatherImgRes(todayWeatherId));
                imgTomorrowWeather.setImageResource(getWeatherImgRes(tomorrowWeatherId));
            }
        } finally {
            if (null != weatherRd) {
                weatherRd.close();
            }
        }
    }

    private String getWeatherText(int weatherCode) {
        switch (weatherCode) {
            case 100:// 晴
                return "晴";
            case 101: // 多云
            case 102: // 少云
            case 103: // 晴间多云
                return "多云";
            case 104: // 阴
                return "阴";
            case 200: // 有风
            case 201: // 平静
            case 202: // 微风
            case 203: // 和风
            case 204: // 清风
            case 205: // 强风/劲风
            case 206: // 疾风
            case 207: // 大风
            case 208: // 烈风
            case 209: // 风暴
            case 210: // 狂爆风
            case 211: // 飓风
            case 212: // 龙卷风
            case 213: // 热带风暴
                return "风";
            case 300: // 阵雨
            case 301: // 强阵雨
                return "阵雨";
            case 302: // 雷阵雨
            case 303: // 强雷阵雨
                return "雷阵雨";
            case 304: // 雷阵雨伴有冰雹
                return "雷阵雨加冰雹";
            case 309: // 毛毛雨/细雨
            case 305: // 小雨
                return "小雨";
            case 306: // 中雨
                return "中雨";
            case 307: // 大雨
                return "大雨";
            case 308: // 极端降雨
            case 310: // 暴雨
            case 311: // 大暴雨
            case 312: // 特大暴雨
                return "暴雨";
            case 313: // 冻雨
                return "冻雨";
            case 400: // 小雪
                return "小雪";
            case 401: // 中雪
                return "中雪";
            case 402: // 大雪
                return "大雪";
            case 403: // 暴雪
                return "暴雪";
            case 404: // 雨夹雪
            case 405: // 雨雪天气
            case 406: // 阵雨夹雪
                return "雨夹雪";
            case 407: // 阵雪
                return "阵雪";
            case 500: // 薄雾
            case 501: // 雾
                return "雾";
            case 502: // 霾
                return "霾";
            case 503: // 扬沙
                return "扬沙";
            case 504: // 浮尘
            case 506: // 火山灰
                return "浮尘";
            case 507: // 沙尘暴
            case 508: // 强沙尘暴
                return "沙尘暴";
            case 900: // 热
                return "热";
            case 901: // 冷
                return "冷";
            default:// 未知
                return "晴";
        }
    }

    private int getWeatherImgRes(int weatherCode) {
        switch (weatherCode) {
            case 100: // 晴
                return R.mipmap.ic_weather_qing;
            case 101: // 多云
            case 102: // 少云
            case 103: // 晴间多云
                return R.mipmap.ic_weather_duoyun;
            case 104: // 阴
                return R.mipmap.ic_weather_yin;
            case 200: // 有风
            case 201: // 平静
            case 202: // 微风
            case 203: // 和风
            case 204: // 清风
            case 205: // 强风/劲风
            case 206: // 疾风
            case 207: // 大风
            case 208: // 烈风
            case 209: // 风暴
            case 210: // 狂爆风
            case 211: // 飓风
            case 212: // 龙卷风
            case 213: // 热带风暴
                return R.mipmap.ic_weather_fuchen;
            case 300: // 阵雨
            case 301: // 强阵雨
                return R.mipmap.ic_weather_zhenyu;
            case 302: // 雷阵雨
            case 303: // 强雷阵雨
                return R.mipmap.ic_weather_leizhenyu;
            case 304: // 雷阵雨伴有冰雹
                return R.mipmap.ic_weather_leizhenyubanbingbao;
            case 309: // 毛毛雨/细雨
            case 305: // 小雨
                return R.mipmap.ic_weather_xiaoyu;
            case 306: // 中雨
                return R.mipmap.ic_weather_zhongyu;
            case 307: // 大雨
                return R.mipmap.ic_weather_dayu;
            case 308: // 极端降雨
            case 310: // 暴雨
            case 311: // 大暴雨
            case 312: // 特大暴雨
                return R.mipmap.ic_weather_baoyu;
            case 313: // 冻雨
                return R.mipmap.ic_weather_dongyu;
            case 400: // 小雪
                return R.mipmap.ic_weather_xiaoxue;
            case 401: // 中雪
                return R.mipmap.ic_weather_zhongxue;
            case 402: // 大雪
                return R.mipmap.ic_weather_daxue;
            case 403: // 暴雪
                return R.mipmap.ic_weather_baoxue;
            case 404: // 雨夹雪
            case 405: // 雨雪天气
            case 406: // 阵雨夹雪
                return R.mipmap.ic_weather_yujiaxue;
            case 407: // 阵雪
                return R.mipmap.ic_weather_zhenxue;
            case 500: // 薄雾
            case 501: // 雾
                return R.mipmap.ic_weather_wu;
            case 502: // 霾
                return R.mipmap.ic_weather_mai;
            case 503: // 扬沙
                return R.mipmap.ic_weather_yangsha;
            case 504: // 浮尘
            case 506: // 火山灰
                return R.mipmap.ic_weather_fuchen;
            case 507: // 沙尘暴
            case 508: // 强沙尘暴
                return R.mipmap.ic_weather_shachenbao;
            case 900: // 热
                return R.mipmap.ic_weather_qing;
            case 901: // 冷
                return R.mipmap.ic_weather_baoxue;
            default:// 未知
                return R.mipmap.ic_weather_qing;
        }
    }
}
