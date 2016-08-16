package cc.coopersoft.estate.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by cooper on 8/16/16.
 */
@Entity
@Table(name = "POWER_CARD",catalog = "ESTATE_PRINT")
public class PowerCard implements java.io.Serializable{

    public enum OwnerType{
        SINGLE,POOL_TOG,POOL_PER
    }

    private String id;

    private OwnerType ownerType;

    private Set<HouseInfo> houseInfos = new HashSet<HouseInfo>(0);
    private Set<Persion> persions = new HashSet<Persion>(0);
    private Set<PowerCer> powerCers = new HashSet<PowerCer>(0);

    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "OWNER_TYPE",nullable = false,length = 50)
    @NotNull
    public OwnerType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "powerCard",cascade = CascadeType.ALL)
    public Set<HouseInfo> getHouseInfos() {
        return houseInfos;
    }

    public void setHouseInfos(Set<HouseInfo> houseInfos) {
        this.houseInfos = houseInfos;
    }

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "powerCard",cascade = CascadeType.ALL)
    public Set<Persion> getPersions() {
        return persions;
    }

    public void setPersions(Set<Persion> persions) {
        this.persions = persions;
    }

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "powerCard",cascade = CascadeType.ALL)
    public Set<PowerCer> getPowerCers() {
        return powerCers;
    }

    public void setPowerCers(Set<PowerCer> powerCers) {
        this.powerCers = powerCers;
    }
}
