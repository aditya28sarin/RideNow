package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.Driver;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

// @Stateless annotation implies that the conversational state with the client shall not be maintained.
@Stateless
public class DriverFacade extends AbstractFacade<Driver> {

    @PersistenceContext(unitName = "RideNowPU")
    private EntityManager entityManager;

    /*
    This constructor method invokes its parent AbstractFacade's constructor method,
    which in turn initializes its entity class type T and entityClass instance variable.
     */
    public DriverFacade() {
        super(Driver.class);
    }


    // Find Driver By Email
    public Driver findByEmail(String email) {
        if (entityManager.createQuery("SELECT c FROM Driver c WHERE c.email = :email")
                .setParameter("email", email)
                .getResultList().isEmpty()) {
            return null;
        } else {
            return (Driver) (entityManager.createQuery("SELECT c FROM Driver c WHERE c.email = :email")
                    .setParameter("email", email)
                    .getSingleResult());
        }
    }

    // Get Top Rated Drivers
    public List<Driver> getTopRatedDrivers(){
        List<Driver> lst = new ArrayList<>();

        lst = entityManager.createQuery("SELECT d FROM Driver d ORDER BY d.overallRating DESC")
                .getResultList();

        if(lst.size()>5)
            lst = lst.subList(0,5);

        return lst;
    }

    // Get Average Rating of a Driver
    public float getAvgRating(Driver driver){
        float rating = 0.0f;
        List<Double> lst = getEntityManager().createQuery("SELECT AVG(r.driverRating) FROM Ride r where r.driverID = :driver and r.status=2 and r.driverRating <> 0")
                .setParameter("driver", driver)
                .getResultList();
        if(lst.size()>0)
            rating = lst.get(0).floatValue();
        return rating;
    }

    // Obtain the object reference of the EntityManager instance in charge of
    // managing the entities in the persistence context identified above.
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

}
