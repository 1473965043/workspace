package com.hq.fiveonejrq.jrq.analysis;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by guodong on 2017/8/15. (SAX解析)
 * SAX(Simple API for XML)解析器是一种基于事件的解析器，它的核心是事件处理模式，主要是围绕着事件源以及事件处理器来
 * 工作的。当事件源产生事件后，调用事件处理器相应的处理方法，一个事件就可以得到处理。在事件源调用事件处理器中特定方
 * 法的时候，还要传递给事件处理器相应事件的状态信息，这样事件处理器才能够根据提供的事件信息来决定自己的行为。SAX解
 * 析器的优点是解析速度快，占用内存少。非常适合在Android移动设备中使用。
 */

public class SaxBookParser implements BookParser {

    @Override
    public List<Books> parse(InputStream is) throws Exception {
        //取得SAXParserFactory实例
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //从factory获取SAXParser实例
        SAXParser saxParser = factory.newSAXParser();
        //实例化自定义DefaultHandler
        MyHandler myHandler = new MyHandler();
        //根据自定义Handler规则解析输入流
        saxParser.parse(is, myHandler);
        return myHandler.getBooks();
    }

    @Override
    public String serialize(List<Books> books) throws Exception {
        //取得SAXTransformerFactory实例
        SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        //从factory获取TransformerHandler实例
        TransformerHandler handler = factory.newTransformerHandler();
        //从handler获取Transformer实例
        Transformer transformer = handler.getTransformer();
        //设置输出采用的编码方式
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        //是否自动添加额外的空白
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        //是否忽略XML声明
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        StringWriter writer = new StringWriter();
        Result result = new StreamResult(writer);
        handler.setResult(result);
        //代表命名空间的URI 当URI无值时 须置为空字符串
        String url = "";
        //命名空间的本地名称(不包含前缀) 当没有进行命名空间处理时 须置为空字符串
        String localName = "";
        handler.startDocument();
        handler.startElement(url, localName, "books", null);
        //负责存放元素的属性信息
        AttributesImpl attrs = new AttributesImpl();
        char[] ch = null;
        for (Books book: books) {
            //清空属性列表
            attrs.clear();
            //添加一个名为id的属性(type影响不大,这里属性值设为string)
            attrs.addAttribute(url, localName, "id", "String", String.valueOf(book.getId()));
            //开始一个book元素 关联上面设定的id属性
            handler.startElement(url, localName, "book", attrs);
            //开始一个name元素 没有属性
            handler.startElement(url, localName, "name", null);
            ch = String.valueOf(book.getName()).toCharArray();
            //设置name元素的文本节点
            handler.characters(ch, 0, ch.length);
            handler.endElement(url, localName, "name");
            //开始一个price元素 没有属性
            handler.startElement(url, localName, "price", null);
            ch = String.valueOf(book.getPrice()).toCharArray();
            //设置price元素的文本节点
            handler.characters(ch, 0, ch.length);
            handler.endElement(url, localName, "price");
        }
        return null;
    }

    /**
     * 自定义DefaultHandler
     */
    private class MyHandler extends DefaultHandler {

        private List<Books> mBooksList;
        private Books mBooks;
        private StringBuilder mStringBuilder;

        /**
         * 返回解析后得到的Book对象集合
         */
        public List<Books> getBooks() {
            return mBooksList;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
        }
    }
}
