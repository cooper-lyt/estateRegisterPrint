package cc.coopersoft.estate.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by cooper on 8/16/16.
 */
@Entity
@Table(name = "PERSION",catalog = "ESTATE_PRINT")
public class Persion implements java.io.Serializable{
    private String id;
    private String name;
    private BigDecimal poolArea;
    private String number;

    private PowerCard powerCard;



    public Persion() {
    }


    public Persion(String id, PowerCard powerCard) {
        this.id = id;
        this.powerCard = powerCard;
    }

    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 128)
    @NotNull
    @Size(max = 128)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "NAME", nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "POOL_AREA")
    public BigDecimal getPoolArea() {
        return poolArea;
    }

    public void setPoolArea(BigDecimal poolArea) {
        this.poolArea = poolArea;
    }

    @Column(name = "NUMBER",nullable = false, length = 50)
    @NotNull
    @Size(max = 50)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "CARD", nullable = false)
    @NotNull
    public PowerCard getPowerCard() {
        return powerCard;
    }

    public void setPowerCard(PowerCard powerCard) {
        this.powerCard = powerCard;
    }

}
