public class PercentageCalculator {

    private double total;

    public void addValue(Object v) {
        if (v instanceof Character) {
            // If the value is a Character, convert it to its numeric equivalent.
            total += Character.getNumericValue((Character) v);
        } else if (v instanceof Number) {
            // If the value is a Number or its subclasses, use the doubleValue method for conversion.
            total += ((Number) v).doubleValue();
        }
    }

    public double getPct(Object v) {
        if (!(v instanceof Number)) {
            throw new IllegalArgumentException("The provided object must be an instance of Number.");
        }
        
        double value = ((Number) v).doubleValue();
        return (value / total) * 100;
    }

    // Other methods and logic
}

// Usage example:
// PercentageCalculator calculator = new PercentageCalculator();
// calculator.addValue(5);
// calculator.addValue('1'); // Assuming '1' is the numeric value of char '1'
// double percentage = calculator.getPct(10); // This should return 20.0%