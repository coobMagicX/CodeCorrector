public static boolean toBoolean(String str) {
    
    if (str == null) {
        return false;
    }

    
    str = str.trim();

    
    final String TRUE = "true";
    final String YES = "yes";
    final String ON = "on";

    switch (str.length()) {
        case 2: 
            return str.equalsIgnoreCase(ON);
        case 3: 
            return str.equalsIgnoreCase(YES);
        case 4: 
            return str.equalsIgnoreCase(TRUE);
        default:
            return false;
    }
}
