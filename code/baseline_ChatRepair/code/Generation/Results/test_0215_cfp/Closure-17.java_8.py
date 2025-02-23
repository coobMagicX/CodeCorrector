private JSType getDeclaredType(String sourceName, JSDocInfo info,
                               Node lValue, @Nullable Node rValue) {
  if (info != null && info.hasType()) {
    return getDeclaredTypeInAnnotation(sourceName, lValue, info);
  } else if (rValue != null && rValue.isFunction() &&
      shouldUseFunctionLiteralType(
          JSType.toMaybeFunctionType(rValue.getJSType()), info, lValue)) {
    return rValue.getJSType();
  } else if (info != null) {
    if (info.hasEnumParameterType()) {
      if (rValue != null && rValue.isObjectLit()) {
        return rValue.getJSType();
      } else {
        return createEnumTypeFromNodes(
            rValue, lValue.getQualifiedName(), info, lValue);
      }
    } else if (info.isConstructor() || info.isInterface()) {
      return createFunctionTypeFromNodes(
          rValue, lValue.getQualifiedName(), info, lValue);
    } else {
      if (info.isConstant() && rValue != null && rValue.getJSType() != null && !rValue.getJSType().isUnknownType()) {
        return rValue.getJSType();
      }
      // When no adequate type info is available and a fallback is happening
      reportWarning("No type information available; defaulting to UNKNOWN type.", sourceName);
      return getNativeType(JSTypeNative.UNKNOWN_TYPE);
    }
  }
  // Likely annotation unavailable, handling it gracefully
  reportWarning("No type information available in annotations; inferring UNKNOWN type.", sourceName);
  return getNativeType(JSTypeNative.UNKNOWN_TYPE);
}

private void reportWarning(String message, String sourceName) {
  // Assuming an existing method for logging warnings, replace with your actual method
  System.err.println("Warning in " + sourceName + ": " + message);
  // or integrate with your existing logging/reporting framework
}
