package com.banshouweng.bswBase.utils.rxbus2;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author leiming
 * @date 2018/12/20.
 */
public class Logger {
    public String TAG = "Logger";
    public boolean LOG_DEBUG = true;
    private final String LINE_SEPARATOR = System.getProperty("line.separator");
    private final int VERBOSE = 2;
    private final int DEBUG = 3;
    private final int INFO = 4;
    private final int WARN = 5;
    private final int ERROR = 6;
    private final int ASSERT = 7;
    private final int JSON = 8;
    private final int XML = 9;

    private final int JSON_INDENT = 4;

    public void init(boolean isDebug, String tag) {
        TAG = tag;
        LOG_DEBUG = isDebug;
    }

    public void v(String msg) {
        log(VERBOSE, null, msg);
    }

    public void v(String tag, String msg) {
        log(VERBOSE, tag, msg);
    }

    public void d(String msg) {
        log(DEBUG, null, msg);
    }

    public void d(String tag, String msg) {
        log(DEBUG, tag, msg);
    }

    public void i(Object... msg) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : msg) {
            sb.append(obj + "");
        }
        log(INFO, null, String.valueOf(sb));
    }

    public void w(String msg) {
        log(WARN, null, msg);
    }

    public void w(String tag, String msg) {
        log(WARN, tag, msg);
    }

    public void e(String msg) {
        log(ERROR, null, msg);
    }

    public void e(String tag, String msg) {
        log(ERROR, tag, msg);
    }

    public void e(String tag, Throwable throwable) {
        StringWriter stackTrace = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stackTrace));
        String logContent = stackTrace.toString();
        e(tag, logContent);
    }

    public <T extends Exception> void e(String tag, T exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        String logContent = stackTrace.toString();
        e(tag, logContent);
    }

    public void a(String msg) {
        log(ASSERT, null, msg);
    }

    public void a(String tag, String msg) {
        log(ASSERT, tag, msg);
    }

    public void json(String json) {
        log(JSON, null, json);
    }

    public void json(String tag, String json) {
        log(JSON, tag, json);
    }

    public void xml(String xml) {
        log(XML, null, xml);
    }

    public void xml(String tag, String xml) {
        log(XML, tag, xml);
    }

    private void log(int logType, String tagStr, Object objects) {
        String[] contents = wrapperContent(tagStr, objects);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];
        if (LOG_DEBUG) {
            switch (logType) {
                case VERBOSE:
                case DEBUG:
                case INFO:
                case WARN:
                case ERROR:
                case ASSERT:
                    printDefault(logType, tag, headString + msg);
                    break;
                case JSON:
                    printJson(tag, msg, headString);
                    break;
                case XML:
                    printXml(tag, msg, headString);
                    break;
                default:
                    break;
            }
        }
    }

    private void printDefault(int type, String tag, String msg) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        int index = 0;
        int maxLength = 4000;
        int countOfSub = msg.length() / maxLength;

        if (countOfSub > 0) {  // The log is so long
            for (int i = 0; i < countOfSub; i++) {
                String sub = msg.substring(index, index + maxLength);
                printSub(type, tag, sub);
                index += maxLength;
            }
            //printSub(type, msg.substring(index, msg.length()));
        } else {
            printSub(type, tag, msg);
        }

    }

    private void printSub(int type, String tag, String sub) {
        if (tag == null) {
            tag = TAG;
        }
        switch (type) {
            case VERBOSE:
                Log.v(tag, sub);
                break;
            case DEBUG:
                Log.d(tag, sub);
                break;
            case INFO:
                Log.i(tag, sub);
                break;
            case WARN:
                Log.w(tag, sub);
                break;
            case ERROR:
                Log.e(tag, sub);
                break;
            case ASSERT:
                Log.wtf(tag, sub);
                break;
        }
    }

    private void printJson(String tag, String json, String headString) {
        if (TextUtils.isEmpty(json)) {
            d("Empty/Null json content");
            return;
        }
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        String message;

        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                message = jsonObject.toString(JSON_INDENT);
            } else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                message = jsonArray.toString(JSON_INDENT);
            } else {
                message = json;
            }
        } catch (JSONException e) {
            message = json;
        }

        printLine(tag, true);
        message = headString + LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        for (String line : lines) {
            Log.d(tag, "|" + line);
        }
        printLine(tag, false);
    }

    private void printXml(String tag, String xml, String headString) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        if (xml != null) {
            try {
                Source xmlInput = new StreamSource(new StringReader(xml));
                StreamResult xmlOutput = new StreamResult(new StringWriter());
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform(xmlInput, xmlOutput);
                xml = xmlOutput.getWriter().toString().replaceFirst(">", ">\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
            xml = headString + "\n" + xml;
        } else {
            xml = headString + "Log with null object";
        }

        printLine(tag, true);
        String[] lines = xml.split(LINE_SEPARATOR);
        for (String line : lines) {
            if (! TextUtils.isEmpty(line)) {
                Log.d(tag, "|" + line);
            }
        }
        printLine(tag, false);
    }

    private String[] wrapperContent(String tag, Object... objects) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement targetElement = stackTrace[5];
        String fileName = targetElement.getFileName();
        String methodName = targetElement.getMethodName();
        int lineNumber = targetElement.getLineNumber();
        if (lineNumber < 0) {
            lineNumber = 0;
        }
        String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        String msg = (objects == null) ? "Log with null object" : getObjectsString(objects);
        String headString = "[(" + fileName + ":" + lineNumber + ")#" + methodNameShort + " ] ";
        return new String[] {tag, msg, headString};
    }

    private String getObjectsString(Object... objects) {

        if (objects.length > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                if (object == null) {
                    stringBuilder.append("param").append("[").append(i).append("]").append(" = ").append("null").append("\n");
                } else {
                    stringBuilder.append("param").append("[").append(i).append("]").append(" = ").append(object.toString()).append("\n");
                }
            }
            return stringBuilder.toString();
        } else {
            Object object = objects[0];
            return object == null ? "null" : object.toString();
        }
    }

    private void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }
}
