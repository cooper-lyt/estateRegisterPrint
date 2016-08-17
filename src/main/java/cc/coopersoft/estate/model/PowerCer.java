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
@Table(name = "POWER_CER",catalog = "ESTATE_PRINT")
public class PowerCer {

    private String id;
    private String powerPerson;
    private String powerType;
    private BigDecimal money;
    private Date dateFrom;
    private Date dateTo;
    private PowerCard powerCard;

    private String number;

    private PowerCard.CardStatus status;

    public PowerCer() {
    }

    public PowerCer(PowerCard.CardStatus status) {
        this.status = status;
    }

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


    @Column(name = "POWER_PERSON",nullable = false,length = 50)
    @NotNull
    @Size(max = 50)
    public String getPowerPerson() {
        return powerPerson;
    }

    public void setPowerPerson(String powerPerson) {
        this.powerPerson = powerPerson;
    }

    @Column(name = "P_TYPE",nullable = false,length = 50)
    @NotNull
    @Size(max = 50)
    public String getPowerType() {
        return powerType;
    }


    public void setPowerType(String powerType) {
        this.powerType = powerType;
    }

    @Column(name = "MONEY",nullable = false)
    @NotNull
    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_FROM", nullable = false, length = 19, columnDefinition = "DATETIME")
    @NotNull
    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_TO", nullable = false, length = 19, columnDefinition = "DATETIME")
    @NotNull
    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS",nullable = false,length = 32)
    @NotNull
    public PowerCard.CardStatus getStatus() {
        return status;
    }

    public void setStatus(PowerCard.CardStatus status) {
        this.status = status;
    }

    @Column(name = "NUMBER", length = 100)
    @Size(max = 100)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
