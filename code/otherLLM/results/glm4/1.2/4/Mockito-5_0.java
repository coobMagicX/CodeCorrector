public void checkDependency(ClassLoader classLoader) {
    // List of JUnit-related classes and packages that need to be checked for dependency.
    String[] jUnitDependencies = new String[]{
        "junit.framework.TestCase",
        "org.junit.Test",
        // Add other JUnit classes or package prefixes here if necessary
    };

    for (String dependency : jUnitDependencies) {
        try {
            classLoader.loadClass(dependency);
            throw new AssertionError("JUnit dependency detected: " + dependency);
        } catch (ClassNotFoundException e) {
            // Expected since we are trying to check that JUnit is not present.
            // No action needed here, continue with the next dependency check.
        }
    }
}