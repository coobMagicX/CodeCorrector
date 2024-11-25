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
  
  // Correctly handle process_closure_primitives flag
  if (!flags.process_closure_primitives) {
    options.closurePass = false; // Ignore or remove primitive closure functions when flag is set to false
  } else {
    options.closurePass = true;
  }

  initOptionsFromFlags(options);
  return options;
}