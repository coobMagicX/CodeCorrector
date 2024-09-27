public boolean isDirectory() {
    if (file != null) {
        return file.isDirectory();
    }

    if (linkFlag == LF_DIR || getName().endsWith("/")) {
        return true;
    }

    return false;
}