package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.Ride;
import edu.vt.EntityBeans.RideRider;
import edu.vt.EntityBeans.Rider;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// @Stateless annotation implies that the conversational state with the client shall not be maintained.
@Stateless
public class RideRiderFacade extends AbstractFacade<RideRider> {

    @PersistenceContext(unitName = "RideNowPU")
    private EntityManager entityManager;

    /*
    This constructor method invokes its parent AbstractFacade's constructor method,
    which in turn initializes its entity class type T and entityClass instance variable.
     */
    public RideRiderFacade() {
        super(RideRider.class);
    }

    // Obtain the object reference of the EntityManager instance in charge of
    // managing the entities in the persistence context identified above.
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    // Remove Ride Rider
    public void removeRideRider(Ride ride, Rider rider) {
        int x = getEntityManager().createQuery(
                        "DELETE FROM RideRider rr WHERE rr.rideId = :ride and rr.riderId = :rider")
                .setParameter("ride", ride)
                .setParameter("rider", rider)
                .executeUpdate();
        x = 5;
    }

    // Update Driver Rating
    public void updateDriverRating(int driverRating, Ride ride, Rider rider) {
        getEntityManager().createQuery(
                        "UPDATE RideRider rr SET rr.driverRating= :driverRating WHERE rr.rideId = :ride and rr.riderId = :rider")
                .setParameter("ride", ride)
                .setParameter("rider", rider)
                .setParameter("driverRating", driverRating)
                .executeUpdate();
    }

    // Delete Selected Ride
    public void deleteSelectedRide(Ride ride) {
        getEntityManager().createQuery("DELETE from RideRider rr where rr.rideId = :ride")
                .setParameter("ride", ride)
                .executeUpdate();
    }

    // Get Riders
    public List<Rider> getRiders(int RideId) {
        List<Rider> riders;
        riders = getEntityManager().createQuery("SELECT r.riderId from RideRider r where r.rideId.id = :RideId")
                .setParameter("RideId", RideId)
                .getResultList();
        return riders;
    }

    // Calculate average rating
    public double calculateAverageRating(int rideId) {
        double averageRating;
        averageRating = (double) getEntityManager().createQuery("SELECT AVG(r.driverRating) FROM RideRider r WHERE r.rideId.id = :RideId and r.driverRating <> 0")
                .setParameter("RideId", rideId)
                .getSingleResult();

        return averageRating;
    }

    // Have rated
    public boolean haveRated(Rider rider){
        List<RideRider> lst = getEntityManager().createQuery("SELECT rr from RideRider rr where rr.driverRating = 0 and rr.riderId = :riderId")
                .setParameter("riderId", rider)
                .getResultList();

        if(lst.size()!=0)
            return false;
        return true;
    }

    // get rating
    public int getRating(Ride rideId, Rider riderId){
        int rating;
        rating = (int) getEntityManager().createQuery("SELECT r.driverRating FROM RideRider r WHERE r.rideId.id = :RideId and r.riderId.id = :RiderId")
                .setParameter("RideId", rideId.getId())
                .setParameter("RiderId", riderId.getId())
                .getSingleResult();
        return rating;
    }

}
