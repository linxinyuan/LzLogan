package com.yibasan.lzlogan.parses.intent;

import android.os.Bundle;

import com.yibasan.lzlogan.common.LogzConstant;
import com.yibasan.lzlogan.common.LogzConvert;
import com.yibasan.lzlogan.config.ILogzConfig;
import com.yibasan.lzlogan.parses.IParser;


/**
 * Author : Create by Linxinyuan on 2018/08/07
 * Email : linxinyuan@lizhi.fm
 * Desc : android dev
 */
public class BundleParse implements IParser<Bundle> {
    @Override
    public Class<Bundle> parseClassType() {
        return Bundle.class;
    }

    @Override
    public String parseString(ILogzConfig configer, Bundle bundle) {
        if (bundle != null) {
            StringBuilder builder = new StringBuilder(bundle.getClass().getName() + " {" + LogzConstant.BR);
            for (String key : bundle.keySet()) {
                builder.append(String.format("'%s' => %s " + LogzConstant.BR, key, LogzConvert.objectToString(configer, bundle.get(key))));
            }
            builder.append("}");
            return builder.toString();
        }
        return null;
    }
}
