package com.evision.dosage.utils;

public class DoubleUtils {

    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s){

        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }

        return s;
    }

    public static String divided(Double a, Double b) {
        double res = 0d;
        if (b.equals(0d)) {
            res = 0d;
        }
       else{
           res = a/b;
        };
       return getPercent(res);
    }
    /*
      * 获取百分比的格式，末尾为四位小数
     *
             * @param d
     * @return
             */

    public static String getPercent(Double d) {
        String str= String.format("%.4f", d * 100) ;
        str = DoubleUtils.subZeroAndDot(str);
        str = str+"%";
        return str;
    }

}
