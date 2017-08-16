package com.hq.fiveonejrq.jrq.demo.analysis;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by guodong on 2017/8/16. -- (DOM解析器)
 * DOM是基于树形结构的的节点或信息片段的集合，允许开发人员使用DOM API遍历XML树、检索所需数据。分析该结构通常需
 * 要加载整个文档和构造树形结构，然后才可以检索和更新节点信息。由于DOM在内存中以树形结构存放，因此检索和更新效
 * 率会更高。但是对于特别大的文档，解析和加载整个文档将会很耗资源。
 */

public class DomBookParser implements BookParser{

    @Override
    public List<Books> parse(InputStream is) throws Exception {
        List<Books> booksList = new ArrayList<>();
        //取得DocumentBuilderFactory实例
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //从factory获取DocumentBuilder实例
        DocumentBuilder builder = factory.newDocumentBuilder();
        //解析输入流 得到Document实例
        Document doc = builder.parse(is);
        Element rootElement = doc.getDocumentElement();
        NodeList items = rootElement.getElementsByTagName("book");
        for (int i = 0; i < items.getLength(); i++) {
            Books book = new Books();
            Node item = items.item(i);
            NodeList properties = item.getChildNodes();
            for (int j = 0; j < properties.getLength(); j++) {
                Node property = properties.item(j);
                String nodeName = property.getNodeName();
                if(nodeName.equals("id")){
                    book.setId(Integer.valueOf(property.getFirstChild().getNodeValue()));
                }else if(nodeName.equals("name")){
                    book.setName(property.getFirstChild().getNodeValue());
                }else if(nodeName.equals("price")){
                    book.setPrice(Float.valueOf(property.getFirstChild().getNodeValue()));
                }
            }
            booksList.add(book);
        }
        return booksList;
    }

    @Override
    public String serialize(List<Books> books) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        //由builder创建新文档
        Document doc = builder.newDocument();
        Element rootElement = doc.createElement("books");
        for (Books book: books) {
            Element bookElement = doc.createElement("book");
            bookElement.setAttribute("id", book.getId() + "");
            Element nameElement = doc.createElement("name");
            nameElement.setAttribute("name", book.getName());
            bookElement.appendChild(nameElement);
            Element priceElement = doc.createElement("price");
            priceElement.setAttribute("price", book.getPrice() + "");
            bookElement.appendChild(priceElement);

            rootElement.appendChild(bookElement);
        }
        doc.appendChild(rootElement);

        //取得TransformerFactory实例
        TransformerFactory transFactory = TransformerFactory.newInstance();
        //从transFactory获取Transformer实例
        Transformer transformer = transFactory.newTransformer();
        // 设置输出采用的编码方式
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        // 是否自动添加额外的空白
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        // 是否忽略XML声明
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "node");

        StringWriter writer = new StringWriter();

        //表明文档来源是doc
        Source source = new DOMSource(doc);
        //表明目标结果为writer
        Result result = new StreamResult(writer);
        transformer.transform(source, result);
        return writer.toString();
    }
}
