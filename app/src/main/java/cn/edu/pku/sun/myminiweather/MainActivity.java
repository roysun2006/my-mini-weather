package cn.edu.pku.sun.myminiweather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import com.example.hasee.myminiweather.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import cn.edu.pku.sun.bean.TodayWeather;
import cn.edu.pku.sun.util.NetUtil;

/**
 * Created by hasee on 2016/9/20.
 */
public class MainActivity extends Activity implements View.OnClickListener   {
    private static final int UPDATE_TODAY_WEATHER =1;
    private ImageView mUpdateBtn;
    private ImageView mCitySelect;
    private ImageView mGetLocation;
    private LocationClient mLocationClient;
    protected MenuItem refreshItem;
    private TextView cityTv, timeTv, humidTv, weekTv, pmDataTv, pmQualityTv,
            temperatureTv, climateTv, windTv, city_name_Tv;
    private ImageView weatherImg, pmImg;
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    void initView() {
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);
        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);

        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);

        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("myWeather", "网络ok");
            Toast.makeText(MainActivity.this, "网络OK!", Toast.LENGTH_LONG).show();
        } else {
            Log.d("myWeather", "网络挂了");
            Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
        }
        mCitySelect=(ImageView )findViewById(R.id.title_city_manager);
        mCitySelect.setOnClickListener(this);
        mGetLocation=(ImageView )findViewById(R.id.title_location);
        mGetLocation.setOnClickListener(this);
        initView();

    }
    void updateTodayWeather(TodayWeather todayWeather){
        if(todayWeather.getPm25() ==null){
            todayWeather.setPm25("null");
       }
        city_name_Tv.setText(todayWeather.getCity()+"天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime()+"发布");
        humidTv.setText("湿度"+todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getHigh()+"~"+todayWeather.getLow());
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力"+todayWeather.getFengli());
        selectWeather(todayWeather.getType());
        selectPm(todayWeather.getPm25());
        Toast.makeText(MainActivity.this,"更新成功!",Toast.LENGTH_SHORT).show();
    }
  /*  private void getLocalCityName(Context context) {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开GPS
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(3000);// 设置发起定位请求的间隔时间为3000ms
        option.disableCache(false);// 禁止启用缓存定位
        option.setPriority(LocationClientOption.NetWorkFirst);// 网络定位优先
        mLocationClient = new LocationClient(context); // 声明LocationClient类
        mLocationClient.setLocOption(option);// 使用设置
        mLocationClient.start();// 开启定位SDK
        mLocationClient.requestLocation();// 开始请求位置

        mLocationClient.registerLocationListener(new BDLocationListener() {
            public void onReceivePoi(BDLocation arg) {

            }

            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location != null) {
                   // tv_city.setText(location.getCity());
                    Log.e("TAG",""+location.getCity());

                } else {
                  //  tv_city.setText("无法定位");
                    return;
                }
            }
        });

    }
    */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.title_city_manager){
            Intent i=new Intent(this,SelectCity.class);
            startActivityForResult(i,1);
        }
        if(view.getId() == R.id.title_location){
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true);// 打开GPS
            option.setAddrType("all");// 返回的定位结果包含地址信息
            option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
            option.setScanSpan(3000);// 设置发起定位请求的间隔时间为3000ms
            option.disableCache(false);// 禁止启用缓存定位
            option.setPriority(LocationClientOption.NetWorkFirst);// 网络定位优先
            mLocationClient = new LocationClient(this); // 声明LocationClient类
            mLocationClient.setLocOption(option);// 使用设置
            mLocationClient.start();// 开启定位SDK
            mLocationClient.requestLocation();// 开始请求位置

            mLocationClient.registerLocationListener(new BDLocationListener() {
                public void onReceivePoi(BDLocation arg) {

                }

                @Override
                public void onReceiveLocation(BDLocation location) {
                    if (location != null) {
                        // tv_city.setText(location.getCity());
                        Log.e("myWeather",""+location.getCity());

                    } else {
                        //  tv_city.setText("无法定位");
                        Log.e("myWeather","无法定位");
                        return;
                    }
                }
            });
        }
        if (view.getId() == R.id.title_update_btn) {
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code", "101010100");
            Log.d("myWeather", cityCode);

            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络ok");
                queryWeatherCode(cityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了!", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.title_update_btn:
                showRefreshAnimation(item);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @SuppressLint("NewApi")
    private void showRefreshAnimation(MenuItem item) {
        hideRefreshAnimation();

        refreshItem = item;

        //这里使用一个ImageView设置成MenuItem的ActionView，这样我们就可以使用这个ImageView显示旋转动画了
        ImageView refreshActionView = (ImageView) getLayoutInflater().inflate(R.layout.weather_info, null);
        refreshActionView.setImageResource(R.drawable.title_update);
        refreshItem.setActionView(refreshActionView);

        //显示刷新动画
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_top);
        refreshActionView.startAnimation(animation);
        Log.d("MainActivity", "延迟1秒");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideRefreshAnimation();
            }

        }, 1000);
    }

    @SuppressLint("NewApi")
    private void hideRefreshAnimation() {
        if (refreshItem != null) {
            View view = refreshItem.getActionView();
            if (view != null) {
                view.clearAnimation();
                refreshItem.setActionView(null);
            }
        }
    }



    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if (requestCode==1&&resultCode==RESULT_OK){
            String newCityCode=data.getStringExtra("cityCode");
            Log.d("myWeather","选择的城市代码为"+newCityCode);

            if (NetUtil.getNetworkState(this)!=NetUtil.NETWORN_NONE){
                Log.d("myWeather","网络ok");
                queryWeatherCode(newCityCode);
            }else {
                Log.d("myWeather","网络挂了");
                Toast.makeText(MainActivity.this,"网络挂了",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void queryWeatherCode(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                TodayWeather todayWeather = null;
                try {
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str);
                        Log.d("myWeather", str);
                    }
                    String responseStr = response.toString();
                    Log.d("myWeather", responseStr);
                    todayWeather = parseXML(responseStr);
                    if (todayWeather != null) {
                        Log.d("myWeather", todayWeather.toString());
                        Message msg=new Message();
                        msg.what=UPDATE_TODAY_WEATHER;
                        msg.obj=todayWeather;
                        mHandler.sendMessage(msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();
    }


    private TodayWeather parseXML(String xmldata){
        TodayWeather todayWeather = null;
        int fengxiangCount=0;
        int fengliCount =0;
        int dateCount=0;
        int highCount =0;
        int lowCount=0;
        int typeCount =0;
        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    // 判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    // 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals("resp")){
                            todayWeather= new TodayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                        }

                        break;


                    // 判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
                // 进入下一个元素并触发相应事件
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayWeather;
    }

    public void selectWeather(String str) {
        switch (str) {
            case "晴":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);break;
            case "阴":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);break;
            case "暴雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);break;
            case "暴雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);break;
            case "大暴雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);break;
            case "大雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);break;
            case "大雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);break;
            case "多云":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);break;
            case "雷阵雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);break;
            case "雷阵雨冰雹":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);break;
            case "沙尘暴":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);break;
            case "特大暴雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);break;
            case "雾":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);break;
            case "小雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);break;
            case "小雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);break;
            case "雨夹雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);break;
            case "阵雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);break;
            case "阵雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);break;
            case "中雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);break;
            case "中雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);break;
            default:
                break;
        }
    }
    public void selectPm(String str) {
        if(str=="null"){
           str="500";
      }
        int pm2_5 = Integer.parseInt(str);
        if (pm2_5<=50){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);return;
        } else if (pm2_5<=100){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);return;
        }else if(pm2_5<=150){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);return;
        }else if (pm2_5<=200){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);return;
        }else  pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);return;


    }


    /*private TodayWeather parseXML(String xmldata){
        try{
            XmlPullParserFactory fac=XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser=fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType= xmlPullParser.getEventType();
            int fengxiangCount=0;
            int fengliCount=0;
            int dateCount=0;
            int highCount=0;
            int lowCount=0;
            int typeCount=0;
            Log.d("myWeather","parseXML");
            while (eventType!= XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("city")){
                            eventType=xmlPullParser.next();
                            Log.d("myWeather","city:    "+xmlPullParser.getText());
                        }else if (xmlPullParser.getName().equals("updatetime")){
                            eventType = xmlPullParser.next();
                            Log.d("myWeather","updatetime:    "+xmlPullParser.getText());
                        }else if (xmlPullParser.getName().equals("shidu")){
                            eventType=xmlPullParser.next();
                            Log.d("myWeather","shidu:      "+xmlPullParser.getText());
                        }else if (xmlPullParser.getName().equals("wendu")){
                            eventType=xmlPullParser.next();
                            Log.d("myWeather","wendu:      "+xmlPullParser.getText());
                        }else if (xmlPullParser.getName().equals("pm25")){
                            eventType=xmlPullParser.next();
                            Log.d("myWeather","pm25:      "+xmlPullParser.getText());
                        }else if (xmlPullParser.getName().equals("quality")){
                            eventType=xmlPullParser.next();
                            Log.d("myWeather","quality:      "+xmlPullParser.getText());
                        }else if (xmlPullParser.getName().equals("fengxiang")&&fengxiangCount==0){
                            eventType=xmlPullParser.next();
                            Log.d("myWeather","fengxiang:      "+xmlPullParser.getText());
                            fengxiangCount++;
                        }else if (xmlPullParser.getName().equals("fengli")&&fengliCount==0){
                            eventType=xmlPullParser.next();
                            Log.d("myWeather","fengli:      "+xmlPullParser.getText());
                            fengliCount++;
                        }else if (xmlPullParser.getName().equals("date")&&dateCount==0){
                            eventType=xmlPullParser.next();
                            Log.d("myWeather","date:      "+xmlPullParser.getText());
                            dateCount++;
                        }else if (xmlPullParser.getName().equals("high")&&highCount==0){
                            eventType=xmlPullParser.next();
                            Log.d("myWeather","high:      "+xmlPullParser.getText());
                            highCount++;
                        }else if (xmlPullParser.getName().equals("low")&&lowCount==0){
                            eventType=xmlPullParser.next();
                            Log.d("myWeather","low:      "+xmlPullParser.getText());
                            lowCount++;
                        }else if (xmlPullParser.getName().equals("type")&&typeCount==0){
                            eventType=xmlPullParser.next();
                            Log.d("myWeather","type:      "+xmlPullParser.getText());
                            typeCount++;
                        }break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType=xmlPullParser.next();
            }
        }catch (XmlPullParserException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    */
}
