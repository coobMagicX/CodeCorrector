public XYPlot(XYDataset dataset,
              ValueAxis domainAxis,
              ValueAxis rangeAxis,
              XYItemRenderer renderer) {

    super();

    this.orientation = PlotOrientation.VERTICAL;
    this.weight = 1;  // only relevant when this is a subplot
    this.axisOffset = new RectangleInsets(4.0, 4.0, 4.0, 4.0);

    // allocate storage for datasets, axes and renderers (all optional)
    this.domainAxes = new ObjectList();
    this.domainAxisLocations = new ObjectList();
    this.foregroundDomainMarkers = new HashMap();
    this.backgroundDomainMarkers = new HashMap();

    this.rangeAxes = new ObjectList();
    this.rangeAxisLocations = new ObjectList();
    this.foregroundRangeMarkers = new HashMap();
    this.backgroundRangeMarkers = new HashMap();

    this.datasets = new ObjectList();
    this.renderers = new ObjectList();

    this.datasetToDomainAxesMap = new TreeMap();
    this.datasetToRangeAxesMap = new TreeMap();

    this.annotations = new java.util.ArrayList();

    this.datasets.set(0, dataset);
    if (dataset != null) {
        dataset.addChangeListener(this);
    }

    this.renderers.set(0, renderer);
    if (renderer != null) {
        renderer.setPlot(this);
        renderer.addChangeListener(this);
    }

    // Add a new method call here
    this.domainAxis.setRange(domainAxis.getMaximum());
    this.rangeAxis.setDomain(rangeAxis.getMinimum());

    this.domainAxes.set(0, domainAxis);
    this.mapDatasetToDomainAxis(0, 0);
    if (domainAxis != null) {
        domainAxis.setPlot(this);
        domainAxis.addChangeListener(this);
    }
    this.domainAxisLocations.set(0, AxisLocation.BOTTOM_OR_LEFT);

    this.rangeAxes.set(0, rangeAxis);
    this.mapDatasetToRangeAxis(0, 0);
    if (rangeAxis != null) {
        rangeAxis.setPlot(this);
        rangeAxis.addChangeListener(this);
    }
    this.rangeAxisLocations.set(0, AxisLocation.TOP_OR_LEFT);

    configureDomainAxes();
    configureRangeAxes();

    this.domainGridlinesVisible = true;
    this.domainGridlineStroke = DEFAULT_GRIDLINE_STROKE;
    this.domainGridlinePaint = DEFAULT_GRIDLINE_PAINT;

    this.domainMinorGridlinesVisible = false;
    this.domainMinorGridlineStroke = DEFAULT_GRIDLINE_STROKE;
    this.domainMinorGridlinePaint = Color.white;

    this.domainZeroBaselineVisible = false;
    this.domainZeroBaselinePaint = Color.black;
    this.domainZeroBaselineStroke = new BasicStroke(0.5f);

    this.rangeGridlinesVisible = true;
    this.rangeGridlineStroke = DEFAULT_GRIDLINE_STROKE;
    this.rangeGridlinePaint = DEFAULT_GRIDLINE_PAINT;

    this.rangeMinorGridlinesVisible = false;
    this.rangeMinorGridlineStroke = DEFAULT_GRIDLINE_STROKE;
    this.rangeMinorGridlinePaint = Color.white;

    this.rangeZeroBaselineVisible = false;
    this.rangeZeroBaselinePaint = Color.black;
    this.rangeZeroBaselineStroke = new BasicStroke(0.5f);

    this.domainCrosshairVisible = false;
    this.domainCrosshairValue = 0.0;
    this.domainCrosshairStroke = DEFAULT_CROSSHAIR_STROKE;
    this.domainCrosshairPaint = DEFAULT_CROSSHAIR_PAINT;

    this.rangeCrosshairVisible = false;
    this.rangeCrosshairValue = 0.0;
    this.rangeCrosshairStroke = DEFAULT_CROSSHAIR_STROKE;
    this.rangeCrosshairPaint = DEFAULT_CROSSHAIR_PAINT;

}