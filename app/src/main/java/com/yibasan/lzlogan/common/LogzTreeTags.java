package com.yibasan.lzlogan.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

import static com.yibasan.lzlogan.common.LogzTreeTags.TAG_CRASH;
import static com.yibasan.lzlogan.common.LogzTreeTags.TAG_DEBUG;
import static com.yibasan.lzlogan.common.LogzTreeTags.TAG_FILE;
import static com.yibasan.lzlogan.common.LogzTreeTags.TAG_GLOBAL;
//import static com.lizhi.ls.common.LogzTreeTags.TAG_LIVE;
//import static com.lizhi.ls.common.LogzTreeTags.TAG_RECORD;
//import static com.lizhi.ls.common.LogzTreeTags.TAG_VOICE;

/**
 * Author : Create by Linxinyuan on 2018/08/06
 * Email : linxinyuan@lizhi.fm
 * Desc : android dev
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({TAG_GLOBAL,TAG_DEBUG, TAG_FILE, TAG_CRASH})
//TAG_LIVE, TAG_VOICE, TAG_RECORD
public @interface LogzTreeTags {
    //Global
    String TAG_GLOBAL = "LizhiFM";
    //Dispather Business
    String TAG_DEBUG = "LizhiFM_Debug";
    String TAG_CRASH = "LizhiFM_Crash";
    String TAG_FILE = "LizhiFM_File";
    //Product Business
//    String TAG_LIVE = "LizhiFM_Live";
//    String TAG_VOICE = "LizhiFM_Voice";
//    String TAG_RECORD = "LizhiFM_Record";
}
