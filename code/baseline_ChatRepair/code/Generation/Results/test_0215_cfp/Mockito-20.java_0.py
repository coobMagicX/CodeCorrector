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
        // Check if the class to be mocked is an inner class and handle accordingly.
        if (mockedProxyType.getEnclosingClass() != null && !Modifier.isStatic(mockedProxyType.getModifiers())) {
            Constructor<? extends T> constructor = mockedProxyType.getDeclaredConstructor(mockedProxyType.getEnclosingClass());
            // Assuming `ownerInstance` is an instance of the enclosing class. Depending on the use-case, this may need dynamic resolution.
            Object ownerInstance = null; // This needs to be appropriately instantiated or passed.
            mockInstance = constructor.newInstance(ownerInstance);
        } else {
            mockInstance = classInstantiator.instantiate(mockedProxyType);
        }

        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mockInstance;
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
        ),cce);
    } catch (Exception e) { // Catching general exceptions to handle instantiation issues.
        throw new MockitoException("Unable to create mock instance of type '" + mockedProxyType.getName() + "'", e);
    }
}
