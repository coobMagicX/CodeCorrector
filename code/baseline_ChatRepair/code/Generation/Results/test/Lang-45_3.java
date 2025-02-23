public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
    
    if (str == null) {
        return null;
    }
    if (str.length() == 0) {
        return StringUtils.EMPTY;
    }

    
    lower = Math.min(lower, str.length());
    if (upper == -1 || upper > str.length()) {
        upper = str.length();
    }
    if (lower > upper) {
        upper = lower;
    }
    
    StringBuffer result = new StringBuffer();
    
    if (upper < str.length()) {
        int index = StringUtils.indexOf(str, " ", lower);
        if (index == -1 || index > upper) {
            result.append(str.substring(0, upper));
        } else {
            result.append(str.substring(0, index));
        }
        if (StringUtils.isNotEmpty(appendToEnd)) {
            result.append(appendToEnd);
        }
    } else {
        
        
        return str;
    }
    return result.toString();
}
