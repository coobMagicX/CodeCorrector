public class NumberCreator {
    public static void main(String[] args) {
        // This should create a long (if within the range of long)
        Number number = createNumber("12345678901234567890l");
        System.out.println(number.getClass() + ": " + number);

        // This might throw an exception if it's beyond the range of long
        try {
            Number largeNumber = createNumber("1234567890123456789012345678901234567890l");
            System.out.println(largeNumber.getClass() + ": " + largeNumber);
        } catch (NumberFormatException e) {
            System.err.println("Failed to create number: " + e.getMessage());
        }
    }

    // Assuming the 'createNumber' method is defined as shown in your post
}