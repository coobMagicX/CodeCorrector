import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Collection;

class TypeResolver {
    static Type getSupertype(Type context, Class<?> contextRawType, Class<?> supertype) {
        checkArgument(supertype.isAssignableFrom(contextRawType));
        HashSet<TypeVariable> visitedTypeVariables = new HashSet<>();
        return resolve(context, contextRawType,
            $Gson$Types.getGenericSupertype(context, contextRawType, supertype), visitedTypeVariables);
    }

    private static Type resolve(Type context, Class<?> contextRawType, Type toResolve, Collection<TypeVariable> visitedTypeVariables) {
        while (true) {
            if (toResolve instanceof TypeVariable) {
                TypeVariable<?> typeVariable = (TypeVariable<?>) toResolve;
                if (visitedTypeVariables.contains(typeVariable)) {
                    return toResolve;
                } else {
                    visitedTypeVariables.add(typeVariable);
                }
                toResolve = resolveTypeVariable(context, contextRawType, typeVariable);
                if (toResolve == typeVariable) {
                    return toResolve;
                }
            } else if (toResolve instanceof Class && ((Class<?>) toResolve).isArray()) {
                Class<?> original = (Class<?>) toResolve;
                Type componentType = original.getComponentType();
                Type newComponentType = resolve(context, contextRawType, componentType, visitedTypeVariables);
                return componentType == newComponentType
                    ? original
                    : arrayOf(newComponentType);
            } else if (toResolve instanceof GenericArrayType) {
                GenericArrayType original = (GenericArrayType) toResolve;
                Type componentType = original.getGenericComponentType();
                Type newComponentType = resolve(context, contextRawType, componentType, visitedTypeVariables);
                return componentType == newComponentType
                    ? original
                    : arrayOf(newComponentType);
            } else if (toResolve instanceof ParameterizedType) {
                ParameterizedType original = (ParameterizedType) toResolve;
                Type ownerType = original.getOwnerType();
                Type newOwnerType = resolve(context, contextRawType, ownerType, visitedTypeVariables);
                boolean changed = newOwnerType != ownerType;
                Type[] args = original.getActualTypeArguments();
                for (int t = 0, length = args.length; t < length; t++) {
                    Type resolvedTypeArgument = resolve(context, contextRawType, args[t], visitedTypeVariables);
                    if (resolvedTypeArgument != args[t]) {
                        if (!changed) {
                            args = args.clone();
                            changed = true;
                        }
                        args[t] = resolvedTypeArgument;
                    }
                }
                return changed
                    ? newParameterizedTypeWithOwner(newOwnerType, original.getRawType(), args)
                    : original;
            } else if (toResolve instanceof WildcardType) {
                WildcardType original = (WildcardType) toResolve;
                Type[] originalLowerBound = original.getLowerBounds();
                Type[] originalUpperBound = original.getUpperBounds();
                if (originalLowerBound.length == 1) {
                    Type lowerBound = resolve(context, contextRawType, originalLowerBound[0], visitedTypeVariables);
                    if (lowerBound != originalLowerBound[0]) {
                        return supertypeOf(lowerBound);
                    }
                } else if (originalUpperBound.length == 1) {
                    Type upperBound = resolve(context, contextRawType, originalUpperBound[0], visitedTypeVariables);
                    if (upperBound != originalUpperBound[0]) {
                        return subtypeOf(upperBound);
                    }
                }
                return original;
            } else {
                return toResolve;
            }
        }
    }

    private static void checkArgument(boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException("Supertype is not assignable from the context raw type.");
        }
    }
}