package com.banshouweng.bswBase.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 正则判断类
 *
 * @author leiming
 * @date 2019/1/8 17:52
 */
public class RegularJudgeUtils {
    /**
     * 判断是否是中国手机号格式
     *
     * @param str 待判断字符串
     * @return 是否符合
     */
    public boolean isChinaMobilePhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^1[0-9]{10}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断是否是中国手机号/座机号格式
     *
     * @param str 待判断字符串
     * @return 是否符合
     */
    public boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^1\\d{10}$|^(0\\d{2,3}(-| |)?|\\(0\\d{2,3}\\))[1-9]\\d{4,7}((-| |)\\d{1,8})?$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断是否是邮箱格式
     *
     * @param str 待判断字符串
     * @return 是否符合
     */
    public boolean isMailLegal(String str) throws PatternSyntaxException {
        String regExp = "^([a-z0-9A-Z]+[_-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 正则表达式匹配“大写字母，小写字母，数字，特殊字符”四项中的至少三项
     * 特使字符：_!@#$%^&*`~()-+=.,;<>:
     *
     * @param str 待判断字符串
     * @return 是否符合
     */
    public boolean isPassword(String str) throws PatternSyntaxException {
        String regExp = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\\\W_!@#$%^&*`~()-+=.,;<>:]+$)(?![a-z0-9]+$)(?![a-z\\\\W_!@#$%^&*`~()-+=.,;<>:]+$)(?![0-9\\\\W_!@#$%^&*`~()-+=.,;<>:]+$)[a-zA-Z0-9\\\\W_!@#$%^&*`~()-+=.,;<>:]{6,15}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
