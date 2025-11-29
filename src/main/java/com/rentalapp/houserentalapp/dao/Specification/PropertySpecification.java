package com.rentalapp.houserentalapp.dao.Specification;

import com.rentalapp.houserentalapp.model.PropertyFilterDTO;
import com.rentalapp.houserentalapp.model.entities.Property;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PropertySpecification {

    public static Specification<Property> filterProperties(PropertyFilterDTO filter) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Selected City
            if (filter.getSelectedCity() != null && !filter.getSelectedCity().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("city")),
                        filter.getSelectedCity().toLowerCase()));
            }

            // Match in ANY of city, pincode or area
//            Predicate locationOrPredicate = null;

            List<Predicate> locationPredicates = new ArrayList<>();

//            if (filter.getSelectedCity() != null && !filter.getSelectedCity().isEmpty()) {
//                locationPredicates.add(cb.equal(cb.lower(root.get("city")), filter.getSelectedCity().toLowerCase()));
//            }

            // Pincode List
            if (filter.getSelectedPincodes() != null && !filter.getSelectedPincodes().isEmpty()) {
                // Convert all to lowercase for case-insensitive matching
                List<String> lowerPincodes = filter.getSelectedPincodes()
                        .stream()
                        .map(String::toLowerCase)
                        .toList();

                locationPredicates.add(
                        cb.lower(root.get("pincode")).in(lowerPincodes)
                );
            }

            // Area List
            if (filter.getSelectedAreas() != null && !filter.getSelectedAreas().isEmpty()) {

                List<String> lowerAreas = filter.getSelectedAreas()
                        .stream()
                        .map(String::toLowerCase)
                        .toList();

                locationPredicates.add(
                        cb.lower(root.get("area")).in(lowerAreas)
                );
            }

            if (!locationPredicates.isEmpty()) {
                predicates.add(cb.or(locationPredicates.toArray(new Predicate[0])));
            }

            // Property Type
            if (filter.getSelectedPropertyTypes() != null && !filter.getSelectedPropertyTypes().isEmpty()) {
                predicates.add(root.get("propertyType").in(filter.getSelectedPropertyTypes()));
            }

            // BHK Type
            if (filter.getSelectedBhkTypes() != null && !filter.getSelectedBhkTypes().isEmpty()) {
                predicates.add(root.get("bhkType").in(filter.getSelectedBhkTypes()));
            }

            // Furnishing
            if (filter.getSelectedFurnishings() != null && !filter.getSelectedFurnishings().isEmpty()) {
                predicates.add(root.get("furnishing").in(filter.getSelectedFurnishings()));
            }

            // Parking
            if (filter.getSelectedParkings() != null && !filter.getSelectedParkings().isEmpty()) {
                predicates.add(root.get("parking").in(filter.getSelectedParkings()));
            }

            // Sell Type
            if (filter.getSelectedSellType() != null) {
                predicates.add(cb.equal(root.get("sellType"), filter.getSelectedSellType()));
            }

            // Price Range
            if (filter.getPriceRange().getStart() != 0 && filter.getPriceRange().getEnd() != 0) {
                predicates.add(cb.between(
                        root.get("price"),
                        BigDecimal.valueOf(filter.getPriceRange().getStart()),
                        BigDecimal.valueOf(filter.getPriceRange().getEnd())
                ));
            }

            // Size Range
            if (filter.getSizeRange().getStart() != 0 && filter.getSizeRange().getEnd() != 0) {
                predicates.add(cb.between(
                        root.get("sizeSqft"),
                        filter.getSizeRange().getStart(),
                        filter.getSizeRange().getEnd()
                ));
            }

            // Amenities boolean filters
            if (Boolean.TRUE.equals(filter.getFilterGatedSociety())) predicates.add(cb.isTrue(root.get("gatedSociety")));
            if (Boolean.TRUE.equals(filter.getFilterGym())) predicates.add(cb.isTrue(root.get("gym")));
            if (Boolean.TRUE.equals(filter.getFilterLift())) predicates.add(cb.isTrue(root.get("lift")));
            if (Boolean.TRUE.equals(filter.getFilterPark())) predicates.add(cb.isTrue(root.get("park")));
            if (Boolean.TRUE.equals(filter.getFilterSwimmingPool())) predicates.add(cb.isTrue(root.get("swimmingPool")));
            if (Boolean.TRUE.equals(filter.getFilterPowerBackup())) predicates.add(cb.isTrue(root.get("powerBackup")));
            if (Boolean.TRUE.equals(filter.getFilterAirConditioner())) predicates.add(cb.isTrue(root.get("airConditioner")));
            if (Boolean.TRUE.equals(filter.getFilterWaterSupply())) predicates.add(cb.isTrue(root.get("waterSupply")));
            if (Boolean.TRUE.equals(filter.getFilterWaterFilter())) predicates.add(cb.isTrue(root.get("waterFilter")));
            if (Boolean.TRUE.equals(filter.getFilterGeyser())) predicates.add(cb.isTrue(root.get("geyser")));

            // Availability
            if (Boolean.TRUE.equals(filter.getFilterAvailableForMen())) predicates.add(cb.isTrue(root.get("availableForMen")));
            if (Boolean.TRUE.equals(filter.getFilterAvailableForWomen())) predicates.add(cb.isTrue(root.get("availableForWomen")));
            if (Boolean.TRUE.equals(filter.getFilterAvailableForFamily())) predicates.add(cb.isTrue(root.get("availableForFamily")));

            // Other filters
            if (Boolean.TRUE.equals(filter.getFilterNegotiable())) predicates.add(cb.isTrue(root.get("negotiable")));
            if (Boolean.TRUE.equals(filter.getFilterWaterBillIncluded())) predicates.add(cb.isTrue(root.get("waterBillIncluded")));
            if (Boolean.TRUE.equals(filter.getFilterElectricityBillIncluded())) predicates.add(cb.isTrue(root.get("electricityBillIncluded")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
