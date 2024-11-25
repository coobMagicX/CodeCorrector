public <T> T createMock(MockCreationSettings<T> settings, MockHandler handler) {
    if (settings.getSerializableMode() == SerializableMode.ACROSS_CLASSLOADERS) {
        throw new MockitoException("Serialization across classloaders not yet supported with ByteBuddyMockMaker");
    }
    Class<? extends T> mockedProxyType = cachingMockBytecodeGenerator.get(
            settings.getTypeToMock(),
            settings.getExtraInterfaces()
    );
    try {
        // Ensure the instance is created before we attempt to cast it.
        T mockInstance = classInstantiator.instantiate(mockedProxyType);
        
        if (mockInstance instanceof MockMethodInterceptor.MockAccess) {
            MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mockInstance;
            mockAccess.setMockitoInterceptor(new MockMethodInterceptor(asInternalMockHandler(handler), settings));
        } else {
            // If the instance is not of type MockMethodInterceptor.MockAccess, throw an exception
            throw new MockitoException("Unexpected class type for mock instance: " + describeClass(mockedProxyType));
        }

        return ensureMockIsAssignableToMockedType(settings, mockInstance);
    } catch (ClassCastException cce) {
        throw new MockitoException(join(
                "ClassCastException occurred while creating the mockito mock :",
                "  class to mock : " + describeClass(mockedProxyType),
                "  created class : " + describeClass(settings.getTypeToMock()),
                "  proxy instance class : " + describeClass(classInstantiator.getClass().getSimpleName()),
                "",
                "You might experience classloading issues, please ask the mockito mailing-list.",
                ""
        ), cce);
    } catch (org.mockito.internal.creation.instance.InstantiationException e) {
        throw new MockitoException("Unable to create mock instance of type '" + mockedProxyType.getSuperclass().getSimpleName() + "'", e);
    }
}

private <T> T ensureMockIsAssignableToMockedType(MockCreationSettings<T> settings, T mock) {
    // Force explicit cast to mocked type here
    Class<T> typeToMock = settings.getTypeToMock();
    return typeToMock.cast(mock);
}