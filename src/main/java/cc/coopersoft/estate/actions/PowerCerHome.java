package cc.coopersoft.estate.actions;

import cc.coopersoft.common.EntityHome;
import cc.coopersoft.estate.model.PowerCard;
import cc.coopersoft.estate.model.PowerCer;
import cc.coopersoft.estate.repository.PowerCerRep;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

/**
 * Created by cooper on 8/17/16.
 */
@Named
@ConversationScoped
public class PowerCerHome extends EntityHome<PowerCer,String> {


    @Inject
    private PowerCerRep powerCerRep;

    @Inject
    private EntityManager entityManager;

    protected PowerCer createInstance() {
        return new PowerCer(PowerCard.CardStatus.PREPARE);
    }

    protected EntityRepository<PowerCer, String> getEntityRepository() {
        return powerCerRep;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Transactional
    public String printComplate(){
        getInstance().setStatus(PowerCard.CardStatus.PRINTED);
        saveOrUpdate();
        return "/home.xhtml";
    }

}
