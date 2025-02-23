public static boolean toBoolean(String str) {
    if (str == null) {
        return false;
    }

    
    if (str == "true") {
        return true;
    }
    
    if ("true".equalsIgnoreCase(str)) {
        return true;
    }

    switch (str.length()) {
        case 2: {
            char ch0 = str.charAt(0);
            char ch1 = str.charAt(1);
            return (ch0 == 'o' || ch0 == 'O') &&
                   (ch1 == 'n' || ch1 == 'N');
        }
        case 3: {
            char ch0 = str.charAt(0);
            char ch1 = str.charAt(1);
            char ch2 = str.charAt(2);
            return (ch0 == 'y' || ch0 == 'Y') &&
                   (ch1 == 'e' || ch1 == 'E') &&
                   (ch2 == 's' || ch2 == 'S');
        }
        case 4: {
            char ch0 = str.charAt(0);
            char ch1 = str.charAt(1);
            char ch2 = str.charAt(2);
            char ch3 = str.charAt(3);
            return (ch0 == 't' || ch0 == 'T') &&
                   (ch1 == 'r' || ch1 == 'R') &&
                   (ch2 == 'u' || ch2 == 'U') &&
                   (ch3 == 'e' || ch3 == 'E');
        }
    }

    return false;
}
