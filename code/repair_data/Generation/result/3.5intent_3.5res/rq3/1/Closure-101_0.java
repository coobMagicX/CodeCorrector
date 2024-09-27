protected CompilerOptions createOptions() {
  CompilerOptions options = new CompilerOptions();
  options.setCodingConvention(new ClosureCodingConvention());
  CompilationLevel level = CompilationLevel.valueOf(flags.compilation_level);
  level.setOptionsForCompilationLevel(options);
  if (flags.debug) {
    level.setDebugOptionsForCompilationLevel(options);
  }

  WarningLevel wLevel = WarningLevel.valueOf(flags.warning_level);
  wLevel.setOptionsForWarningLevel(options);
  for (FormattingOption formattingOption : flags.formatting) {
    formattingOption.applyToOptions(options);
  }
  if (flags.process_closure_primitives) {
    options.closurePass = true;
  }

  initOptionsFromFlags(options);
  return options;
}