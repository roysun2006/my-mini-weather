package cn.edu.pku.sun.myminiweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hasee.myminiweather.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.pku.sun.app.MyApplication;
import cn.edu.pku.sun.bean.City;


/**
 * Created by roy on 2016/10/18.
 */
public class SelectCity extends Activity implements View.OnClickListener {
    private ImageView mBackBtn;
    private ListView lv;
    private TextView selectcity_tv;
    private String[] name={"第1组","第2组","第3组","第4组","第5组","第6组","第7组","第8组","第9组","第10组",
            "第11组","第12组","第13组","第14组","第15组","第16组","第17组","第18组","第19组","第20组",
            "第21组","第22组","第23组"};
    private ArrayList<String> cityname=new ArrayList<String>();
    private ArrayList<String> citycode=new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private List<Map<String,Object>> listems = new ArrayList<Map<String,Object>>();
    private MyApplication myapp;
    private List<City> mycity;
    private String mycode;
    private EditText medittext;
   // private String[] data=;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.select_city);
        selectcity_tv=(TextView )findViewById(R.id.title_name);
        mBackBtn = ( ImageView ) findViewById(R.id.title_back);
        lv = (ListView ) findViewById(R.id.listView);
        mBackBtn.setOnClickListener(this);
        //Log.d("test", mycity.toString());
        SimpleAdapter simplead = new SimpleAdapter(this,listems,R.layout.items
        ,new String[]{"city"},new int[]{R.id.name});
        lv.setAdapter(simplead);
       /* ArrayAdapter<String>adapter=new ArrayAdapter<String>(
                SelectCity.this,android.R.layout.simple_list_item_1,cityname);
        lv.setAdapter(adapter);
        */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public  void onItemClick(AdapterView<?>adapterView, View view,int i,long l){
                String weathercity= ( String ) listems.get(i).get("city");
                String weathercode= ( String ) listems.get(i).get("code");
                mycode = weathercode;
                selectcity_tv.setText("当前城市："+weathercity);
                Toast.makeText(SelectCity.this,"你点击了:"+weathercity,Toast.LENGTH_SHORT).show();
            }
        });

        medittext=(EditText )findViewById(R.id.search_edit);
        medittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("EditText", "beforeTextChanged");
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchContent = s.toString();
                ArrayList<String> newDataList = new ArrayList<String>();
                for (int i = 0; i <cityname.size(); i++){
                    if (cityname.get(i).substring(0, searchContent.length()).equals(searchContent)){
                        newDataList.add(cityname.get(i));
                    }
                }
                int j = 0;
                for(; j < newDataList.size(); j++){
                    name[j] = newDataList.get(j);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        myapp=(MyApplication) getApplication();
        mycity=myapp.getCityList();
        int i=0;
        for  (City city: mycity){
            Map<String,Object>listem=new HashMap<String, Object>();
            listem.put("city",city.getCity());
            listem.put("code",city.getNumber());
            mycode=city.getNumber();
            listems.add(listem);
        }
       /* int i = 0;
        for (City city : mycity){
            String cityName = city.getCity();
            String cityCode = city.getNumber();
            citycode.add(cityCode);
            cityname.add(cityName);
            i++;
            Log.d("test",cityCode+":"+cityName);
        }
        */
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                Intent i=new Intent();
                i.putExtra("cityCode",mycode);
                setResult(RESULT_OK,i);
                finish();
                break;
            default:
                break;
        }
    }
}