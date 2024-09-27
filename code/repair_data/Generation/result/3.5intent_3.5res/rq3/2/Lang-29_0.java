class Main {
    private static final int JAVA_VERSION_TRIM_SIZE = 3;

    public static void main(String[] args) {
        String version = "1.8.0_251";
        float javaVersionInt = toJavaVersionInt(version);
        System.out.println("Java Version Int: " + javaVersionInt);
    }

    static float toJavaVersionInt(String version) {
        return toVersionInt(toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE));
    }

    static int[] toJavaVersionIntArray(String version, int trimSize) {
        String[] versionParts = version.split("\\.");
        int[] versionInts = new int[versionParts.length];

        for (int i = 0; i < versionParts.length; i++) {
            versionInts[i] = Integer.parseInt(versionParts[i]);
        }

        if (versionInts.length > trimSize) {
            int[] trimmedVersionInts = new int[trimSize];
            System.arraycopy(versionInts, 0, trimmedVersionInts, 0, trimSize);
            versionInts = trimmedVersionInts;
        }

        return versionInts;
    }

    static float toVersionInt(int[] versionInts) {
        float versionInt = 0;

        for (int i = 0; i < versionInts.length; i++) {
            versionInt += versionInts[i] * Math.pow(10, -(i + 1));
        }

        return versionInt;
    }
}