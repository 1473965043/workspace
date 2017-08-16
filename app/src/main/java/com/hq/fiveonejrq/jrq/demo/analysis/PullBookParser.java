package com.hq.fiveonejrq.jrq.demo.analysis;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guodong on 2017/8/16. -- (PULL解析器)
 * PULL解析器的运行方式和SAX类似，都是基于事件的模式。不同的是，在PULL解析过程
 * 中，我们需要自己获取产生的事件然后做相应的操作，而不像SAX那样由处理器触发一
 * 种事件的方法，执行我们的代码。PULL解析器小巧轻便，解析速度快，简单易用，非常
 * 适合在Android移动设备中使用，Android系统内部在解析各种XML时也是用PULL解析器。
 */

public class PullBookParser implements BookParser {

    @Override
    public List<Books> parse(InputStream is) throws Exception {
        List<Books> booksList = null;
        Books book = null;
//        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        //由android.util.Xml创建一个XmlPullParser实例
        XmlPullParser parser = Xml.newPullParser();
        //设置输入流 并指明编码方式
        parser.setInput(is, "UTF-8");

        int eventType = parser.getEventType();
        while(eventType != XmlPullParser.END_DOCUMENT){
            switch(eventType){
                case XmlPullParser.START_DOCUMENT:
                    booksList = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    if(parser.getName().equals("book")){
                        book = new Books();
                    }else if(parser.getName().equals("id")){
                        eventType = parser.next();
                        book.setId(Integer.valueOf(parser.getText()));
                    }else if(parser.getName().equals("name")){
                        eventType = parser.next();
                        book.setName(parser.getText());
                    }else if(parser.getName().equals("price")){
                        eventType = parser.next();
                        book.setPrice(Float.valueOf(parser.getText()));
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if(parser.getName().equals("book")){
                        booksList.add(book);
                        book = null;
                    }
                    break;
            }
            eventType = parser.next();
        }
        return booksList;
    }

    @Override
    public String serialize(List<Books> books) throws Exception {
        //由android.util.Xml创建一个XmlSerializer实例
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        //设置输出方向为writer
        serializer.setOutput(writer);
        serializer.startDocument("UTF-8", true);
        serializer.startTag("", "book");
        for (Books book: books) {
            serializer.startTag("", "book");
            serializer.attribute("", "id", book.getId() + "");

            serializer.startTag("", "name");
            serializer.text(book.getName());
            serializer.endTag("", "name");

            serializer.startTag("", "price");
            serializer.text(book.getPrice() + "");
            serializer.endTag("", "price");

            serializer.endTag("", "book");
        }
        serializer.endTag("", "books");
        serializer.endDocument();
        return writer.toString();
    }
}
