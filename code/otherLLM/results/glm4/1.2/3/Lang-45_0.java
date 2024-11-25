import org.apache.commons.lang3.StringUtils;

public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
    // initial parameter checks
    if (str == null || lower < 0 || upper > str.length()) {
        return null;
    }
    if (str.isEmpty()) {
        return StringUtils.EMPTY;
    }

    // Adjust the upper index to be within the bounds of the string length
    if (upper < lower) {
        upper = lower; // If upper is less than lower, set upper to lower
    } else if (upper == -1 || upper > str.length()) {
        upper = str.length(); // If upper is not specified or exceeds the string length, set it to the end of the string
    }

    StringBuffer result = new StringBuffer();
    int index = StringUtils.indexOf(str, " ", lower);
    
    // Find the next space that is within the bounds of upper index
    while (index != -1 && index < upper) {
        result.append(str.substring(0, index));
        result.append(StringUtils.defaultString(appendToEnd));
        
        // Reset appendToEnd to ensure it's only appended once
        appendToEnd = StringUtils.defaultString(appendToEnd);
        
        // Find the next space after the current one
        index = StringUtils.indexOf(str, " ", index + 1);
    }
    
    // If we have reached here and there are still characters left to abbreviate,
    // add the substring from the last found space to the upper limit.
    if (index != -1) {
        result.append(str.substring(index));
    } else {
        // No spaces were found, just append up to the upper index
        result.append(str.substring(0, upper));
    }
    
    return result.toString();
}