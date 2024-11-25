public static Locale toLocale(String str) {
    if (str == null) {
        return null;
    }
    int len = str.length();
    // Adjust condition for length checks to include 7 as a valid length
    if ((len != 2 && len != 5 && len != 7) || str.charAt(2) != '_' || str.charAt(5) != '_') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    char ch0 = str.charAt(0);
    char ch1 = str.charAt(1);
    // Validate the first two characters as language code
    if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    char ch3 = str.charAt(3);
    char ch4 = str.charAt(4);
    // Validate the third and fourth characters as country code
    if (ch3 < 'A' || ch3 > 'Z' || ch4 < 'A' || ch4 > 'Z') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }
    char ch6 = str.charAt(6);
    // Validate the sixth character as variant code
    if (ch6 < 'A' || ch6 > 'Z') {
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    // Use StringBuilder for constructing the three parts of the Locale
    StringBuilder sb = new StringBuilder();
    sb.append(ch0).append(ch1); // Append language code
    sb.append('_');
    sb.append(ch3).append(ch4); // Append country code
    if (len == 7) {
        sb.append('_').append(ch6); // Append variant code if the length is 7
    }

    return new Locale(sb.toString());
}