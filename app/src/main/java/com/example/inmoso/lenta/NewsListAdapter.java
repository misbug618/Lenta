package com.example.inmoso.lenta;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.List;

/**
 * Created by inmoso on 19.03.16.
 */

//храним элементы, которые будут показаны в списке

public class NewsListAdapter extends BaseAdapter {

    private Activity mContext;
    private List<OneNews> mItems;
    private AQuery mAq;

    public NewsListAdapter (Activity context, List<OneNews> items){
        this.mContext = context;
        this.mItems = items;
        mAq = new AQuery(context);
    }

    @Override
    //количество элементов из списка
    public int getCount() {
        return mItems.size();
    }

    @Override
    // получить 1 элемент из списка по позиции
    public OneNews getItem(int position) {
        return mItems.get(position);
    }

    public void setItems(List<OneNews> list){
        mItems = list;
        notifyDataSetChanged();
    }

    @Override
    // получаем id элемента
    public long getItemId(int position) {
        return 0;
    }

    @Override
    // подготавливает и отдает шаблон one_news
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rootView = convertView;
        Holder holder;
        if (rootView == null) {
            holder = new Holder();
            LayoutInflater inflater = mContext.getLayoutInflater();
            rootView = inflater.inflate(R.layout.one_news, null);
            holder.titleTextView = (TextView) rootView.findViewById(R.id.title_text_view); // инициализация заголовка
            holder.subtitleTextView = (TextView) rootView.findViewById(R.id.subtitle_text_view); // инициализация подзаголовка (из текста)
            holder.imageView = (ImageView) rootView.findViewById(R.id.news_picture);
            rootView.setTag(holder);
        } else
            holder = (Holder) rootView.getTag();

        holder.titleTextView.setText(mItems.get(position).getTitle()); // заполнение textview данными
        holder.subtitleTextView.setText(mItems.get(position).getCategory()); //заполнение подзаголовка
        if (mItems.get(position).getmImage() != "") {
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mAq.id(holder.imageView).image(mItems.get(position).getmImage());
        } else {
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            mAq.id(holder.imageView).image(R.drawable.place_holder);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (mContext, DetailedNewsActivity.class);
                intent.putExtra("content", getItem(position).getContent());
                intent.putExtra("title", getItem(position).getTitle());
                intent.putExtra("category", getItem(position).getCategory());
                intent.putExtra("date", getItem(position).getDate());
                intent.putExtra("image", getItem(position).getmImage());
                intent.putExtra("link", getItem(position).getmLink());
                mContext.startActivity(intent);
            }
        });

        return rootView;
    }

    // холдер с элементами нашего шаблона
    class Holder{
        public TextView titleTextView;
        public TextView subtitleTextView;
        public ImageView imageView;

    }

    OneNews example = new OneNews("ghjcnjh", "ghjcnjh", "ghjcnjh", "ghjcnjh", "ghjcnjh", "ghjcnjh");
}
