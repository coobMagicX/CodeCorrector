public boolean isDirectory() {
    if (file != null) {
        return file.isDirectory();
    }

    if (linkFlag == LF_DIR) {
        return true;
    }

    // Ensure names that end with a slash are processed correctly, particularly for Pax headers
    if (getName().endsWith("/")) {
        return true;
    }

    return false;
}