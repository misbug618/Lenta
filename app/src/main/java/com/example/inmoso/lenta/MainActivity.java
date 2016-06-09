package com.example.inmoso.lenta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.util.XmlDom;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String URL_LENTA = "https://lenta.ru/rss";
    private static final String URL_RUSSIA = "russia";
    private static final String URL_WORLD = "world";
    private static final String URL_USSR = "ussr";
    private static final String URL_ECONOMICS = "economics";
    private static final String URL_BUSINESS = "business";
    private static final String URL_FORCES = "forces";
    private static final String URL_SCIENCE = "science";
    private static final String URL_SPORT = "sport";
    private static final String URL_CULTURE = "culture";
    private static final String URL_MEDIA = "media";
    private static final String URL_STYLE = "style";
    private static final String URL_TRAVEL = "travel";
    private static final String URL_LIFE = "life";

    private static final String URL_RUSSIA_NUMBER = "Россия";
    private static final String URL_WORLD_NUMBER = "Мир";
    private static final String URL_USSR_NUMBER = "Бывший СССР";
    private static final String URL_ECONOMICS_NUMBER = "Финансы";
    private static final String URL_BUSINESS_NUMBER = "Бизнес";
    private static final String URL_FORCES_NUMBER = "Силовые структуры";
    private static final String URL_SCIENCE_NUMBER = "Наука и техника";
    private static final String URL_SPORT_NUMBER = "Спорт";
    private static final String URL_CULTURE_NUMBER = "Культура";
    private static final String URL_MEDIA_NUMBER = "Интернет и СМИ";
    private static final String URL_STYLE_NUMBER = "Ценности";
    private static final String URL_TRAVEL_NUMBER = "Путешествия";
    private static final String URL_LIFE_NUMBER = "Из жизни";


    private ListView mNewsList;
    private NewsListAdapter mNewsListAdapter;
    private String mCurrentUrl = "https://lenta.ru/rss/articles/";
    private String mRefresingUrl = "";
    private String mCurrentCategory = "ALL";
    private DrawerLayout mDrawerLayout;
    private Context context;
    private ActionBarDrawerToggle mDrawerToggle;
    private View mPreviousView;

    @Override
    protected void onResume()                                                                                                                                                                                                                                  {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true); // выводит логотип лента
        getSupportActionBar().setIcon(R.drawable.my_icon);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navigation_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.context = this;
        mRefresingUrl = URL_LENTA;
        mNewsList = (ListView) findViewById(R.id.list_news);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.my_icon,
                R.string.drawer_open,
                R.string.drawer_close
        );
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mNewsListAdapter = new NewsListAdapter(this,new ArrayList<OneNews>());
        mNewsList.setAdapter(mNewsListAdapter);


        final Activity activity = this;
        mNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {  //анонимный класс
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent (activity, DetailedNewsActivity.class);
                intent.putExtra("content", mNewsListAdapter.getItem(position).getContent());
                intent.putExtra("title", mNewsListAdapter.getItem(position).getTitle());
                intent.putExtra("category", mNewsListAdapter.getItem(position).getCategory());
                intent.putExtra("date", mNewsListAdapter.getItem(position).getDate());
                intent.putExtra("image", mNewsListAdapter.getItem(position).getmImage());
                intent.putExtra("link", mNewsListAdapter.getItem(position).getmLink());
                startActivity(intent);
            }
        });

        if (isOnline())
            new GetRss().execute(URL_LENTA);  // вызываем асихронный класс для подкачки новостей
        else
            fromCashe();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.ic_refresh:
                new GetRss().execute(mRefresingUrl);
                break;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    public void drawerClick(View v) {
        if (mPreviousView != null) {
            mPreviousView.setBackgroundColor((getResources().getColor(R.color.white)));
        }
        v.setBackgroundColor(getResources().getColor(R.color.gray));
        mPreviousView = v;
        String url = mCurrentUrl;
        switch (v.getId()) {
            case R.id.all_news: {url = URL_LENTA;
                mCurrentCategory = "ALL";} break;
            case R.id.Russia: {url += URL_RUSSIA;
                mCurrentCategory = URL_RUSSIA_NUMBER;} break;
            case R.id.world: {url += URL_WORLD;
                mCurrentCategory = URL_WORLD_NUMBER;} break;
            case R.id.USSR: {url += URL_USSR;
                mCurrentCategory = URL_USSR_NUMBER;} break;
            case R.id.finance: {url += URL_ECONOMICS;
                mCurrentCategory = URL_ECONOMICS_NUMBER;} break;
            case R.id.business: {url += URL_BUSINESS;
                mCurrentCategory = URL_BUSINESS_NUMBER;} break;
            case R.id.strong: {url += URL_FORCES;
                mCurrentCategory = URL_FORCES_NUMBER;} break;
            case R.id.science: {url += URL_SCIENCE;
                mCurrentCategory = URL_SCIENCE_NUMBER;} break;
            case R.id.sport: {url += URL_SPORT;
                mCurrentCategory = URL_SPORT_NUMBER;} break;
            case R.id.culture: {url += URL_CULTURE;
                mCurrentCategory = URL_CULTURE_NUMBER;} break;
            case R.id.internet: {url += URL_MEDIA;
                mCurrentCategory = URL_MEDIA_NUMBER;} break;
            case R.id.values: {url += URL_STYLE;
                mCurrentCategory = URL_STYLE_NUMBER;} break;
            case R.id.travels: {url += URL_TRAVEL;
                mCurrentCategory = URL_TRAVEL_NUMBER;} break;
            case R.id.life: {url += URL_LIFE;
                mCurrentCategory = URL_LIFE_NUMBER;} break;
            default: {url = URL_LENTA;
                mCurrentCategory = "ALL";} break;
        }

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        }

        mRefresingUrl = url;

        if (isOnline()) {
            new GetRss().execute(url);
        } else
            fromCashe();
    }

        //асихронный класс

        private class GetRss extends AsyncTask<String, Void, ArrayList<OneNews>> {

            @Override
            protected ArrayList<OneNews> doInBackground(String... params) {
                String url = params[0];
                ArrayList<OneNews> upworkItems = new ArrayList<>();
                try {
                    URL trueUrl = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) trueUrl.openConnection();
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        XmlDom rootXml = new XmlDom(inputStream);
                        List<XmlDom> xmlItems = rootXml.tags("item");
                    /*for (int i = 0; i < xmlItems.size(); i++) {
                        xmlItems.get(i)
                    }*/
                        for (XmlDom item : xmlItems) {
                            String title = item.tag("title").text();
                            String content = item.tag("description").text();
                            String category = item.tag("category").text();
                            String link = item.tag("link").text();
                            String image = "";
                            String date = item.tag("pubDate").text();
                            if (item.tag("enclosure") != null)
                                image = item.tag("enclosure").attr("url");
                            OneNews oneNews = new OneNews(title, content, category, link, image, date);
                            upworkItems.add(oneNews);
                        }

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return upworkItems;
            }

            @Override
            protected void onPostExecute(ArrayList<OneNews> news) {
                GetSharedPref.getInstance(context).putNewsData(new Gson().toJson(news));
                mNewsListAdapter.setItems(news);
            }

        }

    private void fromCashe() {
        ArrayList<OneNews>  data = new Gson().fromJson(GetSharedPref.getInstance(context).getNewsData(),
                new TypeToken<List<OneNews>>(){}.getType());
        if (data == null) {
            Toast.makeText(this, "Нет сохраненных данных, подключитесь к интернету", Toast.LENGTH_LONG);
        } else {
            mNewsListAdapter.setItems(sortByCategory(mCurrentCategory, data));
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private List<OneNews> sortByCategory(String category, List<OneNews> data) {
        if (!category.equals("ALL")) {
            ArrayList<OneNews> temp = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getCategory().equals(category))
                    temp.add(data.get(i));
            }
            return temp;
        }
        return data;
    }

    }

