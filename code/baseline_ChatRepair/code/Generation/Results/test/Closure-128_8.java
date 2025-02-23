static boolean isSimpleNumber(String s) {
    
    if (s.isEmpty() || (s.charAt(0) == '0' && !s.equals("0"))) {
        return false;
    }

    
    for (int i = 0; i < s.length(); i++) {
        if (!Character.isDigit(s.charAt(i)))