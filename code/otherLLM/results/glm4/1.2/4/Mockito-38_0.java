public int[] getSuspiciouslyNotMatchingArgsIndexes(Object... args) {
    if (args == null) {
        // Return an empty array if the input is null, to handle it gracefully
        return new int[0];
    }

    // Hypothetical logic that may have caused issues with null input
    List<Integer> indexes = new ArrayList<>();
    for (int i = 0; i < args.length; i++) {
        if (args[i] == null) {
            indexes.add(i);
        }
    }

    // Convert the list of indexes to an array and return it
    return indexes.stream().mapToInt(Integer::intValue).toArray();
}