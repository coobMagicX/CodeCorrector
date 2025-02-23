public static boolean toBoolean(String str) {
    if (str == null) {
        return false;
    }
    
    if (str.equals("true") || str.equals("TRUE") || str.equalsIgnoreCase("true")) {
        return true;
    }
    
    
    switch (str.length()) {
        case 2:
            
            return (str.equalsIgnoreCase("on"));
        case 3:
            
            return (str.equalsIgnoreCase("yes"));
        case 4:
            
            return (str.equalsIgnoreCase("true"));
    }
    
    
    return false;
}
