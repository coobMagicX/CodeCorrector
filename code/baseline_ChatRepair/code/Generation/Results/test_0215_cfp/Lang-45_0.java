public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
    
    if (str == null) {
        return null;
    }
    if (str.length() == 0) {
        return StringUtils.EMPTY;
    }

    
    lower = Math.max(0, lower);

    
    if (upper == -1 || upper > str.length()) {
        upper = str.length();
    }
    if (lower > upper) {
        upper = lower;
    }

    StringBuffer result = new StringBuffer();
    
    int index = StringUtils.indexOf(str, " ", lower);
    
    
    if (index == -1 || index > upper) {
        result.append(str.substring(0, upper));
    } else {
        
        result.append(str.substring(0, index));
    }
    
    
    
    if (upper < str.length() || index != -1) {
        result.append(StringUtils.defaultString(appendToEnd));
    }

    return result.toString();
}
