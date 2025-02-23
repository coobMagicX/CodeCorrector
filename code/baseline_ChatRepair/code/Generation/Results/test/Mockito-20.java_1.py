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
        // Check if the class to mock is an inner class requiring an enclosing instance
        if (mockedProxyType.getEnclosingClass() != null && !Modifier.isStatic(mockedProxyType.getModifiers())) {
            Constructor<? extends T> defaultConstructor = mockedProxyType.getDeclaredConstructor(mockedProxyType.getEnclosingClass());
            // You would need to figure out the appropriate 'outerInstance' here
            Object outerInstance = createMock(
                settings.newBuilder().setTypeToMock(mockedProxyType.getEnclosingClass()).build(),
                handler);
            mockInstance = defaultConstructor.newInstance(outerInstance);
        } else {
            mockInstance = classInstantiator.instantiate(mockedProxyType);
        }
        
        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mockInstance;
        mockAccess.setMockitoInterceptor(new MockMethodInterceptor(asInternalMockHandler(handler), settings));

        return ensureMockIsAssignableToMockedType(settings, mockInstance);
    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
        throw new MockitoException("Mock creation failed for type: " + mockedProxyType.getName(), e);
    } catch (ClassCastException cce) {
        throw new MockitoException("Possible class loader issue. Failed to cast the created mock instance.", cce);
    }
}
