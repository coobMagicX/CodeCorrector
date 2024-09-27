protected void setName(String name) {
    // Remove any backslashes from the name
    name = name.replace("\\", "/");
    this.name = name;
}