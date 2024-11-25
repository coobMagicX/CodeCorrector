public double getPct(Object o) {
    if (!(o instanceof Character)) {
        throw new IllegalArgumentException("Input must be a Character");
    }
    return getCumPct((Comparable<?>) Character.valueOf((char) o));
}