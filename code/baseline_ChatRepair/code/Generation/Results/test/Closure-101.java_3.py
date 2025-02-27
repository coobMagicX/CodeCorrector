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

  if (flags.process_closure_primitives) {
    options.closurePass = true;
    options.setManageClosureDependencies(true);  // Ensure closure dependencies are managed correctly
    options.setCheckClosureImports(true);        // Add this if closure imports have to be checked
  }

  initOptionsFromFlags(options);
  return options;
}
