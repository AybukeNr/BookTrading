package org.example.util;

public class CardNumberUtil {

    public static String generateCardNumber(){
        String chars = "0123456789";
        StringBuilder trackingNumber = new StringBuilder();

        for (int i = 0; i < 11; i++) {
            int index = (int) (Math.random() * chars.length());
            trackingNumber.append(chars.charAt(index));
        }

        return trackingNumber.toString();
    }
}
