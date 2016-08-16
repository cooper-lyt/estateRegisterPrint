package cc.coopersoft.estate.system;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 * Created by cooper on 6/5/16.
 */
@Singleton
@Startup
public class SystemInitializer {

    @Inject
    private PartitionManager partitionManager;

    @PostConstruct
    public void create() {

        IdentityManager identityManager = this.partitionManager.createIdentityManager();

        final IdentityQueryBuilder queryBuilder = identityManager.getQueryBuilder();
        final IdentityQuery<User> query = queryBuilder.createIdentityQuery(User.class);

        query.where(queryBuilder.equal(User.LOGIN_NAME, "root"));

        if (query.getResultList().isEmpty()){
            User root = new User("root");
            root.setEmail("hr@acme.com");
            root.setFirstName("root");
            root.setLastName("root");
            identityManager.add(root);
            identityManager.updateCredential(root, new Password("dgsoft"));
        }else{
//            User root = query.getResultList().get(0);
//
//            identityManager.updateCredential(root, new Password("dgsoft"));

        }







//        // Create role "manager"
//        Role manager = new Role("manager");
//        identityManager.add(manager);
//
//        // Create application role "superuser"
//        Role superuser = new Role("superuser");
//        identityManager.add(superuser);
////
//        // Create group "sales"
//        Group sales = new Group("sales");
//        identityManager.add(sales);
//
//        RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();
//
//        // Make john a member of the "sales" group
//        addToGroup(relationshipManager, john, sales);
//
//        // Make mary a manager of the "sales" group
//        grantGroupRole(relationshipManager, mary, manager, sales);
//
//        // Grant the "superuser" application role to jane
//        grantRole(relationshipManager, jane, superuser);
    }
}
