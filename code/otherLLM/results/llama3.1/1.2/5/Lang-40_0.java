public static boolean contains(String str, String searchStr) {
    int index = str.indexOf(searchStr);
    if (index == -1) {
        return false;
    }
    for (int i = 0; i < searchStr.length(); i++) {
        char c = searchStr.charAt(i);
        if (Character.isUpperCase(c)) {
            StringBuilder sb = new StringBuilder();
            for (char cc : str.toCharArray()) {
                if (Character.toUpperCase(cc) == Character.toUpperCase(c)) {
                    sb.append(Character.toLowerCase(cc));
                } else {
                    sb.append(cc);
                }
            }
            str = sb.toString();
        }
    }
    return str.contains(searchStr);
}

public static boolean containsIgnoreCase(String str, String searchStr) {
    if (str == null || searchStr == null) {
        return false;
    }
    return contains(str, searchStr);
}