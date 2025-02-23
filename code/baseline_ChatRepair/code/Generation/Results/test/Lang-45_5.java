public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
    
    if (str == null) {
        return null;
    }
    if (str.length() == 0) {
        return StringUtils.EMPTY;
    }

    
    if (lower < 0) {
        lower = 0;
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

    
    if (index == -1) {
        
        if (upper >= str.length()) {
            return str;
        }
        
        result.append(str.substring(0, upper));
    } else if (index > upper) {
        
        result.append(str.substring(0, upper));
    } else {
        
        result.append(str.substring(0, index));
    }

    
    if (upper < str.length() && appendToEnd != null) {
        result.append(appendToEnd);
    }

    return result.toString();
}
