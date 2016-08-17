package cc.coopersoft.estate.actions;

import cc.coopersoft.estate.model.HouseInfo;
import cc.coopersoft.estate.model.Persion;
import cc.coopersoft.estate.model.PowerCard;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.UUID;

/**
 * Created by cooper on 8/17/16.
 */
@Named
@ConversationScoped
public class CreateSingleHousePowerCard implements java.io.Serializable{

    @Inject
    @Default
    private Conversation conversation;

    @Inject
    private PowerCardHome powerCardHome;

    private int poolOwnerCount;

    public int getPoolOwnerCount() {
        return poolOwnerCount;
    }

    public void setPoolOwnerCount(int poolOwnerCount) {
        this.poolOwnerCount = poolOwnerCount;
    }

    public HouseInfo getHouseInfo(){
        return powerCardHome.getInstance().getHouseInfos().iterator().next();
    }

    public String genPerson(){
        if (PowerCard.OwnerType.SINGLE.equals(powerCardHome.getInstance().getOwnerType())){
            if (powerCardHome.getInstance().getPersions().size() > 1){
                powerCardHome.getInstance().getPersions().clear();
            }
            if (powerCardHome.getInstance().getPersions().isEmpty()){
                powerCardHome.getInstance().getPersions().add(new Persion(UUID.randomUUID().toString().replace("-",""),powerCardHome.getInstance()));
            }
        }else{
            if (powerCardHome.getInstance().getPersions().size() != poolOwnerCount){
                powerCardHome.getInstance().getPersions().clear();
                for(int i=0; i<poolOwnerCount;i++){
                    powerCardHome.getInstance().getPersions().add(new Persion(UUID.randomUUID().toString().replace("-",""),powerCardHome.getInstance()));
                }
            }

        }
        return "/esate/createPowerCardInfo.xhtml";
    }

    @Transactional
    public void begin(){
        //logger.config("call begin change job:" + employeeHome.getInstance().getOffice().getName());
        if ( conversation.isTransient() )
        {
            conversation.begin();
            conversation.setTimeout(1200000);
        }

        powerCardHome.clearInstance();
        powerCardHome.getInstance().getHouseInfos().add(new HouseInfo(powerCardHome.getInstance()));

    }

    public String infoComplete(){
        powerCardHome.saveOrUpdate();
        endConversation();
        return "/esate/printPowerCard.xhtml";
    }


    @PreDestroy
    public void endConversation() {
        if ( !conversation.isTransient() )
        {
            conversation.end();
        }
    }

}
