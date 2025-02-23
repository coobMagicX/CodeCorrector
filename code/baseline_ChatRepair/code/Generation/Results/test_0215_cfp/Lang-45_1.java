public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
    
    if (str == null) {
        return null;
    }
    if (str.length() == 0) {
        return StringUtils.EMPTY;
    }

    
    if (lower > str.length()) {
        lower = str.length();
    }
    if (upper == -1 || upper > str.length()) {
        upper = str.length();
    }
    
    if (upper < lower) {
        upper = lower;
    }

    StringBuffer result = new StringBuffer();
    
    int index = StringUtils.indexOf(str, " ", lower);
    if (index == -1 || index > upper) {
        
        result.append(str.substring(0, Math.min(upper, str.length())));
        
        if (upper < str.length()) {
            result.append(StringUtils.defaultString(appendToEnd));
        }
    } else {
        
        result.append(str.substring(0, index));
        result.append(StringUtils.defaultString(appendToEnd));
    }

    return result.toString();
}
