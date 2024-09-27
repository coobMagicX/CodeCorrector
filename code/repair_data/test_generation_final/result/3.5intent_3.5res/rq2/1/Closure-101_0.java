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
  }

  initOptionsFromFlags(options);
  return options;
}

private void initOptionsFromFlags(CompilerOptions options) {
  options.setPrintTree(flags.print_tree);
  options.setComputePhaseOrdering(flags.compute_phase_ordering);
  options.setPrintAst(flags.print_ast);
  options.setPrintPassGraph(flags.print_pass_graph);
  options.setJscompDevMode(flags.jscomp_dev_mode);
  options.setLoggingLevel(flags.logging_level);
  options.setExterns(flags.externs);
  options.setJs(flags.js);
  options.setJsOutputFile(flags.js_output_file);
  options.setModule(flags.module);
  options.setVariableMapInputFile(flags.variable_map_input_file);
  options.setPropertyMapInputFile(flags.property_map_input_file);
  options.setVariableMapOutputFile(flags.variable_map_output_file);
  options.setCreateNameMapFiles(flags.create_name_map_files);
  options.setPropertyMapOutputFile(flags.property_map_output_file);
  options.setThirdParty(flags.third_party);
  options.setSummaryDetailLevel(flags.summary_detail_level);
  options.setOutputWrapper(flags.output_wrapper);
  options.setOutputWrapperMarker(flags.output_wrapper_marker);
  options.setModuleWrapper(flags.module_wrapper);
  options.setModuleOutputPathPrefix(flags.module_output_path_prefix);
  options.setCreateSourceMap(flags.create_source_map);
  options.setJscompError(flags.jscomp_error);
  options.setJscompWarning(flags.jscomp_warning);
  options.setJscompOff(flags.jscomp_off);
  options.setDefine(flags.define);
  options.setCharset(flags.charset);
}