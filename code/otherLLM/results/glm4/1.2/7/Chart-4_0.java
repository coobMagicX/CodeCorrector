public Range getDataRange(ValueAxis axis) {

    Range result = new Range(Double.MAX_VALUE, Double.MIN_VALUE); // Initialize with extreme values to find bounds
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
            if (isDomainAxis) {
                double[] bounds = r == null ? DatasetUtilities.findDomainBounds(d) : r.findDomainBounds(d);
                result = Range.combine(result, new Range(bounds[0], bounds[1]));
                
                Collection c = r.getAnnotations();
                Iterator i = c.iterator();
                while (i.hasNext()) {
                    XYAnnotation a = (XYAnnotation) i.next();
                    if (a instanceof XYAnnotationBoundsInfo && ((XYAnnotationBoundsInfo)a).getIncludeInDataBounds()) {
                        result = Range.combine(result, new Range(((XYAnnotationBoundsInfo)a).getXRange().getLowerBound(), ((XYAnnotationBoundsInfo)a).getXRange().getUpperBound()));
                    }
                }
            } else {
                double[] bounds = r == null ? DatasetUtilities.findRangeBounds(d) : r.findRangeBounds(d);
                result = Range.combine(result, new Range(bounds[0], bounds[1]));
                
                Collection c = r.getAnnotations();
                Iterator i = c.iterator();
                while (i.hasNext()) {
                    XYAnnotation a = (XYAnnotation) i.next();
                    if (a instanceof XYAnnotationBoundsInfo && ((XYAnnotationBoundsInfo)a).getIncludeInDataBounds()) {
                        result = Range.combine(result, new Range(((XYAnnotationBoundsInfo)a).getYRange().getLowerBound(), ((XYAnnotationBoundsInfo)a).getYRange().getUpperBound()));
                    }
                }
            }
        }
    }

    Iterator it = includedAnnotations.iterator();
    while (it.hasNext()) {
        XYAnnotationBoundsInfo xyabi = (XYAnnotationBoundsInfo) it.next();
        if (xyabi.getIncludeInDataBounds()) {
            Range annotationBounds;
            if (isDomainAxis) {
                annotationBounds = new Range(xyabi.getXRange().getLowerBound(), xyabi.getXRange().getUpperBound());
            } else {
                annotationBounds = new Range(xyabi.getYRange().getLowerBound(), xyabi.getYRange().getUpperBound());
            }
            result = Range.combine(result, annotationBounds);
        }
    }

    // Ensure that the X-axis includes zero in its range bounds calculation
    if (isDomainAxis && result.getLowerBound() > 0) {
        result.setLowerBound(0);
    }

    return result;
}