package edu.vt.EntityBeans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

// Ride EntityBean

@Entity
@Table(name = "ride")

@NamedQueries({

        @NamedQuery(name = "Ride.findAll", query = "SELECT u FROM Ride u")
        , @NamedQuery(name = "Ride.findById", query = "SELECT u FROM Ride u WHERE u.id = :id")
        , @NamedQuery(name = "Ride.findRidesByDriverId", query = "SELECT u FROM Ride u WHERE u.driverID.id = :driverId")
})


public class Ride implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;


    @JoinColumn(name = "driverId", referencedColumnName = "id")
    @OneToOne
    private Driver driverID;

    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;

    @Basic(optional = true)
    @Column(name = "seats")
    private Integer seats;

    @Basic(optional = false)
    @Column(name = "trip_start_time")
    private Timestamp tripStartTime;

    @Basic(optional = false)
    @Column(name = "trip_end_time")
    private Timestamp tripEndTime;

    @Basic(optional = false)
    @Column(name = "driver_rating")
    private float driverRating;

    @Basic(optional = false)
    @Column(name = "trip_Earning")
    private float tripEarning;

    @Basic(optional = false)
    @Column(name = "fare")
    private float fare;

    @JoinColumn(name = "start_location", referencedColumnName = "id")
    @OneToOne
    private Location startLocation;

    @JoinColumn(name = "end_location", referencedColumnName = "id")
    @OneToOne
    private Location destinationLocation;

    @Basic(optional = false)
    @Column(name = "distance")
    private float distance;

    @Transient
    private Date startTime;


    // Default Constructor
    public Ride() {

    }

    // Param Constructor
    public Ride(Integer id, Integer seats, Driver driverID, int status, Timestamp tripStartTime, Timestamp tripEndTime, float driverRating, float tripEarning, Location startLocation, Location destinationLocation) {
        this.id = id;
        this.driverID = driverID;
        this.status = status;
        this.tripStartTime = tripStartTime;
        this.tripEndTime = tripEndTime;
        this.driverRating = driverRating;
        this.seats = seats;
        this.tripEarning = tripEarning;
        this.startLocation = startLocation;
        this.destinationLocation = destinationLocation;
    }

    // Getter and Setter methods for the attributes (columns)

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Driver getDriverID() {
        return driverID;
    }

    public void setDriverID(Driver driverID) {
        this.driverID = driverID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Timestamp getTripStartTime() {
        return tripStartTime;
    }

    public void setTripStartTime(Timestamp tripStartTime) {
        this.tripStartTime = tripStartTime;
    }

    public Timestamp getTripEndTime() {
        return tripEndTime;
    }

    public void setTripEndTime(Timestamp tripEndTime) {
        this.tripEndTime = tripEndTime;
    }

    public float getDriverRating() {
        return driverRating;
    }

    public int getDriverRatingInInt(){
        return (int)driverRating;
    }

    public void setDriverRating(float driverRating) {
        this.driverRating = driverRating;
    }


    public float getTripEarning() {
        return tripEarning;
    }

    public void setTripEarning(float tripEarning) {
        this.tripEarning = tripEarning;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(Location destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public Date getStartTime() {
        Date date = new Date(this.getTripStartTime().getTime());
        return date;
        //return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStartTime(int original) {
        return startTime;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getFare() {
        return fare;
    }

    public void setFare(float price) {
        this.fare = price;
    }

    // Instance Methods Used Internally

    // Generate and return a hash code value for the object with database primary key id
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }


    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Ride)) {
            return false;
        }
        Ride other = (Ride) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    // Return String representation of database primary key id
    @Override
    public String toString() {
        return id.toString();
    }
}
