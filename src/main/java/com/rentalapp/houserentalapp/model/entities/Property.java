package com.rentalapp.houserentalapp.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/* This class has the details of the Property */

@Entity
@Table(name = "property")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyId;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Users owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING_APPROVAL;

    @Column(nullable = true)
    private Double latitude;
    @Column(nullable = true)
    private Double longitude;
    @Column(length = 255)
    private String googleMapsUrl;

    // Property Details
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyType propertyType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BhkType bhkType;
    @Column(nullable = false)
    private Integer sizeSqft;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Facing facing;
    @Column(nullable = false)
    private Integer propertyAge;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FloorType floorType;
    @Column(nullable = false)
    private Integer floorAt;
    @Column(nullable = false)
    private Integer totalFloors;

    // Address Details
    @Column(nullable = false, length = 50)
    private String state;
    @Column(nullable = false, length = 50)
    private String city;
    @Column(nullable = false, length = 50)
    private String pincode;
    @Column(nullable = false, length = 50)
    private String area;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String streetAddress;

    // Price Details
    @Column(nullable = false)
    private SellType sellType;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    @Column(nullable = false)
    private Boolean negotiable;
    @Column(nullable = false)
    private Boolean waterBillIncluded;
    @Column(nullable = false)
    private Boolean electricityBillIncluded;
    @Column(nullable = false)
    private LocalDateTime availableFrom;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Furnishing furnishing;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Kitchen kitchen;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String propertyDescription;

    // Amenities Details
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private List<AvailableFor> availableFor;
    @Column(nullable = false)
    private Boolean availableForMen;
    @Column(nullable = false)
    private Boolean availableForWomen;
    @Column(nullable = false)
    private Boolean availableForFamily;

    @Column(nullable = false)
    private Integer noOfBathrooms;  
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Parking parking;
    // Other Amenities
    @Column(nullable = false)
    private Boolean gatedSociety;
//    @Column(nullable = false)
//    private Boolean nonVegAllowed;
    @Column(nullable = false)
    private Boolean gym;
    @Column(nullable = false)
    private Boolean lift;
    @Column(nullable = false)
    private Boolean park;
    @Column(nullable = false)
    private Boolean swimmingPool;
    @Column(nullable = false)
    private Boolean powerBackup;
//    @Column(nullable = false)
//    private Boolean gasPipeline;
    @Column(nullable = false)
    private Boolean airConditioner;
    @Column(nullable = false)
    private Boolean waterSupply;
    @Column(nullable = false)
    private Boolean waterFilter;
    @Column(nullable = false)
    private Boolean geyser;

    @JsonIgnore
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @JsonIgnore
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @JsonIgnore
    public String getPropertyTitle() {
        return this.bhkType.name() + " near "+ this.area;
    }

    public enum Status {
        ACTIVE, RENTED, PENDING_APPROVAL, INACTIVE, REJECTED
    }

    public enum PropertyType {
        APARTMENT, INDEPENDENT_HOUSE, INDEPENDENT_VILLA, GATED_COMMUNITY_HOUSE, GATED_COMMUNITY_VILLA, PG, HOSTEL
    }

    public enum BhkType {
        RK, BHK1, BHK2, BHK3, BHK4, BHK5
    }

    public enum Facing {
        NORTH, SOUTH, EAST, WEST, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST
    }

    public enum FloorType {
        LAMINATE_FLOORING, VINYL_FLOORING, HARDWOOD_FLOORING, CARPET_FLOORING, CERAMIC_TILES_FLOORING, STONE_FLOORING, CONCRETE_FLOORING, MARBLE_FLOORING, GRANITE_FLOORING, BAMBOO_FLOORING
    }

    public enum Furnishing {
        FULLY_FURNISHED, SEMI_FURNISHED, UN_FURNISHED
    }

    public enum Kitchen {
        ISLAND_MODULAR, PARALLEL_MODULAR, STRAIGHT_MODULAR, L_SHAPED_MODULAR, U_SHAPED_MODULAR, OPEN_MODULAR
    }

    public enum AvailableFor {
        FAMILY, SINGLE_MEN, SINGLE_WOMEN
    }

    public enum Parking {
        NO_PARKING, BIKE, CAR, CAR_AND_BIKE
    }

    public enum SellType {
        RENT, SELL
    }

}
