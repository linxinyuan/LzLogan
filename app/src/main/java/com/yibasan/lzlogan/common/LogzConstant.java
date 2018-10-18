package com.yibasan.lzlogan.common;

import android.os.Environment;


import com.yibasan.lzlogan.config.ILogzConfig;
import com.yibasan.lzlogan.parses.CollectionParser;
import com.yibasan.lzlogan.parses.IParser;
import com.yibasan.lzlogan.parses.MapParser;
import com.yibasan.lzlogan.parses.intent.BundleParse;
import com.yibasan.lzlogan.parses.intent.IntentParser;

import java.util.List;

/**
 * Author : Create by Linxinyuan on 2018/08/02
 * Email : linxinyuan@lizhi.fm
 * Desc : Logz日志常量类
 */
public class LogzConstant {
    // 分割线方位
    public static final int DIVIDER_TOP = 1;
    public static final int DIVIDER_BOTTOM = 2;
    public static final int DIVIDER_CENTER = 4;
    public static final int DIVIDER_NORMAL = 3;

    public static final int LINE_MAX = 3 * 1024;// 最大日志长度
    public static final int CALL_STACK_INDEX = 5;// 堆栈寻址下标
    public static final int JSON_PRINT_INDENT = 4;// Json输出缩进
    public static final int MAX_CHILD_LEVEL = 1;//Object最大解析层级(父子)

    public static final long LOG_FILE_MAX = (long) Math.pow(1024, 2);
    public static final long LOG_FILE_INTERVAL = 3600000L;//时间切片

    public static final String TIP_OBJECT_NULL = "Object[object is null]";//空类
    public static final String BR = System.getProperty("line.separator");// 换行符

    public static final String DEFAULT_SAVE_ROOT_PATH =
            Environment.getExternalStorageDirectory().getPath() + "/LizhiFm/Logz/";

    public static final Class<? extends IParser>[] DEFAULT_PARSE_CLASS = new Class[]{
            CollectionParser.class, MapParser.class, IntentParser.class, BundleParse.class
    };

    public static List<IParser> getParserList(ILogzConfig configer) {
        return configer.getParserList();
    }
}
