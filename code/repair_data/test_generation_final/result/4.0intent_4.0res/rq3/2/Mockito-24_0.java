import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MethodsGuru;

public class MockAnswer implements Answer<Object> {
    private final MethodsGuru methodsGuru = new MethodsGuru();
    private final MockUtil mockUtil = new MockUtil();

    @Override
    public Object answer(InvocationOnMock invocation) {
        if (methodsGuru.isToString(invocation.getMethod())) {
            Object mock = invocation.getMock();
            MockName name = mockUtil.getMockName(mock);
            if (name.isDefault()) {
                return "Mock for " + mockUtil.getMockSettings(mock).getTypeToMock().getSimpleName() + ", hashCode: " + mock.hashCode();
            } else {
                return name.toString();
            }
        } else if (methodsGuru.isCompareToMethod(invocation.getMethod())) {
            // See issue 184.
            // Mocks by default should return 0 if references are the same, otherwise some other value because they are not the same. Hence we return 1 (anything but 0 is good).
            // Only for compareTo() method by the Comparable interface
            Object mock = invocation.getMock();
            Object argument = invocation.getArguments()[0];
            return mock == argument ? 0 : 1;
        }

        Class<?> returnType = invocation.getMethod().getReturnType();
        return returnValueFor(returnType);
    }

    private Object returnValueFor(Class<?> returnType) {
        if (returnType.isPrimitive()) {
            if (returnType.equals(boolean.class)) {
                return false;
            } else if (returnType.equals(int.class)) {
                return 0;
            } else if (returnType.equals(long.class)) {
                return 0L;
            } else if (returnType.equals(double.class)) {
                return 0.0;
            } else if (returnType.equals(float.class)) {
                return 0.0f;
            } else if (returnType.equals(char.class)) {
                return '\u0000';
            } else if (returnType.equals(byte.class)) {
                return (byte) 0;
            } else if (returnType.equals(short.class)) {
                return (short) 0;
            }
        } else {
            return null;
        }
        return null;
    }
}