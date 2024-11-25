public Range getDataRange(ValueAxis axis) {
    Range result = null;
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
                bounds = (r == null) ? DatasetUtilities.findDomainBounds(d) : r.findDomainBounds(d);
                if (!axis.isIncludeZero()) { // Check for includeZero
                    bounds = new Range(bounds.getLowerBound(), Math.max(0, bounds.getUpperBound()));
                }
            } else {
                bounds = (r == null) ? DatasetUtilities.findRangeBounds(d) : r.findRangeBounds(d);
                if (!axis.isIncludeZero()) { // Check for includeZero
                    bounds = new Range(Math.max(0, bounds.getLowerBound()), bounds.getUpperBound());
                }
            }
            
            result = Range.combine(result, bounds);

            Collection c = (r == null) ? DatasetUtilities.getAnnotations(d) : r.getAnnotations();
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
        if (xyabi.getIncludeInDataBounds()) {
            result = Range.combine(result, annotationRange);
        }
    }

    return result;
}