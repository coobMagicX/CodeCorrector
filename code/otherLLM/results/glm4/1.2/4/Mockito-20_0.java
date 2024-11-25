import org.mockito.Mockito;
import org.mockito.exceptions.MockingException;
import java.lang.reflect.Method;

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
        // Create a spy object with the specified type
        T spyInstance = Mockito.spy(mockedProxyType);
        
        // Set up the interceptor for the spy instance
        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) spyInstance;
        mockAccess.setMockitoInterceptor(new MockMethodInterceptor(asInternalMockHandler(handler), settings));

        // Ensure that the spy object is assigned to the correct type
        return ensureMockIsAssignableToMockedType(settings, spyInstance);
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

// Helper method to join strings
private static String join(String... parts) {
    return String.join(System.lineSeparator(), parts);
}