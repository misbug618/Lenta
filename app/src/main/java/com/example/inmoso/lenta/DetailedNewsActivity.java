package com.example.inmoso.lenta;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.inmoso.lenta.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by inmoso on 13.04.16.
 */
public class DetailedNewsActivity extends AppCompatActivity{

    @Override
    protected void  onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.detailed_news);
        String content = getIntent().getExtras().getString("content");
        String title = getIntent().getExtras().getString("title");
        String category = getIntent().getExtras().getString("category");
        String image = getIntent().getExtras().getString("image");
        String date = getIntent().getExtras().getString("date");
        String link = getIntent().getExtras().getString("link");

        TextView contentView = (TextView) findViewById(R.id.detailed_text);
        contentView.setText(content);

        TextView titleView = (TextView) findViewById(R.id.add_title);
        titleView.setText(title);

        TextView categoryView = (TextView) findViewById(R.id.add_subtitle);
        categoryView.setText(category);

        AQuery mAq = new AQuery(this); // создание объекта AQuery для работы с библиотекой

        ImageView imageView = (ImageView) findViewById(R.id.news_picture);

        if (!TextUtils.isEmpty(image)) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mAq.id(imageView).image(image);
        } else {
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            mAq.id(imageView).image(R.drawable.place_holder);
        }

        Date date1 = new Date(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String s = simpleDateFormat.format(date1);

        TextView textView = (TextView) findViewById(R.id.date);
        textView.setText(s);

        TextView linkTextView = (TextView) findViewById(R.id.textview_detail_news_link);
        linkTextView.setText(link);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
