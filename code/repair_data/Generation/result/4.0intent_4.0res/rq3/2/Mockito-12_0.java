import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericTypeFetcher {
    public Class<?> getGenericType(Field field) {
        Type generic = field.getGenericType();
        if (generic instanceof ParameterizedType) {
            Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
            if (actual instanceof Class<?>) {
                return (Class<?>) actual;
            }
        }
        return Object.class;
    }
}