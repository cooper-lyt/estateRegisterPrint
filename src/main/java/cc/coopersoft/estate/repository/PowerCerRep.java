package cc.coopersoft.estate.repository;

import cc.coopersoft.estate.model.PowerCard;
import cc.coopersoft.estate.model.PowerCer;
import cc.coopersoft.estate.system.EMResolver;
import org.apache.deltaspike.data.api.EntityManagerConfig;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import javax.persistence.FlushModeType;

/**
 * Created by cooper on 8/17/16.
 */
@Repository
@EntityManagerConfig(entityManagerResolver = EMResolver.class, flushMode = FlushModeType.COMMIT)
public interface PowerCerRep extends EntityRepository<PowerCer,String> {




}
