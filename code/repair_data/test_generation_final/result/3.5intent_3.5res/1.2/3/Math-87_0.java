private Integer getBasicRow(final int col) {
    Integer row = null;
    List<Integer> rows = new ArrayList<>();
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            rows.add(i);
            if (rows.size() > 1) {
                return null;
            }
        }
    }
    return rows.isEmpty() ? null : rows.get(0);
}