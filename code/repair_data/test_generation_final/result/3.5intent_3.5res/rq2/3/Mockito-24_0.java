@Test
public void testAnswerCompareToDifferentObjects() {
    // Arrange
    Object mock1 = mock(Object.class);
    Object mock2 = mock(Object.class);
    
    // Act
    int result = answer.answer(invocationOnMock(mock1, mock2));
    
    // Assert
    assertNotEquals(0, result);
}