import org.apache.commons.lang3.StringUtils;

public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
    // Initial parameter checks
    if (str == null) {
        return null;
    }
    if (str.length() == 0) {
        return ""; // Use an empty string directly
    }

    // Adjust the upper limit
    if (upper == -1 || upper > str.length()) {
        upper = str.length();
    }

    // Adjust the lower limit if it is out of range
    if (lower > str.length()) {
        lower = str.length();
    }

    // Ensure upper is at least as large as lower
    if (upper < lower) {
        upper = lower;
    }

    StringBuffer result = new StringBuffer();
    int index = str.indexOf(" ", lower); // Use String.indexOf directly
    if (index == -1) {
        result.append(str.substring(0, upper));
        // Append the additional string only if there is an actual abbreviation
        if (upper != str.length()) {
            result.append(StringUtils.defaultString(appendToEnd));
        }
    } else if (index > upper) {
        result.append(str.substring(0, upper));
        result.append(StringUtils.defaultString(appendToEnd));
    } else {
        result.append(str.substring(0, index));
        result.append(StringUtils.defaultString(appendToEnd));
    }

    return result.toString();
}