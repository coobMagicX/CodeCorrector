public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
    if (settings.getSerializableMode() == SerializableMode.ACROSS_CLASSLOADERS) {
        throw new MockitoException("Serialization across classloaders not yet supported with ByteBuddyMockMaker");
    }
    Class<? extends T> mockedProxyType = cachingMockBytecodeGenerator.get(
            settings.getTypeToMock(),
            settings.getExtraInterfaces()
    );
    T mockInstance = null;
    try {
        mockInstance = classInstantiator.instantiate(mockedProxyType);
        if (mockInstance instanceof MockMethodInterceptor.MockAccess) { // Check if the instance is of type MockAccess
            MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mockInstance;
            mockAccess.setMockitoInterceptor(new MockMethodInterceptor(asInternalMockHandler(handler), settings));
        }

        return ensureMockIsAssignableToMockedType(settings, mockInstance);
    } catch (ClassCastException cce) {
        throw new MockitoException(join(
                "ClassCastException occurred while creating the mockito mock :",
                "  class to mock : " + describeClass(mockedProxyType),
                "  created class : " + describeClass(settings.getTypeToMock()),
                "  proxy instance class : " + describeClass(mockInstance),
                "  instance creation by : " + classInstantiator.getClass().getSimpleName(),
                "",
                "You might experience classloading issues, please ask the mockito mailing-list.",
                ""
        ), cce);
    } catch (org.mockito.internal.creation.instance.InstantiationException e) {
        throw new MockitoException("Unable to create mock instance of type '" + mockedProxyType.getSuperclass().getSimpleName() + "'", e);
    }
}

// Helper methods
private static ClassInstantiator initializeClassInstantiator() {
    try {
        Class<?> objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
        Constructor<?> usingClassCacheConstructor = objenesisClassLoader.getDeclaredConstructor(boolean.class);
        return ClassInstantiator.class.cast(usingClassCacheConstructor.newInstance(new GlobalConfiguration().enableClassCache()));
    } catch (Throwable throwable) {
        // MockitoException cannot be used at this point as we are early in the classloading chain and necessary dependencies may not yet be loadable by the classloader
        throw new IllegalStateException(join(
                "Mockito could not create mock: Objenesis is missing on the classpath.",
                "Please add Objenesis on the classpath.",
                ""
        ), throwable);
    }
}

private <T> T ensureMockIsAssignableToMockedType(MockCreationSettings<T> settings, T mock) {
    // Force explicit cast to mocked type here, instead of
    // relying on the JVM to implicitly cast on the client call site.
    // This allows us to catch the ClassCastException earlier
    Class<T> typeToMock = settings.getTypeToMock();
    return typeToMock.cast(mock);
}