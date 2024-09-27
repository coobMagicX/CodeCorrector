public class VersionParser {
    private static final int JAVA_VERSION_TRIM_SIZE = 3; // Assuming we consider major, minor, and patch version numbers

    public static void main(String[] args) {
        String version = "1.8.0_231";
        float versionNumber = toJavaVersionInt(version);
        System.out.println("Parsed version number: " + versionNumber);
    }

    static float toJavaVersionInt(String version) {
        return toVersionInt(toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE));
    }

    static int[] toJavaVersionIntArray(String version, int trimSize) {
        String[] parts = version.split("[._-]");
        int[] versionArray = new int[trimSize];
        int count = Math.min(parts.length, trimSize);
        for (int i = 0; i < count; i++) {
            try {
                versionArray[i] = Integer.parseInt(parts[i]);
            } catch (NumberFormatException e) {
                versionArray[i] = 0; // Default to 0 if parsing fails
            }
        }
        return versionArray;
    }

    static float toVersionInt(int[] versionArray) {
        float versionInt = 0;
        for (int i = 0; i < versionArray.length; i++) {
            versionInt += versionArray[i] * Math.pow(10, (versionArray.length - i - 1) * 2);
        }
        return versionInt;
    }
}