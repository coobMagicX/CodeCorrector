import java.util.List;

// Assuming the Element class and Validate utility are defined elsewhere
class Element {
    private Element parent;
    private List<Element> children;
    private String tag;

    public Element(String tag) {
        this.tag = tag;
    }

    public Element parent() {
        return parent;
    }

    public List<Element> children() {
        return children;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return tag.equals(element.tag);
    }

    public Integer elementSiblingIndex() {
       if (parent() == null) return 0;
       return indexInList(this, parent().children()); 
    }

    private static <E extends Element> Integer indexInList(Element search, List<E> elements) {
        Validate.notNull(search);
        Validate.notNull(elements);
        
        for (int i = 0; i < elements.size(); i++) {
            E element = elements.get(i);
            if (element == search) // Changed to check for reference equality
                return i;
        }
        return null;
    }
}