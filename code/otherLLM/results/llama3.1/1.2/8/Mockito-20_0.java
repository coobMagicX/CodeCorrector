public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
    if (settings.getSerializableMode() == SerializableMode.ACROSS_CLASSLOADERS) {
        throw new MockitoException("Serialization across classloaders not yet supported with ByteBuddyMockMaker");
    }
    
    // Ensure that @Mock fields are injected before @Spy fields
    Class<? extends T> mockedProxyType = cachingMockBytecodeGenerator.get(
            settings.getTypeToMock(),
            settings.getExtraInterfaces()
    );
    
    T mockInstance = null;
    try {
        mockInstance = classInstantiator.instantiate(mockedProxyType);
        
        // Initialize @Mock field before using it
        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mockInstance;
        
        handler.injectInto(mockAccess); // Inject the provided MockHandler
        
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