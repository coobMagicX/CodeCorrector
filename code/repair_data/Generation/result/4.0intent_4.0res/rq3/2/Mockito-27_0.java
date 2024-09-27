import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.handler.MockHandlerImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockHandler;

public class MockitoResetMock {
    public <T> void resetMock(T mock) {
        MockUtil mockUtil = new MockUtil();
        MockHandler<T> oldMockHandler = mockUtil.getMockHandler(mock);
        MockCreationSettings<T> settings = oldMockHandler.getMockSettings();
        MockHandler<T> newMockHandler = new MockHandlerImpl<>(settings);
        MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, (MockSettingsImpl) settings);
        ((org.mockito.cglib.proxy.Factory) mock).setCallback(0, newFilter);
    }
}