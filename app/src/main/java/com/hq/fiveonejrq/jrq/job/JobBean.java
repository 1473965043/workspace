package com.hq.fiveonejrq.jrq.job;

import android.util.Log;
import android.view.View;

/**
 * Created by guodong on 2017/3/16.
 */

public class JobBean implements View.OnClickListener{

    private int jobId;

    private String jobName;

    private String jobMoney;

    private String getAll(){
        return jobMoney + jobName + jobId;
    }

    public JobBean(){}

    private JobBean(String jobName){}

    public JobBean(int jobId){}

    @Override
    public void onClick(View v) {

    }

    public void privates(int jobId){
        Log.i("Reflect", "this is privates");
    }

    public void publics(String jobMoney){
        Log.i("Reflect", "this is publics");
    }
}
