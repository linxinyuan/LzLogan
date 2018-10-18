package com.yibasan.lzlogan.config;


import com.yibasan.lzlogan.parses.IParser;

import java.util.List;

/**
 * Author : Create by Linxinyuan on 2018/08/02
 * Email : linxinyuan@lizhi.fm
 * Desc : logz系统配置接口类
 */
public interface ILogzConfig {
    //设置是否输出日志
    ILogzConfig configAllowLog(boolean allowLog);

    //设置是否显示排版线条
    ILogzConfig configShowBorders(boolean showBorder);

    //设置日志最小显示级别
    ILogzConfig configMimLogLevel(int mimLogLevel);

    //设置日志tag前缀
    ILogzConfig configTagPrefix(String tagPrefix);

    //设置解析类(父类与类成员)层级(考虑到反射效率,取值范围限定是0-2,默认为1)
    ILogzConfig configClassParserLevel(int parserLevel);

    //设置文件切片粒度
    ILogzConfig configLogFileCutSize(long maxSize);

    //添加自定义解析器
    ILogzConfig addLogzParserClass(Class<? extends IParser>... classes);

    //获取日志最小输出级别
    int getMimLogLevel();

    //获取日志解析类的上下最大层级
    int getParserLevel();

    //获取是否输出格式化日志标志位
    boolean isShowBorder();

    //获取是否输出日志标志位
    boolean isEnable();

    //设置日志文件切片最大长度
    long getLogFileCutSize();

    //获取特定标签头设置
    String getTagPrefix();

    //获取自定义转换器列表
    List<IParser> getParserList();
}
