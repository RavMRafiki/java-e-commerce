package com.ravmr.ecommerce.product;

public abstract class ProductUtils {
    public static boolean isMatchValueType(String str, ProductAttributeValue pav) {
        if (pav == null || pav.getAttribute() == null || pav.getAttribute().getValueType() == null || str == null) {
            return false;
        }

        switch (pav.getAttribute().getValueType()) {
            case STRING:
                return pav.getStringValue() != null && pav.getStringValue().equalsIgnoreCase(str);
            case NUMBER:
                try {
                    double num = Double.parseDouble(str);
                    return pav.getNumericValue() != null && pav.getNumericValue().doubleValue() == num;
                } catch (NumberFormatException e) {
                    return false;
                }
            case BOOLEAN:
                boolean bool = Boolean.parseBoolean(str);
                return pav.getBooleanValue() != null && pav.getBooleanValue().booleanValue() == bool;
            default:
                return false;
        }
    }

    public static boolean isMatchValueType(String[] filterValues, ProductAttributeValue pav) {
        for (String filterValue : filterValues) {
            if (isMatchValueType(filterValue, pav)) {
                return true;
            }
        }
        return false;
    }
}
