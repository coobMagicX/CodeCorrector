if (!flags.translationsFile.isEmpty()) {
  try {
    options.messageBundle = new XtbMessageBundle(
        new FileInputStream(flags.translationsFile),
        flags.translationsProject);
  } catch (IOException e) {
    throw new RuntimeException("Reading XTB file", e);
  }
} else if (CompilationLevel.ADVANCED_OPTIMIZATIONS == level) {
  // In SIMPLE or WHITESPACE mode, the user might write their own goog.getMsg that substitutes at run-time.
  // In ADVANCED mode, goog.getMsg is renamed, so inlining it makes sense. 
  // Disable i18n warnings since the user didn't specify a need for i18n handling.
  options.messageBundle = new EmptyMessageBundle();

  // Adjust the options to shut off i18n warnings
  options.setWarningLevel(DiagnosticGroups.MSG_CONVENTIONS, CheckLevel.OFF);
}
