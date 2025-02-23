public Range getDataRange(ValueAxis axis) {
    Range result = null;
    List mappedDatasets = new ArrayList();
    List includedAnnotations = new ArrayList();
    boolean isDomainAxis = true;

    
    int domainIndex = getDomainAxisIndex(axis);
    if (domainIndex >= 0) {
        isDomainAxis = true;
        List domainDatasets = getDatasetsMappedToDomainAxis(new Integer(domainIndex));
        if (domainDatasets != null) {
            mappedDatasets.addAll(domainDatasets);
        }
        
        if (domainIndex == 0 && this.annotations != null) {
            for (Object obj : this.annotations) {
                if (obj instanceof XYAnnotation) {
                    XYAnnotation annotation = (XYAnnotation) obj;
                    if (annotation instanceof XYAnnotationBoundsInfo) {
                        includedAnnotations.add(annotation);
                    }
                }
            }
        }
    }

    
    int rangeIndex = getRangeAxisIndex(axis);
    if (rangeIndex >= 0) {
        isDomainAxis = false;
        List rangeDatasets = getDatasetsMappedToRangeAxis(new Integer(rangeIndex));
        if (rangeDatasets != null) {
            mappedDatasets.addAll(rangeDatasets);
        }
        
        if (rangeIndex == 0 && this.annotations != null) {
            for (Object obj : this.annotations) {
                if (obj instanceof XYAnnotation) {
                    XYAnnotation annotation = (XYAnnotation) obj;
                    if (annotation instanceof XYAnnotationBoundsInfo) {
                        includedAnnotations.add(annotation);
                    }
                }
            }
        }
    }

    
    for (Object obj : mappedDatasets) {
        if (obj instanceof XYDataset) {
            XYDataset d = (XYDataset) obj;
            XYItemRenderer r = getRendererForDataset(d);
            if (r != null) {
                if (isDomainAxis) {
                    result = Range.combine(result, r.findDomainBounds(d));
                } else {
                    result = Range.combine(result, r.findRangeBounds(d));
                }
                
                if (r.getAnnotations() != null) {
                    for (Object ao : r.getAnnotations()) {
                        if (ao instanceof XYAnnotation) {
                            XYAnnotation a = (XYAnnotation) ao;
                            if (a instanceof XYAnnotationBoundsInfo) {
                                includedAnnotations.add(a);
                            }
                        }
                    }
                }
            } else {
                
                if (isDomainAxis) {
                    result = Range.combine(result, DatasetUtilities.findDomainBounds(d));
                } else {
                    result = Range.combine(result, DatasetUtilities.findRangeBounds(d));
                }
            }
        }
    }

    
    for (Object obj : includedAnnotations) {
        if (obj instanceof XYAnnotationBoundsInfo) {
            XYAnnotationBoundsInfo xyabi = (XYAnnotationBoundsInfo) obj;
            if (xyabi.getIncludeInDataBounds()) {
                if (isDomainAxis) {
                    result = Range.combine(result, xyabi.getXRange());
                } else {
                    result = Range.combine(result, xyabi.getYRange());
                }
            }
        }
    }

    return result;
}
