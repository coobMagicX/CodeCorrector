private JSType getDeclaredType(String sourceName, JSDocInfo info, Node lValue, @Nullable Node rValue) {
  if (info != null) {
    if (info.hasType()) {
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
      return getConstantType(rValue, lValue, info);
    }
  }

  if (rValue != null && rValue.isFunction() &&
      shouldUseFunctionLiteralType(
          JSType.toMaybeFunctionType(rValue.getJSType()), info, lValue)) {
    return rValue.getJSType();
  }

  return (info != null) ? getDeclaredTypeInAnnotation(sourceName, lValue, info) : null; // Return null if info is null to avoid misuse.
}

private JSType getConstantType(@Nullable Node rValue, Node lValue, JSDocInfo info) {
  if (rValue != null && rValue.getJSType() != null && !rValue.getJSType().isUnknownType()) {
    return rValue.getJSType(); // Use type from rValue if available and known
  } else if (rValue != null && rValue.isOr()) {
    Node firstClause = rValue.getFirstChild();
    Node secondClause = firstClause.getNext();
    boolean namesMatch = firstClause.isName() && lValue.isName() && firstClause.getString().equals(lValue.getString());
    if (namesMatch && secondClause.getJSType() != null && !secondClause.getJSType().isUnknownType()) {
      return secondClause.getJSType(); // Match specific idiom pattern
    }
  }
  return null; // Return null if no suitable type found
}
