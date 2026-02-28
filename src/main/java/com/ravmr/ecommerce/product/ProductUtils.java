package com.ravmr.ecommerce.product;

import java.util.stream.Stream;

public abstract class ProductUtils {
    public static boolean isMatchValueType(String str, ProductAttributeValue pav) {
        String valueType = pav.getAttribute().getValueType().toString();
        switch (valueType) {
            case "STRING":
                return pav.getStringValue() != null && pav.getStringValue().equalsIgnoreCase(str);
            case "NUMBER":
                try {
                    double num = Double.parseDouble(str);
                    return pav.getNumericValue() != null && pav.getNumericValue().doubleValue() == num;
                } catch (NumberFormatException e) {
                    return false;
                }
            case "BOOLEAN":
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
