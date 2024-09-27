public DefaultPrettyPrinter createInstance() {
    if (this.getClass() == DefaultPrettyPrinter.class) {
        return new DefaultPrettyPrinter(this);
    } else if (this.getClass() == MyCustomPrettyPrinter.class) {
        return new MyCustomPrettyPrinter(this);
    } else {
        throw new IllegalStateException("Invalid subclass of DefaultPrettyPrinter is used.");
    }
}