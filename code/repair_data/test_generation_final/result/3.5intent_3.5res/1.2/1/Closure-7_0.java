public JSType caseObjectType(ObjectType type) {
  JSType typeOfFunction = getNativeTypeForTypeOf("function");
  if (typeOfFunction != null && typeOfFunction.isSubtype(type)) {
    return typeOfFunction;
  }
  return matchesExpectation("object") || matchesExpectation("function") ? type : null;
}