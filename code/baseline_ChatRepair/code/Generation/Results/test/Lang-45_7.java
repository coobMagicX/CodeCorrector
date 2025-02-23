public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
    
    if (str == null) {
        return null;
    }
    if (str.length() == 0) {
        return StringUtils.EMPTY;
    }

    
    
    lower = Math.min(lower, str.length());
    
    
    
    upper = (upper == -1 || upper > str.length()) ? str.length() : upper;
    
    upper = Math.max(upper, lower);

    
    StringBuffer result = new StringBuffer();
    
    int index = StringUtils.indexOf(str, " ", lower);

    if (index == -1) {
        
        result.append(str.substring(0, Math.min(upper, str.length())));
    } else if (index > upper) {
        
        result.append(str.substring(0, upper));
    } else {
        
        result.append(str.substring(0, index));
    }
    
    
    if (result.length() < str.length()) {
        result.append(StringUtils.defaultString(appendToEnd));
    }

    return result.toString();
}
