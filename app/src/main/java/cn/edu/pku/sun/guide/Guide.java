package cn.edu.pku.sun.guide;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.hasee.myminiweather.R;

import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.sun.myminiweather.MainActivity;

/**
 * Created by roy on 2016/11/29.
 */
public class Guide extends Activity implements ViewPager.OnPageChangeListener {
    private ViewPagerAdapter vpAdapter;
    private ViewPager vp;
    private List<View> views;

    private ImageView[] dots;
    private int[] ids = {R.id.iv1, R.id.iv2, R.id.iv3};

    private Button btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        initViews();
        initDots();
        btn=(Button ) views.get(2).findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent=new Intent(Guide.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    void initDots() {
        dots = new ImageView[views.size()];
        for (int i = 0; i < views.size(); i++) {
            dots[i] = ( ImageView ) findViewById(ids[i]);
        }
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.page1, null));
        views.add(inflater.inflate(R.layout.page2, null));
        views.add(inflater.inflate(R.layout.page3, null));
        vpAdapter = new ViewPagerAdapter(views, this);
        vp = ( ViewPager ) findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);
        vp.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int a = 0; a < ids.length; a++) {
            if (a == position) {
                dots[a].setImageResource(R.drawable.page_indicator_focused);
            } else {
                dots[a].setImageResource(R.drawable.page_indicator_unfocused);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
