private JSType getDeclaredType(String sourceName, JSDocInfo info,
    Node lValue, @Nullable Node rValue) {
  if (info != null) { 
    if (info.hasType()) {
      // Always prioritize explicitly annotated types.
      return getDeclaredTypeInAnnotation(sourceName, lValue, info);
    } else if (info.hasEnumParameterType()) {
      if (rValue != null && rValue.isObjectLit()) {
        return rValue.getJSType();
      } else {
        return createEnumTypeFromNodes(
            rValue, lValue.getQualifiedName(), info, lValue);
      }
    } else if (info.isConstructor() || info.isInterface()) {
      return createFunctionTypeFromNodes(
          rValue, lValue.getQualifiedName(), info, lValue);
    } else if (info.isConstant()) {
        // Handle constant without explicit type by attempting to derive from context or defaults.
        JSType knownType = null;
        if (rValue != null && rValue.getJSType() != null && !rValue.getJSType().isUnknownType()) {
          return rValue.getJSType();
        }
        // Use the UNKNOWN_TYPE if no type information is resolved.
        return getNativeType(JSTypeNative.UNKNOWN_TYPE);
    }
  }

  if (rValue != null && rValue.isFunction() &&
      shouldUseFunctionLiteralType(
          JSType.toMaybeFunctionType(rValue.getJSType()), info, lValue)) {
    return rValue.getJSType();
  }
  
  return getNativeType(JSTypeNative.UNKNOWN_TYPE);
}
