package com.rentalapp.houserentalapp.model;

import com.rentalapp.houserentalapp.model.entities.Property.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PropertyFilterDTO {

    private String selectedCity;
    private List<PropertyType> selectedPropertyTypes;
    private List<BhkType> selectedBhkTypes;
    private List<Furnishing> selectedFurnishings;
    private List<Parking> selectedParkings;

    private double priceRangeStart;
    private double priceRangeEnd;
    private double sizeRangeStart;
    private double sizeRangeEnd;

    private SellType selectedSellType;
    private boolean showFilters;

    // Boolean amenities filters
    private Boolean filterGatedSociety;
    private Boolean filterGym;
    private Boolean filterLift;
    private Boolean filterPark;
    private Boolean filterSwimmingPool;
    private Boolean filterPowerBackup;
    private Boolean filterAirConditioner;
    private Boolean filterWaterSupply;
    private Boolean filterWaterFilter;
    private Boolean filterGeyser;

    // Available for filters
    private Boolean filterAvailableForMen;
    private Boolean filterAvailableForWomen;
    private Boolean filterAvailableForFamily;

    // Other filters
    private Boolean filterNegotiable;
    private Boolean filterWaterBillIncluded;
    private Boolean filterElectricityBillIncluded;

    public PropertyFilterDTO() {
        this.selectedCity = "";
        this.selectedSellType = SellType.RENT;
        this.priceRangeStart = 0;
        this.priceRangeEnd = 100000;
        this.sizeRangeStart = 0;
        this.sizeRangeEnd = 50000;
        this.showFilters = false;
    }

}
