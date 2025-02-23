static boolean isSimpleNumber(String s) {
    if (s == null || s.isEmpty()) {
        return false;
    }
    
    if (s.charAt(0) == '0' && s.length() > 1) {
       