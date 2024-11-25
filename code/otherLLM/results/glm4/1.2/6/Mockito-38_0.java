public class Tool {
    // Hypothetical method signature for reference.
    public int[] getSuspiciouslyNotMatchingArgsIndexes(List<Object> args, List<Object> expectedArgs) {
        if (args == null || expectedArgs == null) {
            return new int[0]; // Return an empty array if either list is null
        }
        
        List<Integer> suspiciousIndices = new ArrayList<>();
        for (int i = 0; i < args.size(); i++) {
            if (args.get(i) == null && expectedArgs.get(i) != null ||
                args.get(i) != null && !toStringEquals(new Matcher(), args.get(i)) &&
                expectedArgs.get(i) != null && !toStringEquals(new Matcher(), expectedArgs.get(i))) {
                suspiciousIndices.add(i);
            }
        }
        
        return suspiciousIndices.stream().mapToInt(Integer::intValue).toArray();
    }

    private boolean toStringEquals(Matcher m, Object arg) {
        if (arg == null || m == null) {
            return false; // Cannot compare with null
        }
        return StringDescription.toString(m).equals(arg.toString());
    }
    
    // Matcher class and other necessary classes would be defined here.
}

// Matcher class definition (hypothetical)
class Matcher {
    // Matcher implementation details would go here.
}