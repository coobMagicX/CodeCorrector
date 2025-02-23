public static boolean toBoolean(String str) {
    if (str == null) {
        return false;
    }
    int len = str.length();
    switch (len) {
        case 2:
            char ch0 = str.charAt(0);
            char ch1 = str.charAt(1);
            return (ch0 == 'o' || ch0 == 'O') && (ch1 == 'n' || ch1 == 'N');
        case 3:
            char ch2 = str.charAt(0);
            char ch3 = str.charAt(1);
            char ch4 = str.charAt(2);
            return ((ch2 == 'y' || ch2 == 'Y') && 
                    (ch3 == 'e' || ch3 == 'E') && 
                    (ch4 == 's' || ch4 == 'S'));
        case 4:
            return (str.charAt(0) == 't' || str.charAt(0) == 'T') &&
                   (str.charAt(1) == 'r' || str.charAt(1) == 'R') &&
                   (str.charAt(2) == 'u' || str.charAt(2) == 'U') &&
                   (str.charAt(3) == 'e' || str.charAt(3) == 'E');
        default:
            return false;  
    }
}
