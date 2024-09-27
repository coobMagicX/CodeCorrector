public void testFunctionArguments16() throws Exception {
    testTypes(
        "/** @param {...number} var_args */" +
        "function g(var_args) {} g(1, 2);",
        "actual parameter 2 of g does not match formal parameter\n" +
        "found   : number\n" +
        "required: (number|undefined)");
}