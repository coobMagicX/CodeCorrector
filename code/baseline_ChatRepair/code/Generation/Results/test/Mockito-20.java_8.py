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
        // Ensure correct handling of the instantiation especially for inner classes
        mockInstance = createInstanceOf(mockedProxyType);

        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mockInstance;
        mockAccess.setMockitoInterceptor(new MockMethodInterceptor(asInternalMockHandler(handler), settings));

        return ensureMockIsAssignableToMockedType(settings, mockInstance);

    } catch (Exception ex) {  // Catching general Exception to handle all scenarios of failures
        throw new MockitoException("Error occurred during the creation of the mock: " + mockedProxyType.getName(), ex);
    }
}

private <T> T createInstanceOf(Class<T> clazz) throws IllegalAccessException, InvocationTargetException, org.mockito.internal.creation.instance.InstantiationException, InstantiationException {
    if (!Modifier.isStatic(clazz.getModifiers()) && clazz.isMemberClass()) {
        // For non-static inner class, need an instance of the enclosing class
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterTypes().length == 1 && constructor.getParameterTypes()[0] == clazz.getEnclosingClass()) {
                // Assume the enclosing class can be instantiated without arguments
                Object outerInstance = clazz.getEnclosingClass().getDeclaredConstructor().newInstance();
                constructor.setAccessible(true);
                return (T) constructor.newInstance(outerInstance);
            }
        }
    }
    return clazz.newInstance();
}
