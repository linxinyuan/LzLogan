package com.yibasan.lzlogan.base;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.yibasan.lzlogan.Logz;
import com.yibasan.lzlogan.common.LogzConstant;
import com.yibasan.lzlogan.common.LogzConvert;
import com.yibasan.lzlogan.config.ILogzConfig;
import com.yibasan.lzlogan.trees.ITree;
import com.yibasan.lzlogan.trees.SoulsTree;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Author : Create by Linxinyuan on 2018/08/02
 * Email : linxinyuan@lizhi.fm
 * Desc : android dev
 */
public abstract class Tree implements ITree {
    private final ThreadLocal<String> localTags = new ThreadLocal<>();
    private ILogzConfig mTLogConfig;//Golbal log output level

    public Tree() {
        mTLogConfig = (configer() == null ? Logz.getLogGlobalConfigCenter() : configer());
    }

    public ILogzConfig getConfiger(){
        return mTLogConfig;
    }

    public ITree setTag(String tag) {
        if (!TextUtils.isEmpty(tag) && mTLogConfig.isEnable()) {
            localTags.set(tag);
        }
        return this;
    }

    @Override
    public void v(String message, Object... args) {
        prepareLog(Log.VERBOSE, null, message, args);
    }

    @Override
    public void v(Throwable t, String message, Object... args) {
        prepareLog(Log.VERBOSE, t, message, args);
    }

    @Override
    public void v(Throwable t) {
        prepareLog(Log.VERBOSE, t, null);
    }

    @Override
    public void v(Object o) {
        prepareLogObject(Log.VERBOSE, o);
    }

    @Override
    public void d(String message, Object... args) {
        prepareLog(Log.DEBUG, null, message, args);
    }

    @Override
    public void d(Throwable t, String message, Object... args) {
        prepareLog(Log.DEBUG, t, message, args);
    }

    @Override
    public void d(Throwable t) {
        prepareLog(Log.DEBUG, t, null);
    }

    @Override
    public void d(Object o) {
        prepareLogObject(Log.DEBUG, o);
    }

    @Override
    public void i(String message, Object... args) {
        prepareLog(Log.INFO, null, message, args);
    }

    @Override
    public void i(Throwable t, String message, Object... args) {
        prepareLog(Log.INFO, t, message, args);
    }

    @Override
    public void i(Throwable t) {
        prepareLog(Log.INFO, t, null);
    }

    @Override
    public void i(Object o) {
        prepareLogObject(Log.INFO, o);
    }

    @Override
    public void w(String message, Object... args) {
        prepareLog(Log.WARN, null, message, args);
    }

    @Override
    public void w(Throwable t, String message, Object... args) {
        prepareLog(Log.WARN, t, message, args);
    }

    @Override
    public void w(Throwable t) {
        prepareLog(Log.WARN, t, null);
    }

    @Override
    public void w(Object o) {
        prepareLogObject(Log.WARN, o);
    }

    @Override
    public void e(String message, Object... args) {
        prepareLog(Log.ERROR, null, message, args);
    }

    @Override
    public void e(Throwable t, String message, Object... args) {
        prepareLog(Log.ERROR, t, message, args);
    }

    @Override
    public void e(Throwable t) {
        prepareLog(Log.ERROR, t, null);
    }

    @Override
    public void e(Object o) {
        prepareLogObject(Log.ERROR, o);
    }

    @Override
    public void wtf(String message, Object... args) {
        prepareLog(Log.ASSERT, null, message, args);
    }

    @Override
    public void wtf(Throwable t, String message, Object... args) {
        prepareLog(Log.ASSERT, t, message, args);
    }

    @Override
    public void wtf(Throwable t) {
        prepareLog(Log.ASSERT, t, null);
    }

    @Override
    public void wtf(Object o) {
        prepareLogObject(Log.ASSERT, o);
    }

    @Override
    public void log(int priority, String message, Object... args) {
        prepareLog(priority, null, message, args);
    }

    @Override
    public void log(int priority, Throwable t, String message, Object... args) {
        prepareLog(priority, t, message, args);
    }

    @Override
    public void log(int priority, Throwable t) {
        prepareLog(priority, t, null);
    }

    @Override
    public void log(int priority, Object o) {
        prepareLogObject(priority, o);
    }

    @Override
    public void json(String json) {
        if (TextUtils.isEmpty(json)) {
            d("JSON{json is empty}");
            return;
        }
        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String msg = jsonObject.toString(LogzConstant.JSON_PRINT_INDENT);
                d(msg);
            } else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String msg = jsonArray.toString(LogzConstant.JSON_PRINT_INDENT);
                d(msg);
            }
        } catch (JSONException e) {
            e(e.toString() + "\n\njson = " + json);
        }
    }

    @Override
    public void xml(String xml) {
        if (TextUtils.isEmpty(xml)) {
            d("XML{xml is empty}");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            d(xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e) {
            e(e.toString() + "\n\nxml = " + xml);
        }
    }

    private void prepareLogObject(int priority, Object o) {
        prepareLog(priority, null, LogzConvert.objectToString(mTLogConfig, o));
    }

    private void prepareLog(int priority, Throwable t, String message, Object... args) {
        //not allow log output
        if (!mTLogConfig.isEnable()) {
            return;
        }
        //target log level mim than minLogOutputLevel
        if (priority < mTLogConfig.getMimLogLevel()) {
            return;
        }
        //get tag (custom/global/class_name)
        String tagPrefix = generateTag();
        if (TextUtils.isEmpty(tagPrefix)) {
            return;
        }
        //get combine log msg
        message = getCombineLogMsg(t, message, args);
        if (TextUtils.isEmpty(message)) {
            return;
        }
        //do not need cut message
        if (message.length() > LogzConstant.LINE_MAX) {
            if (mTLogConfig.isShowBorder()) {
                printLog(priority, tagPrefix, LogzConvert.printDividingLine(LogzConstant.DIVIDER_TOP));
                printLog(priority, tagPrefix, LogzConvert.printDividingLine(LogzConstant.DIVIDER_NORMAL) + getTopStackInfo());
                printLog(priority, tagPrefix, LogzConvert.printDividingLine(LogzConstant.DIVIDER_CENTER));
                for (String sub : message.split(LogzConstant.BR)) {
                    printLog(priority, tagPrefix, LogzConvert.printDividingLine(LogzConstant.DIVIDER_NORMAL) + sub);
                }
                printLog(priority, tagPrefix, LogzConvert.printDividingLine(LogzConstant.DIVIDER_BOTTOM));
            } else {
                printLog(priority, tagPrefix, message);
            }
            return;
        }
        // split by line, then ensure each line can fit into Log's maximum length.
        if (message.length() < LogzConstant.LINE_MAX) {
            List<String> subList = getSplitMessageList(message);
            if (mTLogConfig.isShowBorder()) {
                printLog(priority, tagPrefix, LogzConvert.printDividingLine(LogzConstant.DIVIDER_TOP));
                printLog(priority, tagPrefix, LogzConvert.printDividingLine(LogzConstant.DIVIDER_NORMAL) + getTopStackInfo());
                printLog(priority, tagPrefix, LogzConvert.printDividingLine(LogzConstant.DIVIDER_CENTER));
                for (String sub : subList) {
                    printLog(priority, tagPrefix, LogzConvert.printDividingLine(LogzConstant.DIVIDER_NORMAL) + sub);
                }
                printLog(priority, tagPrefix, LogzConvert.printDividingLine(LogzConstant.DIVIDER_BOTTOM));
                return;
            } else {
                for (String sub : subList) {
                    printLog(priority, tagPrefix, sub);
                }
            }
            return;
        }
    }

    private List<String> getSplitMessageList(String message) {
        List<String> stringList = new ArrayList<>();
        for (int i = 0, length = message.length(); i < length; i++) {
            int newline = message.indexOf('\n', i);
            newline = newline != -1 ? newline : length;
            do {
                int end = Math.min(newline, i + LogzConstant.LINE_MAX);
                String part = message.substring(i, end);
                stringList.add(part);
                i = end;
            } while (i < newline);
        }
        return stringList;
    }

    // custom > global > class
    private String generateTag() {
        String tempTag = localTags.get();
        //custom tag
        if (!TextUtils.isEmpty(tempTag)) {
            localTags.remove();
            return tempTag;
        }
        // global tag
        tempTag = mTLogConfig.getTagPrefix();
        if (!TextUtils.isEmpty(tempTag)) {
            return tempTag;
        }
        // class name
        tempTag = createStackElementTag(getCurrentStackTrace());
        if (!TextUtils.isEmpty(tempTag)){
            return tempTag;
        }
        // class tag
        return tempTag;
    }

    private String createStackElementTag(StackTraceElement element) {
        try {
            final int MAX_TAG_LENGTH = 23;
            final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");

            String tag = element.getClassName();
            Matcher m = ANONYMOUS_CLASS.matcher(tag);
            if (m.find()) {
                tag = m.replaceAll("");
            }
            tag = tag.substring(tag.lastIndexOf('.') + 1);
            // Tag length limit was removed in API
            if (tag.length() <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return tag;
            }
            return tag.substring(0, MAX_TAG_LENGTH);
        } catch (Exception exception) {
            e(exception.toString());
        }
        return null;
    }

    private String getTopStackInfo() {
        StackTraceElement caller = getCurrentStackTrace();
        if (caller == null) {
            return "";
        }
        String stackTrace = caller.toString();
        stackTrace = stackTrace.substring(stackTrace.lastIndexOf('('), stackTrace.length());
        String tag = "%s.%s%s";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), stackTrace);
        return tag;
    }

    private StackTraceElement getCurrentStackTrace() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int stackOffset_tln = getStackOffset(trace, Logz.class);
        int stackOffset_soul = getStackOffset(trace, SoulsTree.class);
        //if set custom tag
        if (stackOffset_tln != -1) {
            return trace[stackOffset_tln];
        }
        if (stackOffset_soul != -1) {
            return trace[stackOffset_soul];
        }
        return null;
    }

    private int getStackOffset(StackTraceElement[] trace, Class cla) {
        for (int i = LogzConstant.CALL_STACK_INDEX; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (cla.equals(Logz.class) && i < trace.length - 1 && trace[i + 1].getClassName().equals(Logz.class.getName())) {
                continue;
            }
            if (name.equals(cla.getName())) {
                return ++i;
            }
        }
        return -1;
    }

    private String getCombineLogMsg(Throwable t, String message, Object... args) {
        if (TextUtils.isEmpty(message)) {
            if (null == t)
                return null;
            return getThrowable2String(t);
        } else {
            if (args != null && args.length > 0) {
                message = String.format(message, args);
            }
            if (t != null) {
                message += "\n" + getThrowable2String(t);
            }
        }
        return message;
    }

    private String getThrowable2String(Throwable t) {
        StringWriter sw = new StringWriter(256);
        PrintWriter pw = new PrintWriter(sw, false);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    private void printLog(int priority, String tag, String message) {
        log(priority, tag, message);
    }

    protected abstract ILogzConfig configer();//return null if you want to use global config
    protected abstract void log(int type, String tag, String message);
}
