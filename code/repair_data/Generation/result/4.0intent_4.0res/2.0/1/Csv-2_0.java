public String get(final String name) {
    if (mapping == null) {
        throw new IllegalStateException(
                "No header mapping was specified, the record values can't be accessed by name");
    }
    final Integer index = mapping.get(name);
    if (index != null && index >= 0 && index < values.length) {
        return values[index];
    } else {
        return null;
    }
}