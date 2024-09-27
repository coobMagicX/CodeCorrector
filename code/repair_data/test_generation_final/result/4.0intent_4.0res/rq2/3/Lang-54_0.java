import java.util.Locale;

public class LocaleUtil {
    public static Locale toLocale(String str) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len < 2) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        char ch0 = str.charAt(0);
        char ch1 = str.charAt(1);
        if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (len == 2) {
            return new Locale(str, "");
        } else {
            if (str.charAt(2) != '_') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            if (len == 5) {
                char ch3 = str.charAt(3);
                char ch4 = str.charAt(4);
                if (ch3 < 'A' || ch3 > 'Z' || ch4 < 'A' || ch4 > 'Z') {
                    throw new IllegalArgumentException("Invalid locale format: " + str);
                }
                return new Locale(str.substring(0, 2), str.substring(3, 5));
            } else if (len > 5 && str.charAt(5) == '_') {
                char ch3 = str.charAt(3);
                char ch4 = str.charAt(4);
                if (ch3 < 'A' || ch3 > 'Z' || ch4 < 'A' || ch4 > 'Z') {
                    throw new IllegalArgumentException("Invalid locale format: " + str);
                }
                return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
            } else if (len > 5 && str.charAt(4) == '_') {
                return new Locale(str.substring(0, 2), "", str.substring(5));
            } else {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(toLocale("en_US_POSIX"));
        System.out.println(toLocale("en__POSIX"));
        System.out.println(toLocale("en_US"));
        System.out.println(toLocale("en"));
    }
}