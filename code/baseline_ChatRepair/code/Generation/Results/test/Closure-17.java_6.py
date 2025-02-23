private JSType getDeclaredType(String sourceName, JSDocInfo info,
                               Node lValue, @Nullable Node rValue) {
  JSType type = null;
  
  if (info != null && info.hasType()) {
    type = getDeclaredTypeInAnnotation(sourceName, lValue, info);
  } else if (rValue != null && rValue.isFunction() &&
             shouldUseFunctionLiteralType(
                 JSType.toMaybeFunctionType(rValue.getJSType()), info, lValue)) {
    type = rValue.getJSType();
  } else if (info != null) {
    if (info.hasEnumParameterType()) {
      if (rValue != null && rValue.isObjectLit()) {
        type = rValue.getJSType();
      } else {
        type = createEnumTypeFromNodes(
          rValue, lValue.getQualifiedName(), info, lValue);
      }
    } else if (info.isConstructor() || info.isInterface()) {
      type = createFunctionTypeFromNodes(
          rValue, lValue.getQualifiedName(), info, lValue);
    } else {
      if (info.isConstant()) {
        if (rValue != null && rValue.hasChildren()) {
          Node firstChild = rValue.getFirstChild();
          if ((!firstChild.getJSType().isUnknownType())) {
            type = firstChild.getJSType();
          }
        }
      }
    }
  }
  
  if (type == null || type.isUnknownType()) { // Ensure fallback to a reasonable type instead of relying entirely on annotation.
    type = getNativeType(JSTypeNative.UNKNOWN_TYPE);
  }
  
  return type;
}
