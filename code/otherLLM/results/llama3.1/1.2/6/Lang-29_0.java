static float toJavaVersionInt(String version) {
    if (version == null || version.isEmpty()) {
        return 0; // handle empty string or null input
    }

    int major = 0, minor = 0, patch = 0;
    try {
        String[] parts = getJavaVersionTrimmed().split("\\.");
        if (parts.length >= 2) {
            major = Integer.parseInt(parts[0]);
            minor = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
            // only extract the first two parts, ignore any additional sub-versions
            patch = 0; // set patch to 0 when there's no sub-version
        }
    } catch (NumberFormatException e) {
        // ignore and return 0 if version is invalid or contains non-digit characters
    }

    // calculate the integer representation using a simple formula
    int versionInt = ((major * 10000) + (minor * 100) + patch);

    return toVersionInt(versionInt);
}