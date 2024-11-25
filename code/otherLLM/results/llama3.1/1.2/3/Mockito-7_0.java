private void readTypeVariables() {
    for (Type type : typeVariable.getBounds()) {
        registerTypeVariablesOn(type);
    }
    // Added call to getActualTypeArgumentFor to match the intent of the failed testcase
    Type actualArg = getActualTypeArgumentFor(typeVariable);
    if (actualArg != null) {  // Check for null before calling registerTypeVariablesOn
        registerTypeVariablesOn(actualArg);
    } else {
        registerTypeVariablesOn(getRawTypeFromParameterizedType(parameterizedType));  // Added call to handle parameterized type
    }
}