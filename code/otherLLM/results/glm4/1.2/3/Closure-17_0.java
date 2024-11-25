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
      identifyNameNode(lValue, rValue, info);
      if (rValue != null && rValue.isObjectLit()) {
        // If it's an object literal, we need to handle the case where the enum
        // is being treated as a constant value which could lead to incorrect type inference.
        JSType knownType = determineEnumConstantType(rValue);
        if (knownType != null) {
          return knownType;
        }
      } else {
        return createEnumTypeFromNodes(
            rValue, lValue.getQualifiedName(), info, lValue);
      }
    } else if (info.isConstructor() || info.isInterface()) {
      return createFunctionTypeFromNodes(
          rValue, lValue.getQualifiedName(), info, lValue);
    } else {
      // Check if this is constant and if it has a known type.
      if (info.isConstant()) {
        JSType knownType = null;
        if (rValue != null) {
          if (rValue.getJSType() != null && !rValue.getJSType().isUnknownType()) {
            // If rValue has a type-cast, we use the type in the type-cast.
            return rValue.getJSType();
          } else if (rValue.isOr()) {
            // Check for a very specific JS idiom:
            // var x = x || TYPE;
            Node firstClause = rValue.getFirstChild();
            Node secondClause = firstClause.getNext();
            boolean namesMatch = firstClause.isName()
                && lValue.isName()
                && firstClause.getString().equals(lValue.getString());
            if (namesMatch && secondClause.getJSType() != null
                && !secondClause.getJSType().isUnknownType()) {
              return secondClause.getJSType();
            }
          }
        }
      }
    }
  }

  return getDeclaredTypeInAnnotation(sourceName, lValue, info);
}

private JSType determineEnumConstantType(Node rValue) {
  if (rValue.isString() || rValue.isNumber()) {
    // Assuming that the string or number is a valid enum constant
    JSType knownType = new JSType();
    knownType.setPrimitiveType(PrimitiveType.ENUM);
    return knownType;
  }
  return null; // If not a valid constant, return null.
}