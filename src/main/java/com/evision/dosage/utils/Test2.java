package com.evision.dosage.utils;

/**
 * @author: AubreyXue
 * @date: 2020-03-10 20:58
 **/
public class Test2 {
    public static void main(String[] args) {
        double[] gps = GPSUtil.gps84_To_bd09(117.5, 41.05);

        System.out.println(gps[0]);
        System.out.println(gps[1]);
    }
}
