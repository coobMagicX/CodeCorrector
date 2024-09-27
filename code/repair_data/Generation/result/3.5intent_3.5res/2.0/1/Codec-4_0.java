// Assuming the existing code for the testEncoder method
public void testEncoder() {
    byte[] testData = new byte[] { 1, 2, 3 };
    
    // Encode the test data using the Base64 encoder
    byte[] encodedData = Base64.encodeBase64(testData);
    
    // Decode the encoded data to ensure it matches the original test data
    byte[] decodedData = Base64.decodeBase64(encodedData);
    
    // Assert that the decoded data matches the original test data
    assertTrue(Arrays.equals(testData, decodedData));
}