package cc.coopersoft.estate.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by cooper on 8/16/16.
 */
@Entity
@Table(name = "HOUSE_INFO",catalog = "ESTATE_PRINT")
public class HouseInfo implements java.io.Serializable{

    private String id;

    private String landAddress;
    private String houseAddress;
    private String landPowerType;
    private String housePowerType;
    private String landPowerProperty;
    private String housePowerProperty;
    private String landUseType;
    private String houseUseType;
    private BigDecimal landArea;
    private BigDecimal houseArea;
    private Date landDateFrom;
    private Date landDateTo;
    private String houseStruct;
    private BigDecimal commArea;
    private BigDecimal useArea;
    private int floorCount;
    private String inFloorName;

    private PowerCard powerCard;

    public HouseInfo() {
    }

    public HouseInfo(PowerCard powerCard) {
        this.powerCard = powerCard;
    }

    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 28)
    @NotNull
    @Size(max = 28)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "LAND_ADDRESS",nullable = false,length = 512)
    @NotNull
    @Size(max = 512)
    public String getLandAddress() {
        return landAddress;
    }

    public void setLandAddress(String landAddress) {
        this.landAddress = landAddress;
    }
    @Column(name = "HOUSE_ADDRESS",nullable = false,length = 512)
    @NotNull
    @Size(max = 512)
    public String getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }


    @Column(name = "LAND_POWER_TYPE",nullable = false,length = 50)
    @NotNull
    @Size(max = 50)
    public String getLandPowerType() {
        return landPowerType;
    }

    public void setLandPowerType(String landPowerType) {
        this.landPowerType = landPowerType;
    }

    @Column(name = "HOUSE_POWER_TYPE",nullable = false,length = 50)
    @NotNull
    @Size(max = 50)
    public String getHousePowerType() {
        return housePowerType;
    }

    public void setHousePowerType(String housePowerType) {
        this.housePowerType = housePowerType;
    }

    @Column(name = "LAND_POWER_PROPERTY",nullable = false,length = 50)
    @NotNull
    @Size(max = 50)
    public String getLandPowerProperty() {
        return landPowerProperty;
    }

    public void setLandPowerProperty(String landPowerProperty) {
        this.landPowerProperty = landPowerProperty;
    }

    @Column(name = "HOUSE_POWER_PROPERTY",nullable = false,length = 50)
    @NotNull
    @Size(max = 50)
    public String getHousePowerProperty() {
        return housePowerProperty;
    }

    public void setHousePowerProperty(String housePowerProperty) {
        this.housePowerProperty = housePowerProperty;
    }




    @Column(name = "LAND_USE_TYPE",nullable = false,length = 50)
    @NotNull
    @Size(max = 50)
    public String getLandUseType() {
        return landUseType;
    }

    public void setLandUseType(String landUseType) {
        this.landUseType = landUseType;
    }

    @Column(name = "HOUSE_USE_TYPE",nullable = false,length = 50)
    @NotNull
    @Size(max = 50)
    public String getHouseUseType() {
        return houseUseType;
    }

    public void setHouseUseType(String houseUseType) {
        this.houseUseType = houseUseType;
    }

    @Column(name = "LAND_AREA",nullable = false)
    @NotNull
    public BigDecimal getLandArea() {
        return landArea;
    }

    public void setLandArea(BigDecimal landArea) {
        this.landArea = landArea;
    }


    @Column(name = "HOUSE_AREA",nullable = false)
    @NotNull
    public BigDecimal getHouseArea() {
        return houseArea;
    }

    public void setHouseArea(BigDecimal houseArea) {
        this.houseArea = houseArea;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAND_DATE_FROM", nullable = false, length = 19, columnDefinition = "DATETIME")
    @NotNull
    public Date getLandDateFrom() {
        return landDateFrom;
    }

    public void setLandDateFrom(Date landDateFrom) {
        this.landDateFrom = landDateFrom;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAND_DATE_TO", nullable = false, length = 19, columnDefinition = "DATETIME")
    @NotNull
    public Date getLandDateTo() {
        return landDateTo;
    }

    public void setLandDateTo(Date landDateTo) {
        this.landDateTo = landDateTo;
    }


    @Column(name = "HOUSE_STRUCT",nullable = false,length = 50)
    @NotNull
    @Size(max = 50)
    public String getHouseStruct() {
        return houseStruct;
    }

    public void setHouseStruct(String houseStruct) {
        this.houseStruct = houseStruct;
    }

    @Column(name = "HOUSE_COMM_AREA")
    public BigDecimal getCommArea() {
        return commArea;
    }

    public void setCommArea(BigDecimal commArea) {
        this.commArea = commArea;
    }

    @Column(name = "HOUSE_USE_AREA")
    public BigDecimal getUseArea() {
        return useArea;
    }

    public void setUseArea(BigDecimal useArea) {
        this.useArea = useArea;
    }

    @Column(name = "FLOOR_COUNT",nullable = false)
    public int getFloorCount() {
        return floorCount;
    }

    public void setFloorCount(int floorCount) {
        this.floorCount = floorCount;
    }


    @Column(name = "IN_FLOOR_NAME",length = 32)
    @Size(max = 32)
    public String getInFloorName() {
        return inFloorName;
    }

    public void setInFloorName(String inFloorName) {
        this.inFloorName = inFloorName;
    }

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "POWER_CARD", nullable = false)
    @NotNull
    public PowerCard getPowerCard() {
        return powerCard;
    }

    public void setPowerCard(PowerCard powerCard) {
        this.powerCard = powerCard;
    }
}
