static boolean isSimpleNumber(String s) {
    
    if (s.isEmpty()) {
        return false;
    }

    
    if (s.charAt(0) == '0') {
        
        return s.equals("0");
    }

    