public static boolean toBoolean(String str) {
    if (str == null) {
        return false;
    }
    if (str.equals("true")) { 
        return true;
    }
    int length = str.length();
    switch (length) {
        case 2:
            char ch0 = str.charAt(0);
            char ch1 = str.charAt(1);
            return (ch0 == 'o' || ch0 == 'O') &&
                   (ch1 == 'n' || ch1 == 'N');
                   
        case 3:
            char ch = str.charAt(0);
            if (ch == 'y' || ch == 'Y') {
                return (str.charAt(1) == 'e' || str.charAt(1) == 'E') &&
                       (str.charAt(2) == 's' || str.charAt(2) == 'S');
            }
            break; 
            
        case 4:
            ch = str.charAt(0);
            if (ch == 't' || ch == 'T') {
                return (str.charAt(1) == 'r' || str.charAt(1) == 'R') &&
                       (str.charAt(2) == 'u' || str.charAt(2) == 'U') &&
                       (str.charAt(3) == 'e' || str.charAt(3) == 'E');
            }
            break; 
    }
    return false;
}
