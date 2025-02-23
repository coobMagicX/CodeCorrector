static boolean isSimpleNumber(String s) {
    int len = s.length();
    if (len == 0) {
        return false;  
    }

    
    if (s.equals("0")) {
        return true;
    }

    
    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);
       