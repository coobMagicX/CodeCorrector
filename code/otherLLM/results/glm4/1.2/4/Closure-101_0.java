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
  
  // Adjust the logic to ensure that `options.closurePass` is set correctly based on the value of `flags.process_closure_primitives`.
  if (!flags.process_closure_primitives) { // Only set closurePass to true if process_closure_primitives is true.
    options.closurePass = false;
  }

  initOptionsFromFlags(options);
  return options;
}