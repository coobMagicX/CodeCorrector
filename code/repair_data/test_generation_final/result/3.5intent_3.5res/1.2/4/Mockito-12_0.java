import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Main {
    
    public static void main(String[] args) throws NoSuchFieldException {
        Field field = MyClass.class.getDeclaredField("list");
        Class<?> genericType = getGenericType(field);
        System.out.println(genericType); // expected output: java.util.List<java.lang.String>
    }
    
    public static Class<?> getGenericType(Field field) {
        Type generic = field.getGenericType();
        if (generic instanceof ParameterizedType) {
            Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
            if (actual instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) actual).getRawType();
            } else if (actual instanceof Class) {
                return (Class<?>) actual;
            }
        }
        
        return Object.class;
    }
    
    public static class MyClass {
        public List<String> list;
    }
}