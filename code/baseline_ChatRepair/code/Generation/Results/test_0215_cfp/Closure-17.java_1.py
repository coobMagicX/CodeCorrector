private JSType getDeclaredType(String sourceName, JSDocInfo info,
                               Node lValue, @Nullable Node rValue) {
  if (info != null) {
    if (info.hasType()) {
      return getDeclaredTypeInAnnotation(sourceName, lValue, info);
    } else if (info.isConstant() && rValue != null) {
      JSType type = rValue.getJSType();
      if (type != null && !type.isUnknownType()) {
        return type;
      }
      // If the right side has no type or an unknown type, return the annotated type or unknown.
      return getNativeType(JSTypeNative.UNKNOWN_TYPE);
    } else if (info.hasEnumParameterType()) {
      return handleEnumParameterType(info, lValue, rValue);
    } else if (info.isConstructor() || info.isInterface()) {
      return handleConstructorOrInterfaceType(info, lValue, rValue);
    }
  }

  // Case when info is null or does not have any overriding type information
  if (rValue != null && rValue.isFunction() &&
      shouldUseFunctionLiteralType(JSType.toMaybeFunctionType(rValue.getJSType()), info, lValue)) {
    return rValue.getJSType();
  }

  // If none of the conditions are met, return unknown to maintain function behavior consistency
  return getNativeType(JSTypeNative.UNKNOWN_TYPE);
}

private JSType handleEnumParameterType(JSDocInfo info, Node lValue, @Nullable Node rValue) {
  if (rValue != null && rValue.isObjectLit()) {
    return rValue.getJSType();
  } else {
    return createEnumTypeFromNodes(
        rValue, lValue.getQualifiedName(), info, lValue);
  }
}

private JSType handleConstructorOrInterfaceType(JSDocInfo info, Node lValue, @Nullable Node rValue) {
  return createFunctionTypeFromNodes(
      rValue, lValue.getQualifiedName(), info, lValue);
}
