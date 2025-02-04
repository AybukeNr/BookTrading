package org.example.util;


public class TrackingNumberUtil {


    public static String generateTrackingNumber(){
        String chars = "0123456789";
        StringBuilder trackingNumber = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int index = (int) (Math.random() * chars.length());
            trackingNumber.append(chars.charAt(index));
        }

        return trackingNumber.toString();
    }



}
