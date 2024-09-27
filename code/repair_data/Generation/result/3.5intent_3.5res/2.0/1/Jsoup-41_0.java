public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    Element element = (Element) o;

    // Compare the attributes and content of the elements
    return this.attribute1.equals(element.attribute1) &&
            this.attribute2.equals(element.attribute2) &&
            this.content.equals(element.content);
}