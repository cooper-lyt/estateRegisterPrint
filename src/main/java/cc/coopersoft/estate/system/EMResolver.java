package cc.coopersoft.estate.system;

import org.apache.deltaspike.data.api.EntityManagerResolver;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Created by cooper on 6/17/16.
 */
public class EMResolver implements org.apache.deltaspike.data.api.EntityManagerResolver {


    @Inject
    @Default
    private EntityManager em;

    public EntityManager resolveEntityManager() {
        return em;
    }
}
