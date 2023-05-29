package com.example.demo.util;


public class SqlValidateUtil {

    //校验单个数据防止sql注入
    public static String sqlValidate(String str) {
        str = str.toLowerCase();//统一转为小写
        String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|chr|truncate|" +
                "declare|sitename|net user|xp_cmdshell|;| or | - |+| , | like' |create|" +
                "table|from|grant|use|group_concat|column_name|" +
                "information_schema.columns|table_schema|union|where| order | by |" +
                "chr|mid|master|truncate|char|declare|--| like |//|/|#";//过滤掉的sql关键字，可以手动添加
        String[] badStrs = badStr.split("\\|");
        for (String s : badStrs) {
            if (str.contains(s)) {
                str = str.substring(0, str.indexOf(s) - 1);
            }
        }
        return str;
    }
}
