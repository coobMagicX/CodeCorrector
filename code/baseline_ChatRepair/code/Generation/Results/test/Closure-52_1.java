static boolean isSimpleNumber(String s) {
    int len = s.length();
    if (len == 0) {
        return false;
    }
    if (s.charAt(0) == '0' && len > 1) {
        return false;  
    }
    for (int index =