protected CompilerOptions createOptions() {
  CompilerOptions options = new CompilerOptions();
  options.setCodingConvention(new ClosureCodingConvention());
  CompilationLevel level = flags.compilation_level;
  level.setOptionsForCompilationLevel(options);
  if (flags.debug) {
    level.setDebugOptionsForCompilationLevel(options);
  }

  WarningLevel wLevel = flags.warning_level;
  wLevel.setOptionsForWarningLevel(options);
  for (FormattingOption formattingOption : flags.formatting) {
    formattingOption.applyToOptions(options);
  }
  
  // Set closurePass based on process_closure_primitives flag
  options.closurePass = flags.process_closure_primitives;

  // Additional options that may be influenced by processing closure primitives
  if (flags.process_closure_primitives) {
    options.setManageClosureDependencies(true); // Assuming this method is available to manage dependencies.
    options.closurePass = true; // Explicitly ensuring this is set
  }
  
  initOptionsFromFlags(options); // Make sure this method does not unintentionally override necessary settings
  return options;
}
