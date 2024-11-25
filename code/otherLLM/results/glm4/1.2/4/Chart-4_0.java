public Range getDataRange(ValueAxis axis) {

    Range result = new Range(Double.MAX_VALUE, -Double.MAX_VALUE); // Initialize with extreme values
    List mappedDatasets = new ArrayList();
    List includedAnnotations = new ArrayList();
    boolean isDomainAxis = true;

    // is it a domain axis?
    int domainIndex = getDomainAxisIndex(axis);
    if (domainIndex >= 0) {
        isDomainAxis = true;
        mappedDatasets.addAll(getDatasetsMappedToDomainAxis(new Integer(domainIndex)));
        if (domainIndex == 0) {
            // grab the plot's annotations
            Iterator iterator = this.annotations.iterator();
            while (iterator.hasNext()) {
                XYAnnotation annotation = (XYAnnotation) iterator.next();
                if (annotation instanceof XYAnnotationBoundsInfo) {
                    includedAnnotations.add(annotation);
                }
            }
        }
    }

    // or is it a range axis?
    int rangeIndex = getRangeAxisIndex(axis);
    if (rangeIndex >= 0) {
        isDomainAxis = false;
        mappedDatasets.addAll(getDatasetsMappedToRangeAxis(new Integer(rangeIndex)));
        if (rangeIndex == 0) {
            Iterator iterator = this.annotations.iterator();
            while (iterator.hasNext()) {
                XYAnnotation annotation = (XYAnnotation) iterator.next();
                if (annotation instanceof XYAnnotationBoundsInfo) {
                    includedAnnotations.add(annotation);
                }
            }
        }
    }

    // iterate through the datasets that map to the axis and get the union
    // of the ranges.
    Iterator iterator = mappedDatasets.iterator();
    while (iterator.hasNext()) {
        XYDataset d = (XYDataset) iterator.next();
        if (d != null) {
            XYItemRenderer r = getRendererForDataset(d);
            Range bounds;
            if (isDomainAxis) {
                if (r != null) {
                    bounds = r.findDomainBounds(d);
                } else {
                    bounds = DatasetUtilities.findDomainBounds(d);
                }
            } else {
                if (r != null) {
                    bounds = r.findRangeBounds(d);
                } else {
                    bounds = DatasetUtilities.findRangeBounds(d);
                }
            }

            result = Range.combine(result, bounds);

            Collection c = r.getAnnotations();
            Iterator i = c.iterator();
            while (i.hasNext()) {
                XYAnnotation a = (XYAnnotation) i.next();
                if (a instanceof XYAnnotationBoundsInfo) {
                    includedAnnotations.add(a);
                }
            }
        }
    }

    Iterator it = includedAnnotations.iterator();
    while (it.hasNext()) {
        XYAnnotationBoundsInfo xyabi = (XYAnnotationBoundsInfo) it.next();
        Range annotationRange;
        if (isDomainAxis) {
            annotationRange = xyabi.getXRange();
        } else {
            annotationRange = xyabi.getYRange();
        }

        result = Range.combine(result, annotationRange);
    }

    // Adjust the range to include zero if auto-range is enabled
    boolean isAutoRangeIncludesZero = this.isAutoRangeIncludesZero; // Assuming a method that checks for auto-range includes zero
    if (isAutoRangeIncludesZero && (result.getMin() > 0 || result.getMax() < 0)) {
        double newMin = Math.min(0, result.getMin());
        double newMax = Math.max(0, result.getMax());
        result = new Range(newMin, newMax);
    }

    return result;

}