public static boolean toBoolean(String str) {
    if (str == null) {
        return false;
    }
    String lowerStr = str.toLowerCase(); 

    
    if ("true".equals(lowerStr)) {
        return true;
    }
    
    switch (str.length()) {
        case 2: 
            return "on".equals(lowerStr);
        case 3: 
            return "yes".equals(lowerStr);
        case 4: 
            return "true".equals(lowerStr);
    }
    return false;
}
