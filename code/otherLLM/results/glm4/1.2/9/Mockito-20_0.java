public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
    if (settings.getSerializableMode() == SerializableMode.ACROSS_CLASSLOADERS) {
        throw new MockitoException("Serialization across classloaders not yet supported with ByteBuddyMockMaker");
    }
    
    // Ensure the mockedProxyType is loaded and accessible
    Class<? extends T> mockedProxyType = cachingMockBytecodeGenerator.get(
            settings.getTypeToMock(),
            settings.getExtraInterfaces()
    );

    T mockInstance = null;
    try {
        // Instantiate the mock instance before setting up interceptors or annotations
        mockInstance = classInstantiator.instantiate(mockedProxyType);

        // Access the MockAccess interface to set up Mockito interceptors
        if (mockInstance instanceof MockMethodInterceptor.MockAccess) {
            MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mockInstance;
            mockAccess.setMockitoInterceptor(new MockMethodInterceptor(asInternalMockHandler(handler), settings));

            // Verify the mock instance is assigned to the correct type
            return ensureMockIsAssignableToMockedType(settings, mockInstance);
        } else {
            throw new MockitoException("Mock instance does not implement required interface MockMethodInterceptor.MockAccess");
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