private JSType getDeclaredType(String sourceName, JSDocInfo info, Node lValue, @Nullable Node rValue) {
  if (info != null) {
    if (info.hasType()) {
      // Return the type specified in JSDoc
      return getDeclaredTypeInAnnotation(sourceName, lValue, info);
    } else if (info.isConstructor() || info.isInterface()) {
      // Return type for constructors or interfaces
      return createFunctionTypeFromNodes(rValue, lValue.getQualifiedName(), info, lValue);
    } else if (info.hasEnumParameterType() && rValue != null) {
      // Return type for enums
      return rValue.isObjectLit() ? rValue.getJSType() :
          createEnumTypeFromNodes(rValue, lValue.getQualifiedName(), info, lValue);
    }
  }

  if (rValue != null && rValue.isFunction()) {
    JSType maybeType = rValue.getJSType();
    // Use function literal type only if the inferred type from the function is deemed usable
    if (shouldUseFunctionLiteralType(JSType.toMaybeFunctionType(maybeType), info, lValue)) {
      return maybeType;
    }
  }

  if (rValue != null) {
    // Fallback to using rValue's JSType if available and not unknown,
    // unless rValue is expected to be part of a logical OR operation forming idiomatic pattern
    if (rValue.isOr()) {
      Node secondClause = rValue.getSecondChild();
      JSType secondType = secondClause.getJSType();
      if (secondType != null && !secondType.isUnknownType()) {
        return secondType;
      }
    } else {
      JSType rValueType = rValue.getJSType();
      if (rValueType != null && !rValueType.isUnknownType()) {
        return rValueType;
      }
    }
  }

  // Handle constants explicitly declared without type info
  if (info != null && info.isConstant()) {
    return getNativeType(JSTypeNative.UNKNOWN_TYPE);
  }

  // Final fallback to avoid inconsistent return type.
  return getNativeType(JSTypeNative.UNKNOWN_TYPE);
}
