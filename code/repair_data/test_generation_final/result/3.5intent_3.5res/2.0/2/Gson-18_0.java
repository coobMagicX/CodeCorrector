static Type getGenericSupertype(Type context, Class<?> rawType, Class<?> toResolve) {
  if (toResolve == rawType) {
    return context;
  }

  // we skip searching through interfaces if unknown is an interface
  if (toResolve.isInterface()) {
    // Check if the raw type is the same as the toResolve interface
    if (rawType == toResolve) {
      return context;
    }
    
    Class<?>[] interfaces = rawType.getInterfaces();
    Type[] genericInterfaces = rawType.getGenericInterfaces();
    for (int i = 0, length = interfaces.length; i < length; i++) {
      if (interfaces[i] == toResolve) {
        return genericInterfaces[i];
      } else if (toResolve.isAssignableFrom(interfaces[i])) {
        return getGenericSupertype(genericInterfaces[i], interfaces[i], toResolve);
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