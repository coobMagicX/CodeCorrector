public void checkDependency() {
    Class<?>[] classesToCheck = getMockitoApiClasses(); // Hypothetical method to get Mockito's API classes

    for (Class<?> clazz : classesToCheck) {
        if (clazz.getName().contains("junit") || clazz.getName().contains("org.junit")) {
            throw new IllegalArgumentException("Dependency on JUnit detected in Mockito's API: " + clazz.getName());
        }
    }

    System.out.println("No JUnit-related classes found in Mockito's API.");
}

private Class<?>[] getMockitoApiClasses() {
    // Hypothetical method to return all the classes that belong to Mockito's API
    return new Class<?>[]{};
}