/**
 * Method to report an error.
 *
 * @param message the error message
 * @param args    the arguments to format the error message
 */
private void _reportError(String message, Object... args) {
    throw new IOException(String.format(message, args));
}