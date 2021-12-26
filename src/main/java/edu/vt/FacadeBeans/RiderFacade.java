package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.Driver;
import edu.vt.EntityBeans.Rider;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

// Rider Facade

// @Stateless annotation implies that the conversational state with the client shall not be maintained.
@Stateless
public class RiderFacade extends AbstractFacade<Rider> {

    @PersistenceContext(unitName = "RideNowPU")
    private EntityManager entityManager;


    public RiderFacade() {
        super(Rider.class);
    }


    // Find by email
    public Rider findByEmail(String email) {
        if (entityManager.createQuery("SELECT c FROM Rider c WHERE c.email = :email")
                .setParameter("email", email)
                .getResultList().isEmpty()) {
            return null;
        } else {
            return (Rider) (entityManager.createQuery("SELECT c FROM Rider c WHERE c.email = :email")
                    .setParameter("email", email)
                    .getSingleResult());
        }
    }

    // Obtain the object reference of the EntityManager instance in charge of
    // managing the entities in the persistence context identified above.
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

}
