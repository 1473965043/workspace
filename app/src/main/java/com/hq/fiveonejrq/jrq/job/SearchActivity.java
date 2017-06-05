package com.hq.fiveonejrq.jrq.job;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.hq.fiveonejrq.jrq.R;
import com.hq.fiveonejrq.jrq.common.Utils.StatusBarUtils;
import com.hq.fiveonejrq.jrq.common.widget.SearchBar;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView mRecyclerView;
    private SearchBar mSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
        initViews();
        initEvents();
//        initData();
    }

    private void initViews() {
        progressBar = (ProgressBar) findViewById(R.id.internal_search_progress);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSearchBar = (SearchBar) findViewById(R.id.search_bar);
    }

    private void initEvents() {
        StatusBarUtils.from(this)
                .setActionbarView(mSearchBar)
                .setTransparentStatusbar(true)
                .setLightStatusBar(true)
                .process();
        mSearchBar.setOnButton(new Runnable() {
            @Override public void run() {
                finish();
            }
        });
        RxTextView.textChanges(mSearchBar.getEditTextSearch())
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleWithTimeout(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .distinct()
                .filter(new Func1<CharSequence, Boolean>() {
                    @UiThread
                    @Override public Boolean call(CharSequence charSequence) {
                        //void unnecessary request
                        return charSequence.length() != 0;
                    }
                })
                .map(new Func1<CharSequence, String>() {
                    @UiThread @Override public String call(CharSequence charSequence) {
                        //fit network api doc require
                        return charSequence + "*";
                    }
                })
                .doOnNext(new Action1<CharSequence>() {
                    @UiThread @Override public void call(CharSequence charSequence) {
                    }
                })
                .observeOn(Schedulers.io())
                .retry(new Func2<Integer, Throwable, Boolean>() {
                    //fix InterruptedIOException bugs on Retrofit
                    // when stop old search
                    @WorkerThread @Override public Boolean call(Integer integer, Throwable throwable) {
                        return throwable instanceof InterruptedIOException;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void initData() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(0, 0);
        }
    }
}
