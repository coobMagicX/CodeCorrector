static boolean isSimpleNumber(String s) {
    if (s == null || s.isEmpty()) {
        return false; 
    }
    
    if (s.equals("0")) {
        return true; 