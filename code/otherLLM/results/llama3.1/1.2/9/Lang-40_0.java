public class Main {

    public static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        int strLen = str.length();
        int searchLen = searchStr.length();

        for (int i = 0; i <= strLen - searchLen; i++) {
            boolean match = true;
            for (int j = 0; j < searchLen; j++) {
                if (str.charAt(i + j) != searchStr.charAt(j)) {
                    match = false;
                    break;
                }
            }
            if (match) return true;
        }

        return false;
    }

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        int strLen = str.length();
        int searchLen = searchStr.length();

        for (int i = 0; i <= strLen - searchLen; i++) {
            boolean match = true;
            for (int j = 0; j < searchLen; j++) {
                if (Character.toLowerCase(str.charAt(i + j)) != Character.toLowerCase(searchStr.charAt(j))) {
                    match = false;
                    break;
                }
            }
            if (match) return true;
        }

        return false;
    }

    public static void main(String[] args) {
        System.out.println(containsIgnoreCase("Hello, World!", "world"));  // Expected output: true
        System.out.println(containsIgnoreCase("Hello, World!", "World"));  // Expected output: true
        System.out.println(containsIgnoreCase("Hello, World!", "hello"));   // Expected output: false
    }
}