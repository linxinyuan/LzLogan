package com.yibasan.lzlogan.parses;

import com.yibasan.lzlogan.config.ILogzConfig;

/**
 * Author : Create by Linxinyuan on 2018/08/02
 * Email : linxinyuan@lizhi.fm
 * Desc : android dev
 */
public interface IParser<T> {
    Class<T> parseClassType();

    String parseString(ILogzConfig configer, T t);
}
