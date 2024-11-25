import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Map;

Object returnValueFor(Class<?> type) {
    if (type == Iterable.class) {
        // Return an empty iterable for Iterable.class
        return Collections.emptyList();
    } else if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
        // new instances are used instead of Collections.emptyList(), etc.
        // to avoid UnsupportedOperationException if code under test modifies returned collection
    } else if (type == List.class) {
        return new LinkedList<>();
    } else if (type == Set.class) {
        return new HashSet<>();
    } else if (type == Map.class) {
        return new HashMap<>();
    }
    
    // Return null for other types or handle additional collections as needed
    return null;
}