private int[] getSuspiciouslyNotMatchingArgsIndexes(Matcher m, Object... args) {
    if (m == null || args == null) {
        // If either matcher or arguments are null, return an empty array.
        return new int[0];
    }
    
    List<Integer> indexes = new ArrayList<>();
    for (int i = 0; i < args.length; i++) {
        // Use the toStringEquals method safely by checking if 'args[i]' is not null before calling its toString method.
        if (!toStringEquals(m, args[i])) {
            indexes.add(i);
        }
    }
    
    // Convert the list of integers to an array and return it.
    return indexes.stream().mapToInt(Integer::intValue).toArray();
}