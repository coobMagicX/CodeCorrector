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
        // Check if the class to mock is an inner class requiring an enclosing instance.
        if (mockedProxyType.getEnclosingClass() != null && !Modifier.isStatic(mockedProxyType.getModifiers())) {
            Constructor<? extends T> defaultConstructor = mockedProxyType.getDeclaredConstructor(mockedProxyType.getEnclosingClass());

            // You would need to determine how to appropriately provide or create an instance of the outer class
            Object outerInstance = null; // Example placeholder, provide actual outer instance here
            mockInstance = defaultConstructor.newInstance(outerInstance);
        } else {
            mockInstance = classInstantiator.instantiate(mockedProxyType);
        }

        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mockInstance;
        mockAccess.setMockitoInterceptor(new MockMethodInterceptor(asInternalMockHandler(handler), settings));

        return ensureMockIsAssignableToMockedType(settings, mockInstance);
    } catch (NoSuchMethodException e) {
        throw new MockitoException("Mock creation failed: " + mockedProxyType.getName() + "due to missing constructor", e);
    } catch (InstantiationException e) {
        throw new MockitoException("Mock creation failed: " + mockedProxyType.getName() + " cannot be instantiated.", e);
    } catch (IllegalAccessException e) {
        throw new MockitoException("Mock creation failed: Illegal access for " + mockedProxyType.getName(), e);
    } catch (InvocationTargetException e) {
        throw new MockitoException("Mock creation failed: Constructor threw an exception for " + mockedProxyType.getName(), e);
    } catch (ClassCastException cce) {
        throw new MockitoException("Possible class loader issue. Failed to cast the created mock instance.", cce);
    }
}
