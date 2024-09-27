private JSType getDeclaredType(String sourceName, JSDocInfo info,
    Node lValue, @Nullable Node rValue) {
  if (info != null && info.hasType()) {
    return getDeclaredTypeInAnnotation(sourceName, lValue, info);
  } else if (rValue != null && rValue.isFunction() &&
      shouldUseFunctionLiteralType(
          JSType.toMaybeFunctionType(rValue.getJSType()), info, lValue)) {
    return rValue.getJSType();
  } else if (info != null) {
    if (info.hasEnumParameterType() || info.isInterface()) {
      if (rValue != null && rValue.isObjectLit()) {
        // Ensure object literals are correctly typed, especially for interfaces and enums
        JSType declaredType = getDeclaredTypeInAnnotation(sourceName, lValue, info);
        // Check if the annotated type matches the type of rValue, if not, use the type from rValue
        return declaredType != null ? declaredType : rValue.getJSType();
      } else if (info.hasEnumParameterType()) {
        return createEnumTypeFromNodes(
            rValue, lValue.getQualifiedName(), info, lValue);
      }
    } else if (info.isConstructor()) {
      return createFunctionTypeFromNodes(
          rValue, lValue.getQualifiedName(), info, lValue);
    } else {
      // Check if this is constant, and if it has a known type.
      if (info.isConstant()) {
        if (rValue != null) {
          if (rValue.getJSType() != null && !rValue.getJSType().isUnknownType()) {
            // Use the type of rValue if it is known and not unknown
            return rValue.getJSType();
          } else if (rValue.isOr()) {
            // Handle specific JS idiom: var x = x || TYPE;
            Node firstClause = rValue.getFirstChild();
            Node secondClause = firstClause.getNext();
            boolean namesMatch = firstClause.isName() && lValue.isName() &&
                firstClause.getString().equals(lValue.getString());
            if (namesMatch && secondClause.getJSType() != null &&
                !secondClause.getJSType().isUnknownType()) {
              return secondClause.getJSType();
            }
          }
        }
      }
    }
  }

  // Fallback to type declared in annotation if all else fails
  return getDeclaredTypeInAnnotation(sourceName, lValue, info);
}