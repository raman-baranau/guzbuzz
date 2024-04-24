package com.abc.guzik.util;

import java.util.Random;

public class BuzzUtil {
    public static String generateRoomId() {
        int leftLimit = 65; // letter 'A'
        int rightLimit = 90; // letter 'Z'
        int targetStringLength = 4;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
