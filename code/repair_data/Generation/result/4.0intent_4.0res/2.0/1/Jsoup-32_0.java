public Element clone() {
    Element clone = (Element) super.clone();
    clone.classNames = new HashSet<>(this.classNames); // Deep copy of classNames set
    return clone;
}