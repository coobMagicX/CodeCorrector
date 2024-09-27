import org.apache.commons.lang3.StringUtils;

public class StringAbbreviator {

    public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
        // initial parameter checks
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return StringUtils.EMPTY;
        }

        // if the lower value is greater than the length of the string,
        // set to the length of the string
        // if the upper value is -1 (i.e. no limit) or is greater
        // than the length of the string, set to the length of the string
        if (upper == -1 || upper > str.length()) {
            upper = str.length();
        }
        // if upper is less than lower, raise it to lower
        if (upper < lower) {
            upper = lower;
        }

        StringBuffer result = new StringBuffer();
        int index = StringUtils.indexOf(str.substring(lower), " ") + lower;
        if (index == -1 || index > upper) {
            result.append(str.substring(0, upper));
            // only if abbreviation has occurred do we append the appendToEnd value
            if (upper != str.length()) {
                result.append(StringUtils.defaultString(appendToEnd));
            }
        } else {
            result.append(str.substring(0, index));
            result.append(StringUtils.defaultString(appendToEnd));
        }
        return result.toString();
    }

    public static void main(String[] args) {
        String input = "Hello world, this is a test string";
        String output = abbreviate(input, 5, 14, "...");
        System.out.println(output);
    }
}