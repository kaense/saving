package com.sample.saving;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity implements TextWatcher {
    EditText edit_saving, edit_income, edit_target;
    TextView kekka, bikou;
    //TextView before_target,before_saving,before_income,before_kekka;
    static protected SharedPreferences sharedPreferences;
    static protected SharedPreferences.Editor editor;
    static protected float key_target, key_saving, key_income;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //TextView privacy = (TextView)findViewById(R.id.privacypolicy) ;
        //MovementMethod mMethod = LinkMovementMethod.getInstance();
        //privacy.setMovementMethod(mMethod);
        //CharSequence link = Html.fromHtml("<a href=\"" + "https://komugiapp.blogspot.com/2018/09/blog-post.html" + "\">Application Privacy Policy</a>");
        //privacy.setText(link);

        kekka = (TextView) findViewById(R.id.kekka);
        bikou = (TextView) findViewById(R.id.bikou);

        edit_target = (EditText) findViewById(R.id.target);
        edit_saving = (EditText) findViewById(R.id.saving);
        edit_income = (EditText) findViewById(R.id.income);

        //before_kekka = (TextView)findViewById(R.id.before_kekka);
        //before_target = (TextView)findViewById(R.id.before_target);
        //before_saving = (TextView)findViewById(R.id.before_saving);
        //before_income = (TextView)findViewById(R.id.before_income);

        edit_target.addTextChangedListener(this);
        edit_saving.addTextChangedListener(this);
        edit_income.addTextChangedListener(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        try {
            key_target = sharedPreferences.getFloat("key_target", 3000f);
            //keyが存在しないと初期化されない？floatにできないもの、String?左と右どっちかがString?か何かでキャストが対応失敗？文字入れたのか？初期化？
            //新しい気ーのときはできてる　putint→getfloatとかしたときだめ
            key_saving = sharedPreferences.getFloat("key_saving", 100f);
            key_income = sharedPreferences.getFloat("key_income", 5f);
        } catch (ClassCastException e) {
            editor.clear();
            key_target = 3000f;
            key_saving = 100f;
            key_income = 5f;
            editor.putFloat("key_target", 3000f).apply();
            editor.putFloat("key_saving", 100f).apply();
            editor.putFloat("key_income", 5f).apply();
        }

        if (key_target == (int) key_target) {
            edit_target.setText(String.format("%s", (int) key_target));
        } else {
            edit_target.setText(String.format("%s", key_target));
        }
        if (key_saving == (int) key_saving) {
            edit_saving.setText(String.format("%s", (int) key_saving));
        } else {
            edit_saving.setText(String.format("%s", key_saving));
        }
        if (key_income == (int) key_income) {
            edit_income.setText(String.format("%s", (int) key_income));
        } else {
            edit_income.setText(String.format("%s", key_income));
        }


        //target.setText(String.valueOf(key_target));
        //saving.setText(String.valueOf(key_saving));
        //income.setText(String.valueOf(key_income));

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6789227322694215~2084399888");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.privacyPolicy:
                //Intent intent = new Intent(MainActivity.this, SubActivity.class);
                //startActivity(intent);
                //setContentView(R.layout.activity_webview);
                //WebView web = (WebView)findViewById(R.id.webview_id);
                // httpクライアントを設定
                //web.setWebViewClient(new WebViewClient());
                //web.loadUrl("https://komugiapp.blogspot.com/2018/09/blog-post.html");
                Uri uri = Uri.parse("https://komugiapp.blogspot.com/2018/09/blog-post.html");
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        keisan();
    }

    public void keisan() {
        try {
            float target=0, saving=0, income=0;
            int month, year;

            String targetText = edit_target.getText().toString();
            if(!targetText.matches("")){
                target = Float.parseFloat(targetText);
            }
            String savingText = edit_saving.getText().toString();
            if(!savingText.matches("")){
                saving = Float.parseFloat(savingText);
            }
            String incomeText = edit_income.getText().toString();
            if(!incomeText.matches("")){
                income = Float.parseFloat(incomeText);
            }

            editor.putFloat("key_target", target).apply();
            editor.putFloat("key_saving", saving).apply();
            editor.putFloat("key_income", income).apply();

            month = (int) Math.ceil((target - saving) / income);
            year = month / 12;
            month = month % 12;
            if (((target - saving) > 0) && (income <= 0)) {
                kekka.setText("∞");
            } else if ((target - saving) <= 0) {
                kekka.setText("0");
            } else {
                kekka.setText(year + " 年 " + month + " か月");
            }
            bikou.setText("半年後:" + (int) (income * 6 + saving) + "万円\n" +
                    "1年後:" + (int) (income * 12 + saving) + "万円\n" +
                    "2年後:" + (int) (income * 12 * 2 + saving) + "万円\n" +
                    "3年後:" + (int) (income * 12 * 3 + saving) + "万円\n" +
                    "4年後:" + (int) (income * 12 * 4 + saving) + "万円\n" +
                    "5年後:" + (int) (income * 12 * 5 + saving) + "万円\n" +
                    "10年後:" + (int) (income * 12 * 10 + saving) + "万円\n" +
                    "15年後:" + (int) (income * 12 * 15 + saving) + "万円\n" +
                    "20年後:" + (int) (income * 12 * 20 + saving) + "万円\n" +
                    "30年後:" + (int) (income * 12 * 30 + saving) + "万円\n" +
                    "50年後:" + (int) (income * 12 * 50 + saving) + "万円\n" +
                    "100年後:" + (int) (income * 12 * 100 + saving) + "万円");
            //before_kekka.setText();
            //before_target.setText();
        } catch (Exception e) {
        }
    }
}