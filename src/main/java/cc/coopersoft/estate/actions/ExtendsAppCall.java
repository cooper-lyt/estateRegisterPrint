package cc.coopersoft.estate.actions;

import cc.coopersoft.common.EnumHelper;
import cc.coopersoft.estate.model.HouseInfo;
import cc.coopersoft.estate.model.Persion;
import cc.coopersoft.estate.model.PowerCard;
import cc.coopersoft.estate.repository.PowerCardRep;

import javax.activation.MimeType;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Created by cooper on 8/17/16.
 */
@Path("/extends")
@RequestScoped
public class ExtendsAppCall {

    public static class PowerCardInfo{

        private PowerCard powerCard;
        private HouseInfo houseInfo;

        public PowerCardInfo(PowerCard powerCard) {
            this.powerCard = powerCard;
            this.houseInfo = powerCard.getHouseInfos().iterator().next();
        }

        public String getNumber(){
            return powerCard.getId();
        }

        public String getPersonName(){
            String result = "";
            for(Persion person: powerCard.getPersions()){
                if (!"".equals(result)){
                    result += ",";
                }
                result += person.getName();
            }
            return result;
        }

        public String getPoolType(){
            return powerCard.getOwnerType().name();
        }

        public String getHouseNumber(){
            return houseInfo.getId();
        }

        public String getAddress(){
            return houseInfo.getLandAddress() + "/" + houseInfo.getHouseAddress();
        }

        public String getPowerType(){
            return houseInfo.getLandPowerType() + "/" + houseInfo.getHousePowerType();
        }

        public String getPowerProperty(){
            return houseInfo.getLandPowerProperty() + "/" + houseInfo.getHousePowerProperty();
        }
        public String getUseType(){
            return houseInfo.getLandUseType() + "/" + houseInfo.getHouseUseType();
        }

        public String getArea(){
            DecimalFormat format = new DecimalFormat("#0.00");
            return "宗地面积 " +  format.format(houseInfo.getLandArea()) + "/ 房屋建筑面积" + format.format(houseInfo.getHouseArea());
        }

        public String getTimeLimit(){
            SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
            return houseInfo.getLandPowerType() + ":" + dateformat.format(houseInfo.getLandDateFrom()) + "至" + dateformat.format(houseInfo.getLandDateTo());
        }

        public String getHouseStructure(){
            return houseInfo.getHouseStruct();
        }

        public BigDecimal getHouseUseArea(){
            return houseInfo.getUseArea();
        }

        public BigDecimal getHouseCommArea(){
            return houseInfo.getCommArea();
        }

        public int getFloorCount(){
            return houseInfo.getFloorCount();
        }

        public String getInfloorName(){
            return houseInfo.getInFloorName();
        }

    }

    @Inject
    private PowerCardRep powerCardRep;


    @Path("/powerCard/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PowerCardInfo getPowerCard(@PathParam("id") String id){
        return new PowerCardInfo(powerCardRep.findBy(id));
    }

}
