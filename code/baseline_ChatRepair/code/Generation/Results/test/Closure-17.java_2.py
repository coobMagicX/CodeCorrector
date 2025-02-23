private JSType getDeclaredType(String sourceName, JSDocInfo info,
                               Node lValue, @Nullable Node rValue) {
  // Prioritize declared types in JSDoc annotations
  if (info != null && info.hasType()) {
    return getDeclaredTypeInAnnotation(sourceName, lValue, info);
  }

  if (rValue != null && rValue.isFunction() &&
      shouldUseFunctionLiteralType(JSType.toMaybeFunctionType(rValue.getJSType()), info, lValue)) {
    return rValue.getJSType();
  }

  if (info != null) {
    // Check for the presence of Enumeration or Constant type annotations
    if (info.hasEnumParameterType() && rValue != null && rValue.isObjectLit()) {
      return rValue.getJSType();
    } else if (info.isConstructor() || info.isInterface()) {
      return createFunctionTypeFromNodes(
          rValue, lValue.getQualifiedName(), info, lValue);
    } else if (info.isConstant()) {
      // For constants, if an explicit type is given, prioritize that
      return getDeclaredTypeInAnnotation(sourceName, lValue, info);
    }

    // Handle enum type cases without object literals
    if (info.hasEnumParameterType()) {
      return createEnumTypeFromNodes(
          rValue, lValue.getQualifiedName(), info, lValue);
    }
  }

  // If all special conditions fail, use the type of the right-hand value if available and not unknown
  if (rValue != null && rValue.getJSType() != null && !rValue.getJSType().isUnknownType()) {
    return rValue.getJSType();
  }

  // Default to unknown type when nothing specific is determined
  return getNativeType(JSTypeNative.UNKNOWN_TYPE);
}
