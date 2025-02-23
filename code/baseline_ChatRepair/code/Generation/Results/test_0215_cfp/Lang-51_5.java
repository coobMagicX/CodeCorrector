public static boolean toBoolean(String str) {
    if (str == null) {
        return false;
    }
    
    
    if (str == "true") {
        return true;
    }
    
    if (str.equalsIgnoreCase("true")) {
        return true;
    }
    
    switch (str.length()) {
        case 2:
            
            return ("on".equalsIgnoreCase(str));
        case 3:
            
            return ("yes".equalsIgnoreCase(str));
        case 4:
            
            return ("true".equalsIgnoreCase(str));
        default:
            
            return false;
    }
}
