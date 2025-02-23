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
        // Specially handle instantiation for non-static inner classes
        if (mockedProxyType.getEnclosingClass() != null && !Modifier.isStatic(mockedProxyType.getModifiers())) {
            Constructor<? extends T> constructor = mockedProxyType.getDeclaredConstructor(mockedProxyType.getEnclosingClass());
            constructor.setAccessible(true);
            Object outerClassInstance = getOuterClassInstance(mockedProxyType.getEnclosingClass());
            mockInstance = constructor.newInstance(outerClassInstance);
        } else {
            mockInstance = classInstantiator.instantiate(mockedProxyType);
        }

        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mockInstance;
        mockAccess.setMockitoInterceptor(new MockMethodInterceptor(asInternalMockHandler(handler), settings));
        
        return ensureMockIsAssignableToMockedType(settings, mockInstance);
    } catch (ClassCastException cce) {
        throw new MockitoException(join(
                "ClassCastException occurred while creating the mockito mock:",
                "  class to mock: " + describeClass(mockedProxyType),
                "  created class: " + describeClass(settings.getTypeToMock()),
                "  proxy instance class: " + describeClass(mockInstance),
                "  instance creation by: " + classInstantiator.getClass().getSimpleName(),
                "",
                "You might experience classloading issues, please ask the mockito mailing-list.",
                ""
        ), cce);
    } catch (InstantiationException e) {
        throw new MockitoException("Unable to create mock instance of type '" + mockedProxyType.getSuperclass().getSimpleName() + "'", e);
    } catch (IllegalAccessException e) {
        throw new MockitoException("Illegal access while creating mock instance of type '" + mockedProxyType.getSuperclass().getSimpleName() + "'", e);
    } catch (InvocationTargetException e) {
        throw new MockitoException("Invocation target exception while creating mock instance of type '" + mockedProxyType.getSuperclass().getSimpleName() + "'", e);
    } catch (NoSuchMethodException e) {
        throw new MockitoException("No such method exception while trying to access constructor for mock instance of type '" + mockedProxyType.getSuperclass().getSimpleName() + "'", e);
    }
}

// Helper method to create an instance of the outer class, this should be specified according to your exact use case
private Object getOuterClassInstance(Class<?> outerClass) {
    try {
        return outerClass.newInstance();
    } catch (InstantiationException e) {
        throw new MockitoException("Error instantiating outer class " + outerClass.getSimpleName(), e);
    } catch (IllegalAccessException e) {
        throw new MockitoException("Illegal access when instantiating outer class " + outerClass.getSimpleName(), e);
    }
}
