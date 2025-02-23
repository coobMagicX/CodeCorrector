private JSType getDeclaredType(String sourceName, JSDocInfo info,
                               Node lValue, @Nullable Node rValue) {
  JSType declaredType = null;
  
  if (info != null && info.hasType()) {
    declaredType = getDeclaredTypeInAnnotation(sourceName, lValue, info);
  } else if (rValue != null && rValue.isFunction() &&
             shouldUseFunctionLiteralType(
                 JSType.toMaybeFunctionType(rValue.getJSType()), info, lValue)) {
    declaredType = rValue.getJSType();
  } else if (info != null) {
    if (info.hasEnumParameterType()) {
      if (rValue != null && rValue.isObjectLit()) {
        declaredType = rValue.getJSType();
      } else {
        declaredType = createEnumTypeFromNodes(
            rValue, lValue.getQualifiedName(), info, lValue);
      }
    } else if (info.isConstructor() || info.isInterface()) {
      declaredType = createFunctionTypeFromNodes(
          rValue, lValue.getQualifiedName(), info, lValue);
    } else {
      if (info.isConstant()) {
        if (rValue != null) {
          declaredType = rValue.getJSType();
          if (declaredType == null || declaredType.isUnknownType()) {
            reporter.warning("Expected type information but found unknown type.", sourceName, null);
            declaredType = getNativeType(JSTypeNative.UNKNOWN_TYPE);
          }
        }
      }
    }
  } else {
    reporter.warning("Type could not be determined. Defaulting to UNKNOWN_TYPE.", sourceName, null);
    declaredType = getNativeType(JSTypeNative.UNKNOWN_TYPE);
  }
  
  return declaredType;
}
