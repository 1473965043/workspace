package com.hq.fiveonejrq.jrq.demo.analysis;

import java.io.InputStream;
import java.util.List;

/**
 * Created by guodong on 2017/8/15.
 */

public interface BookParser {

    /**
     * 解析输入流 得到Book对象集合
     * @param is
     * @return xml --> 对象
     * @throws Exception
     */
    List<Books> parse(InputStream is) throws Exception;

    /**
     * 序列化Book对象集合 得到XML形式的字符串
     * @param books
     * @return 对象 --> xml
     * @throws Exception
     */
    String serialize(List<Books> books) throws Exception;
}
