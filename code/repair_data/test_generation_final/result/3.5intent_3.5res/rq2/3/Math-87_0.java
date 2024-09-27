private List<Integer> getBasicRows(final int col) {
    List<Integer> rows = new ArrayList<>();
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            rows.add(i);
        }
    }
    return rows.isEmpty() ? null : rows;
}