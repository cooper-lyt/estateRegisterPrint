package cc.coopersoft.estate.actions;

import cc.coopersoft.common.Messages;
import cc.coopersoft.estate.model.PowerCard;
import cc.coopersoft.estate.repository.PowerCardRep;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.deltaspike.jsf.api.message.JsfMessage;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by cooper on 8/17/16.
 */
@Named
@RequestScoped
public class CreatePowerCer {

    private String houseId;

    @Inject
    private PowerCerHome powerCerHome;

    @Inject
    private PowerCardRep powerCardRep;

    @Inject
    private JsfMessage<Messages> messages;


    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }


    @Transactional
    public String savePowerCerInfo(){

        PowerCard powerCard = powerCardRep.findBy(houseId);
        if (powerCard == null){
            messages.addError().powerCardNotFound();
            return null;
        }else{
            powerCerHome.getInstance().setPowerCard(powerCard);
            powerCerHome.saveOrUpdate();
            return "/esate/printPowerCer.xhtml";
        }
    }



}
