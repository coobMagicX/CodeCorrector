public Element clone() {
    Element clone = (Element) super.clone();
    clone.attributes.putAll(this.attributes.clone());
    return clone;
}