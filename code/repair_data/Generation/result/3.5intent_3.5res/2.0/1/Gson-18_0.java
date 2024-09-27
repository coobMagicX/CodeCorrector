static Type getSupertype(Type context, Class<?> contextRawType, Class<?> supertype) {
  checkArgument(supertype.isAssignableFrom(contextRawType));
  return resolve(context, contextRawType,
      $Gson$Types.getGenericSupertype(context, contextRawType, supertype));
}

static Type resolve(Type context, Class<?> contextRawType, Type toResolve) {
  if (toResolve instanceof Class<?>) {
    Class<?> toResolveClass = (Class<?>) toResolve;
    if (toResolveClass.isArray()) {
      Type componentType = toResolveClass.getComponentType();
      Type newComponentType = resolve(context, contextRawType, componentType);
      if (componentType != newComponentType) {
        return $Gson$Types.arrayOf(newComponentType);
      } else {
        return toResolve;
      }
    }
  }
  return $Gson$Types.resolve(context, contextRawType, toResolve);
}

static Type getGenericSupertype(Type context, Class<?> rawType, Class<?> toResolve) {
  if (toResolve == rawType) {
    return context;
  }

  // we skip searching through interfaces if unknown is an interface
  if (toResolve.isInterface()) {
    Class<?>[] interfaces = rawType.getInterfaces();
    for (int i = 0, length = interfaces.length; i < length; i++) {
      if (interfaces[i] == toResolve) {
        return rawType.getGenericInterfaces()[i];
      } else if (toResolve.isAssignableFrom(interfaces[i])) {
        return getGenericSupertype(rawType.getGenericInterfaces()[i], interfaces[i], toResolve);
      }
    }
  }

  // check our supertypes
  if (!rawType.isInterface()) {
    while (rawType != Object.class) {
      Class<?> rawSupertype = rawType.getSuperclass();
      if (rawSupertype == toResolve) {
        return rawType.getGenericSuperclass();
      } else if (toResolve.isAssignableFrom(rawSupertype)) {
        return getGenericSupertype(rawType.getGenericSuperclass(), rawSupertype, toResolve);
      }
      rawType = rawSupertype;
    }
  }

  // we can't resolve this further
  return toResolve;
}