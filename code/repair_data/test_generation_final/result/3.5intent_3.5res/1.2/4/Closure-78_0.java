// Test case: Division by zero should throw an error
@Test(expected = Error.class)
public void testDivisionByZero() {
    Node left = Node.newNumber(10);
    Node right = Node.newNumber(0);
    performArithmeticOp(Token.DIV, left, right);
}

// Test case: Operand is a string
@Test
public void testOperandIsString() {
    Node left = Node.newString(Token.NAME, "10");
    Node right = Node.newNumber(5);
    Node result = performArithmeticOp(Token.ADD, left, right);
    assertNull(result); // Expect null when one of the operands is a string
}