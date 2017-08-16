package com.hq.fiveonejrq.jrq.demo.analysis;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hq.fiveonejrq.jrq.R;
import com.hq.fiveonejrq.jrq.common.Utils.Util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

public class XmlAnalysisActivity extends AppCompatActivity {

    private Button saxRead, saxWrite;

    private Button domRead, domWrite;

    private Button pullRead, pullWrite;

    private BookParser parser;

    private List<Books> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml_analysis);
        findView();
        setListener();
    }

    private File createFile(String fileName) {
        String rootPath = Util.getExternalStorageDirectory();
        if(!TextUtils.isEmpty(rootPath)){
            String filepath = Util.getFileTargetPath(this, fileName, "analysis");
            return new File(filepath);
        }
        return null;
    }

    private void setListener() {
        saxRead.setOnClickListener(listener);
        saxWrite.setOnClickListener(listener);
        domRead.setOnClickListener(listener);
        domWrite.setOnClickListener(listener);
        pullRead.setOnClickListener(listener);
        pullWrite.setOnClickListener(listener);
    }

    private void findView() {
        saxRead = (Button) findViewById(R.id.sax_read);
        saxWrite = (Button) findViewById(R.id.sax_write);
        domRead = (Button) findViewById(R.id.dom_read);
        domWrite = (Button) findViewById(R.id.dom_write);
        pullRead = (Button) findViewById(R.id.pull_read);
        pullWrite = (Button) findViewById(R.id.pull_write);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.sax_read:
                    try {
                        InputStream is = getAssets().open("books.xml");
                        parser = new SaxBookParser();
                        list = parser.parse(is);
                        for (Books book : list) {
                            Log.i("sax读", book.toString());
                        }
                        is.close();
                    } catch (Exception e) {
                        Log.e("sax读出错", e.getMessage());
                    }
                    break;
                case R.id.sax_write:
                        //write();
                        write(createFile("saxBooks.xml"));
                    break;
                case R.id.dom_read:
                    try {
                        InputStream is = getAssets().open("books.xml");
                        parser = new DomBookParser();
                        list = parser.parse(is);
                        for (Books book : list) {
                            Log.i("dom读", book.toString());
                        }
                        is.close();
                    } catch (Exception e) {
                        Log.e("dom读出错", e.getMessage());
                    }
                    break;
                case R.id.dom_write:
                    //write();
                    write(createFile("domBooks.xml"));
                    break;
                case R.id.pull_read:
                    try {
                        InputStream is = getAssets().open("books.xml");
                        parser = new DomBookParser();
                        list = parser.parse(is);
                        for (Books book : list) {
                            Log.i("pull读", book.toString());
                        }
                        is.close();
                    } catch (Exception e) {
                        Log.e("pull读出错", e.getMessage());
                    }
                    break;
                case R.id.pull_write:
                    //write();
                    write(createFile("pullBooks.xml"));
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 保存为xml
     */
    public void write(File file){
        if(null != file){
            try {
                if(!file.exists()){
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(file);
                InputStream is = new ByteArrayInputStream(parser.serialize(list).getBytes("UTF-8"));
                int len = 0;
                byte[] buffer = new byte[1024];
                while ( (len = is.read(buffer)) != -1){
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        }
    }

    /**
     * 保存为xml
     */
    public void write(){
        try {
            FileOutputStream fos = openFileOutput("newBooks", Context.MODE_PRIVATE);
            fos.write(parser.serialize(list).getBytes("UTF-8"));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("sax写", "完成");
    }
}
