package com.banshouweng.bswBase.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.banshouweng.bswBase.utils.CommonUtils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public class HideHintEditText extends AppCompatEditText {
    private String hint;

    public HideHintEditText(Context context) {
        super(context);
        init();
    }

    public HideHintEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        CharSequence noEmojiString = emojiJudge(text);
        CommonUtils.log().i("onTextChanged", "text = " + text + " noEmojiString " + noEmojiString);
        // text是SpannableStringBuilder，不能直接判断
        if (!text.toString().equals(noEmojiString.toString())) {
            setText(noEmojiString);
            // 执行setText后，光标会自动移动到文本左侧，通过如下设置将光标移动至最右侧（暂时无法记录编辑前位置，lengthBefore无法使用）
            setSelection(noEmojiString.length());
        }
    }

    /**
     * 若输入文本包含表情、回车、空格、空白字符则滤除
     *
     * @param text 输入的文本
     * @return 滤除特殊符号后的文本
     */
    private CharSequence emojiJudge(CharSequence text) {
        try {
            return Pattern.compile("[\ud800\udc00-\udbff\udfff]|[\u2600-\u27ff]|\r|\n|\\s",
                    Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE).matcher(new String(text.toString().getBytes("UTF-8"), "UTF-8")).replaceAll("");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return text;
        }
    }

    /**
     * 获得hint值
     *
     * @author admin 2016-9-5 下午4:32:19
     */
    private void init() {
        CharSequence hint = getHint();
        if (!TextUtils.isEmpty(hint)) {
            this.hint = hint.toString();
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        if (!TextUtils.isEmpty(hint)) {
            if (focused) {
                setHint("");
            } else {
                setHint(hint);
            }
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }
}
