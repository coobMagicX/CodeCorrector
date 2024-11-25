public JSType caseObjectType(ObjectType type) {
  if (goog.isFunction(type)) { // Use goog.isFunction to check for Function type
    return matchesExpectation("function") ? type : null; // Return 'type' if it's a function, otherwise null
  } else if (matchesExpectation("object")) {
    return type; // Return the 'type' if it's an object and meets expectation
  }
  return null; // Return null for all other cases
}