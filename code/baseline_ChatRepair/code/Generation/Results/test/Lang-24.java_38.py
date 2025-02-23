import java.util.regex.Pattern;

public static boolean isNumber(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }

    String regex = "[+-]?(\\d+\\.?\\d*|\\.\\d+)([eE][+-]?\\d+)?|[+-]?0[xX][0-9a-fA-F]+";

    // Compile and use regular expression
    Pattern pattern = Pattern.compile(regex);
    return pattern.matcher(str).matches();
}
