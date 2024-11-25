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
        // Check if the mocked proxy type is an inner class
        Class<?> outerClass = getOuterClass(mockedProxyType);
        
        if (outerClass != null) {
            // Create a mock instance of the inner class with its dependencies injected
            MockMethodInterceptor.MockAccess mockAccess = createInnerClassMock(outerClass, handler, settings);
            
            return ensureMockIsAssignableToMockedType(settings, mockInstance);
        } else {
            mockInstance = classInstantiator.instantiate(mockedProxyType);
            MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mockInstance;
            mockAccess.setMockitoInterceptor(new MockMethodInterceptor(asInternalMockHandler(handler), settings));
            
            return ensureMockIsAssignableToMockedType(settings, mockInstance);
        }
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

private Class<?> getOuterClass(Class<?> clazz) {
    if (!clazz.getSimpleName().contains(".")) {
        return null;
    }
    
    String[] parts = clazz.getSimpleName().split("\\.");
    String outerClassName = parts[0];
    
    try {
        Class<?> outerClass = Class.forName(outerClassName);
        
        return outerClass;
    } catch (ClassNotFoundException e) {
        // Handle exception
    }
    
    return null;
}

private MockMethodInterceptor.MockAccess createInnerClassMock(Class<?> clazz, MockHandler handler, MockCreationSettings settings) {
    // Create a mock instance of the inner class with its dependencies injected
    Class<? extends MockMethodInterceptor.MockAccess> mockedProxyType = cachingMockBytecodeGenerator.get(
            clazz,
            settings.getExtraInterfaces()
    );
    
    T mockInstance = null;
    try {
        mockInstance = classInstantiator.instantiate(mockedProxyType);
        
        return (MockMethodInterceptor.MockAccess) mockInstance;
    } catch (ClassCastException cce) {
        throw new MockitoException("Unable to create mock instance of inner class '" + clazz.getSimpleName() + "'", cce);
    }
}