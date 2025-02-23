public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
    
    if (str == null) {
        return null;
    }
    if (str.length() == 0) {
        return ""; 
    }

    
    lower = Math.min(lower, str.length());

    
    if (upper == -1 || upper > str.length()) {
        upper = str.length();
    }

    
    upper = Math.max(upper, lower);

    StringBuffer result = new StringBuffer();
    
    int firstSpaceIndex = str.indexOf(" ", lower);
    
    if (firstSpaceIndex == -1 || firstSpaceIndex > upper) {
        result.append(str.substring(0, upper));
    }  else {
        result.append(str.substring(0, firstSpaceIndex));
    }

    
    if (upper < str.length()) {
        result.append(StringUtils.defaultString(appendToEnd));
    }

    return result.toString();
}
