public static boolean toBoolean(String str) {
    if (str == null) {
        return false;
    }
    
    String normalizedStr = str.toLowerCase();

    
    if (normalizedStr.equals("true")) {
        return true;
    }

    switch (normalizedStr.length()) {
        case 2:
            
            return normalizedStr.equals("on");
        case 3:
            
            return normalizedStr.equals("yes");
        case 4:
            
            return normalizedStr.equals("true");
    }

    
    return false;
}
