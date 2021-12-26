package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.Driver;
import edu.vt.EntityBeans.Ride;
import edu.vt.EntityBeans.Rider;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

// @Stateless annotation implies that the conversational state with the client shall not be maintained.
@Stateless
public class RideFacade extends AbstractFacade<Ride> {

    @PersistenceContext(unitName = "RideNowPU")
    private EntityManager entityManager;

    /*
    This constructor method invokes its parent AbstractFacade's constructor method,
    which in turn initializes its entity class type T and entityClass instance variable.
     */
    public RideFacade() {
        super(Ride.class);
    }

    // Obtain the object reference of the EntityManager instance in charge of
    // managing the entities in the persistence context identified above.
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    //  Other Methods


    // Returns the object reference of the Ride object whose primary key is id
    public Ride getDriverRide(int id) {
        return entityManager.find(Ride.class, id);
    }

    // Returns a list of object references of UserFile objects that belong to
    // the User object whose database Primary Key = primaryKey
    public List<Ride> findRidesByDriverPrimaryKey(Integer primaryKey) {

        return entityManager.createNamedQuery("Ride.findRidesByDriverId")
                .setParameter("driverId", primaryKey)
                .getResultList();
    }


    // Find Available Rides
    public List<Ride> findAvailableRides(String startLoc, String endLoc, Date startDate, int riderid) {

        startLoc = "%" + startLoc + "%";
        endLoc = "%" + endLoc + "%";

        if(startDate == null)
        {
            return getEntityManager().createQuery(
                            "SELECT c FROM Ride c WHERE c.startLocation.landmarkName LIKE :startLoc AND " +
                                    "c.destinationLocation.landmarkName LIKE :endLoc AND c.status = 0 AND c.seats >= 1" +
                                    "AND c.id NOT in (SELECT rr.rideId.id from RideRider rr where rr.riderId.id = :riderid)")
                    .setParameter("startLoc", startLoc)
                    .setParameter("endLoc", endLoc)
                    .setParameter("riderid", riderid)
                    .getResultList();
        }

        return getEntityManager().createQuery(
                        "SELECT c FROM Ride c WHERE c.startLocation.landmarkName LIKE :startLoc AND " +
                                "c.destinationLocation.landmarkName LIKE :endLoc AND c.tripStartTime >= :startDate AND c.status = 0 AND c.seats >= 1" +
                                "AND c.id NOT in (SELECT rr.rideId.id from RideRider rr where rr.riderId.id = :riderid)")
                .setParameter("startLoc", startLoc)
                .setParameter("endLoc", endLoc)
                .setParameter("startDate", startDate)
                .setParameter("riderid", riderid)
                .getResultList();

    }

    // Find History of rider function

    public List<Ride> findHistoryOfRider(Rider rider) {
        List<Integer> rideId;
        List<Ride> rides = null;
        //int riderId = rider.getId().intValue();
        rides = getEntityManager().createQuery("SELECT rr.rideId from RideRider rr where rr.riderId = :rider")
                .setParameter("rider", rider)
                .getResultList();


        return rides;
    }

    // Delete Selected Ride
    public void deleteSelectedRide(Ride ride)
    {
        getEntityManager().createQuery("DELETE from Ride r where r.id = :ride")
                .setParameter("ride", ride.getId())
                .executeUpdate();
    }

    // Get Longest Rides
    public List<Ride> getLongestRides(){
        List<Ride> rides =  getEntityManager().createQuery("SELECT r from Ride r where r.status = 2 order by r.distance desc")
                .getResultList();
        if(rides.size()>5)
            rides = rides.subList(0,5);
        return rides;
    }


    // Get Shortest Rides
    public List<Ride> getShortestRides(){
        List<Ride> rides =  getEntityManager().createQuery("SELECT r from Ride r where r.status = 2 order by r.distance asc")
                .getResultList();
        if(rides.size()>5)
            rides = rides.subList(0,5);
        return rides;
    }


    // Get total kms
    public float getTotalKms(Driver driver){
        float kms = 0.0f;
        List<Double> lst = getEntityManager().createQuery("SELECT SUM(r.distance) FROM Ride r where r.driverID = :driver and r.status=2")
                .setParameter("driver", driver)
                .getResultList();

        return kms;
    }


    // Get total earnings
    public float getTotalEarnings(Driver driver){
        float earnings = 0.0f;
        List<Double> lst = getEntityManager().createQuery("SELECT SUM(r.tripEarning) FROM Ride r where r.driverID = :driver")
                .setParameter("driver", driver)
                .getResultList();
        if(lst.size()>0)
            earnings = lst.get(0).floatValue();
        return earnings;
    }

}
