private JSType getDeclaredType(String sourceName, JSDocInfo info,
    Node lValue, @Nullable Node rValue) {
  if (info != null && info.hasType()) {
    return getDeclaredTypeInAnnotation(sourceName, lValue, info);
  } else if (rValue != null && rValue.isFunction() &&
      shouldUseFunctionLiteralType(
          JSType.toMaybeFunctionType(rValue.getJSType()), info, lValue)) {
    // Ensure that the function literal type is a number
    if (rValue.getJSType().isNumber()) {
      return rValue.getJSType();
    }
  } else if (info != null) {
    if (info.hasEnumParameterType()) {
      if (rValue != null && rValue.isObjectLit()) {
        // Ensure that the object literal has a number type
        JSType objType = rValue.getJSType();
        if (objType.isNumber()) {
          return objType;
        }
      } else {
        return createEnumTypeFromNodes(
            rValue, lValue.getQualifiedName(), info, lValue);
      }
    } else if (info.isConstructor() || info.isInterface()) {
      return createFunctionTypeFromNodes(
          rValue, lValue.getQualifiedName(), info, lValue);
    } else {
      // Check if this is constant and has a known type
      if (info.isConstant()) {
        JSType knownType = null;
        if (rValue != null) {
          if (rValue.getJSType() != null && !rValue.getJSType().isUnknownType()) {
            // Check for a number type before returning it
            if (rValue.getJSType().isNumber()) {
              return rValue.getJSType();
            }
          } else if (rValue.isOr()) {
            Node firstClause = rValue.getFirstChild();
            Node secondClause = firstClause.getNext();
            boolean namesMatch = firstClause.isName()
                && lValue.isName()
                && firstClause.getString().equals(lValue.getString());
            if (namesMatch && secondClause.getJSType() != null
                && !secondClause.getJSType().isUnknownType()) {
              // Ensure the type from the or clause is a number before returning it
              if (secondClause.getJSType().isNumber()) {
                return secondClause.getJSType();
              }
            }
          }
        }
      }
    }
  }

  // Fallback to getDeclaredTypeInAnnotation in case of any inconsistency
  return getDeclaredTypeInAnnotation(sourceName, lValue, info);
}