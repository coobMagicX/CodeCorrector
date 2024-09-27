import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockHandler;
import org.mockito.plugins.MockHandlerInterface;
import org.mockito.stubbing.Answer;

public class MockResetter {
    public <T> void resetMock(T mock) {
        MockUtil mockUtil = new MockUtil();
        MockHandlerInterface<T> oldMockHandler = mockUtil.getMockHandler(mock);
        MockCreationSettings<T> settings = oldMockHandler.getMockSettings();
        MockHandler<T> newMockHandler = new MockHandler<>(settings);
        MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, (MockSettingsImpl) org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS));
        ((org.mockito.cglib.proxy.Factory) mock).setCallback(0, newFilter);
    }
}