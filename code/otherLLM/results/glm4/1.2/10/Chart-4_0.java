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
            if (isDomainAxis) {
                Range domainBounds;
                if (r != null) {
                    domainBounds = r.findDomainBounds(d);
                } else {
                    domainBounds = DatasetUtilities.findDomainBounds(d);
                }
                result = Range.combine(result, domainBounds);
            } else {
                Range rangeBounds;
                if (r != null) {
                    rangeBounds = r.findRangeBounds(d);
                } else {
                    rangeBounds = DatasetUtilities.findRangeBounds(d);
                }
                result = Range.combine(result, rangeBounds);
            }

            // Remove annotations collection iteration code from this loop to avoid possible conflicts
        }
    }

    Iterator it = includedAnnotations.iterator();
    while (it.hasNext()) {
        XYAnnotationBoundsInfo xyabi = (XYAnnotationBoundsInfo) it.next();
        if (xyabi.getIncludeInDataBounds()) {
            Range annotationRange;
            if (isDomainAxis) {
                annotationRange = xyabi.getXRange();
            } else {
                annotationRange = xyabi.getYRange();
            }
            result = Range.combine(result, annotationRange);
        }
    }

    return result;

}