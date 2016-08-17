package cc.coopersoft.estate.actions;

import cc.coopersoft.common.EntityHome;
import cc.coopersoft.estate.model.HouseInfo;
import cc.coopersoft.estate.model.Persion;
import cc.coopersoft.estate.model.PowerCard;
import cc.coopersoft.estate.repository.PowerCardRep;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.util.*;

/**
 * Created by cooper on 8/17/16.
 */
@Named
@ConversationScoped
public class PowerCardHome extends EntityHome<PowerCard,String> {

    @Produces
    @Default
    @SessionScoped
    @Named
    public List<PowerCard.OwnerType> getOwnerTypeList(){
        return Arrays.asList(PowerCard.OwnerType.values());
    }


    @Inject
    private PowerCardRep powerCardRep;



    @Inject
    private FacesContext facesContext;

    @Inject
    @Default
    private EntityManager entityManager;

    private List<Persion> personList;

    protected PowerCard createInstance() {
        return new PowerCard(PowerCard.OwnerType.SINGLE);
    }

    protected EntityRepository<PowerCard, String> getEntityRepository() {
        return powerCardRep;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected void clearDirtyInstance(){
        super.clearDirtyInstance();
        personList = null;
    }

    public List<Persion> getPersonList() {
        if (personList == null){
            personList = new ArrayList<Persion>(getInstance().getPersions());
            Collections.sort(personList, new Comparator<Persion>() {
                public int compare(Persion o1, Persion o2) {
                    return o1.getId().compareTo(o2.getId());
                }
            });
        }
        return personList;
    }

    @PostConstruct
    public void initParam(){
        setId(facesContext.getExternalContext().getRequestParameterMap().get("powerCardId"));
    }

}
