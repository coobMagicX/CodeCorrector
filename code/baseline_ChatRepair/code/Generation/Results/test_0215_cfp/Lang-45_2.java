public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
    
    if (str == null) {
        return null;
    }
    if (str.isEmpty()) {
        return StringUtils.EMPTY;
    }

    
    if (lower < 0) {
        lower = 0;  
    }
    if (upper == -1 || upper > str.length()) {
        upper = str.length();  
    }
    if (lower > upper) {
        lower = upper;  
    }

    
    if (upper - lower >= str.length()) {
        return str;  
    }

    int firstSpaceIndex = str.indexOf(" ", lower);  
    StringBuffer result = new StringBuffer();
    if (firstSpaceIndex == -1 || firstSpaceIndex > upper) {
        
        result.append(str.substring(0, upper));
    } else {
        
        result.append(str.substring(0, firstSpaceIndex));
    }
    
    if (upper < str.length()) {
        result.append(StringUtils.defaultString(appendToEnd));
    }
    return result.toString();
}

