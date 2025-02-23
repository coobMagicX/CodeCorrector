static boolean isSimpleNumber(String s) {
    int len = s.length();
    if (len == 0) {
        return false;
    }

    
    if (s.equals("0")) {
        return true; 
    }

    
    if (s.charAt(0) == '0') {
        return false;
    }

    
    for (int