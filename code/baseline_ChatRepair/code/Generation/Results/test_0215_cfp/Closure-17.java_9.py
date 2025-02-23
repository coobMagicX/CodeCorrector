private JSType getDeclaredType(String sourceName, JSDocInfo info, Node lValue, @Nullable Node rValue) {
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
      // Check if this is constant, and if it has a known type.
      if (info.isConstant() && rValue != null) {
        JSType rValueType = rValue.getJSType();
        if (rValueType != null && !rValueType.isUnknownType()) {
          declaredType = rValueType;
        } else if (rValue.isOr()) {
          Node firstClause = rValue.getFirstChild();
          Node secondClause = firstClause.getNext();
          boolean namesMatch = firstClause.isName()
              && lValue.isName()
              && firstClause.getString().equals(lValue.getString());
          if (namesMatch) {
            declaredType = (secondClause.getJSType() != null && !secondClause.getJSType().isUnknownType()) ? secondClause.getJSType() : rValueType;
          }
        }
      }
    }
  }

  if (declaredType == null) {
    declaredType = getDeclaredTypeInAnnotation(sourceName, lValue, info);
  }

  return declaredType;
}
