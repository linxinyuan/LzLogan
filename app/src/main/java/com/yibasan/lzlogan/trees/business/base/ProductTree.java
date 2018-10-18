package com.yibasan.lzlogan.trees.business.base;//package com.lizhi.ls.trees.business.base;
//
//import com.lizhi.ls.Logz;
//import com.lizhi.ls.config.ILogzConfig;
//import com.lizhi.ls.trees.DebugTree;
//
///**
// * Author : Create by Linxinyuan on 2018/08/06
// * Email : linxinyuan@lizhi.fm
// * Desc : 根据业务线分类生成LogTree的父类
// * 1.业务日志树继承于ProductTree抽象类
// * 2.重写configer方法重新定义业务日志树的配置器
// * (1)super则使用DebugTree的日志输出配置器
// * (2)返回null使用全局默认配置器
// * 3.重写flatLog方法可以自定义log输出特性
// * TODO 4.记得在Logz中添加静态引用变量
// * TODO 5.业务树打印代码相互隔离(仅用于开发调试)
// */
//public abstract class ProductTree extends DebugTree{
//    @Override
//    protected ILogzConfig configer() {
//        return super.configer();
//    }
//
//    @Override
//    protected void log(int type, String tag, String message) {
//        super.log(type, tag, message);
//        flatLog(type, tag, message);
//    }
//
//    protected abstract void flatLog(int type, String tag, String message);
//}
