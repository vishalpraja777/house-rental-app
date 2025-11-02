package com.rentalapp.houserentalapp.dao.Specification;

import com.rentalapp.houserentalapp.model.PropertyFilterDTO;
import com.rentalapp.houserentalapp.model.entities.Property;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class PropertySpecification {

    public static Specification<Property> filterProperties(PropertyFilterDTO filter) {
        return (root, query, cb) -> {
            // Start with a conjunction (true)
            var predicates = cb.conjunction();

            // Selected City
            if (filter.getSelectedCity() != null && !filter.getSelectedCity().isEmpty()) {
                predicates.getExpressions().add(
                        cb.equal(cb.lower(root.get("city")), filter.getSelectedCity().toLowerCase())
                );
            }

            // Property Type
            if (filter.getSelectedPropertyTypes() != null && !filter.getSelectedPropertyTypes().isEmpty()) {
                predicates.getExpressions().add(
                        root.get("propertyType").in(filter.getSelectedPropertyTypes())
                );
            }

            // BHK Type
            if (filter.getSelectedBhkTypes() != null && !filter.getSelectedBhkTypes().isEmpty()) {
                predicates.getExpressions().add(
                        root.get("bhkType").in(filter.getSelectedBhkTypes())
                );
            }

            // Furnishing
            if (filter.getSelectedFurnishings() != null && !filter.getSelectedFurnishings().isEmpty()) {
                predicates.getExpressions().add(
                        root.get("furnishing").in(filter.getSelectedFurnishings())
                );
            }

            // Parking
            if (filter.getSelectedParkings() != null && !filter.getSelectedParkings().isEmpty()) {
                predicates.getExpressions().add(
                        root.get("parking").in(filter.getSelectedParkings())
                );
            }

            // Sell Type
            if (filter.getSelectedSellType() != null) {
                predicates.getExpressions().add(
                        cb.equal(root.get("sellType"), filter.getSelectedSellType())
                );
            }

            // Price Range
            predicates.getExpressions().add(
                    cb.between(root.get("price"),
                            BigDecimal.valueOf(filter.getPriceRangeStart()),
                            BigDecimal.valueOf(filter.getPriceRangeEnd()))
            );

            // Size Range
            predicates.getExpressions().add(
                    cb.between(root.get("sizeSqft"),
                            filter.getSizeRangeStart(),
                            filter.getSizeRangeEnd())
            );

            // Boolean amenities filters
            if (Boolean.TRUE.equals(filter.getFilterGatedSociety())) {
                predicates.getExpressions().add(cb.isTrue(root.get("gatedSociety")));
            }
            if (Boolean.TRUE.equals(filter.getFilterGym())) {
                predicates.getExpressions().add(cb.isTrue(root.get("gym")));
            }
            if (Boolean.TRUE.equals(filter.getFilterLift())) {
                predicates.getExpressions().add(cb.isTrue(root.get("lift")));
            }
            if (Boolean.TRUE.equals(filter.getFilterPark())) {
                predicates.getExpressions().add(cb.isTrue(root.get("park")));
            }
            if (Boolean.TRUE.equals(filter.getFilterSwimmingPool())) {
                predicates.getExpressions().add(cb.isTrue(root.get("swimmingPool")));
            }
            if (Boolean.TRUE.equals(filter.getFilterPowerBackup())) {
                predicates.getExpressions().add(cb.isTrue(root.get("powerBackup")));
            }
            if (Boolean.TRUE.equals(filter.getFilterAirConditioner())) {
                predicates.getExpressions().add(cb.isTrue(root.get("airConditioner")));
            }
            if (Boolean.TRUE.equals(filter.getFilterWaterSupply())) {
                predicates.getExpressions().add(cb.isTrue(root.get("waterSupply")));
            }
            if (Boolean.TRUE.equals(filter.getFilterWaterFilter())) {
                predicates.getExpressions().add(cb.isTrue(root.get("waterFilter")));
            }
            if (Boolean.TRUE.equals(filter.getFilterGeyser())) {
                predicates.getExpressions().add(cb.isTrue(root.get("geyser")));
            }

            // Available for filters
            if (Boolean.TRUE.equals(filter.getFilterAvailableForMen())) {
                predicates.getExpressions().add(cb.isTrue(root.get("availableForMen")));
            }
            if (Boolean.TRUE.equals(filter.getFilterAvailableForWomen())) {
                predicates.getExpressions().add(cb.isTrue(root.get("availableForWomen")));
            }
            if (Boolean.TRUE.equals(filter.getFilterAvailableForFamily())) {
                predicates.getExpressions().add(cb.isTrue(root.get("availableForFamily")));
            }

            // Other filters
            if (Boolean.TRUE.equals(filter.getFilterNegotiable())) {
                predicates.getExpressions().add(cb.isTrue(root.get("negotiable")));
            }
            if (Boolean.TRUE.equals(filter.getFilterWaterBillIncluded())) {
                predicates.getExpressions().add(cb.isTrue(root.get("waterBillIncluded")));
            }
            if (Boolean.TRUE.equals(filter.getFilterElectricityBillIncluded())) {
                predicates.getExpressions().add(cb.isTrue(root.get("electricityBillIncluded")));
            }

            return predicates;
        };
    }

}
