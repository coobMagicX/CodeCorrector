public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
    if (settings.getSerializableMode() == SerializableMode.ACROSS_CLASSLOADERS) {
        throw new MockitoException("Serialization across classloaders not yet supported with ByteBuddyMockMaker");
    }
    Class<? extends T> mockedProxyType = cachingMockBytecodeGenerator.get(
            settings.getTypeToMock(),
            settings.getExtraInterfaces()
    );
    
    // Create a mock instance of the spy's inner class
    Class<?> innerClassType = getInnerClassType(mockedProxyType);
    T mockInstance = null;
    try {
        mockInstance = classInstantiator.instantiate(innerClassType);
        
        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mockInstance;
        // Use the correct type to set the Mockito interceptor
        Class<T> typeToMock = settings.getTypeToMock();
        typeToMock.cast(mockInstance);  // Ensure correct casting
        
        mockAccess.setMockitoInterceptor(new MockMethodInterceptor(asInternalMockHandler(handler), settings));
        
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

// Helper method to get the inner class type
private Class<?> getInnerClassType(Class<? extends Object> clazz) {
    // Assuming the inner class is a static nested class
    Class<?>[] classes = clazz.getDeclaredClasses();
    if (classes.length > 0) {
        return classes[0];
    }
    return null;
}