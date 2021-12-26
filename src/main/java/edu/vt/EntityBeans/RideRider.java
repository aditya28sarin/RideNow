package edu.vt.EntityBeans;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;


// RideRider EntityBean
@Entity
@Table(name = "ride_rider")
/*
 * This entity maps the Ride with Rider
 * */
public class RideRider implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "ride_id", referencedColumnName = "id")
    @OneToOne
    private Ride rideId;

    @JoinColumn(name = "rider_id", referencedColumnName = "id")
    @OneToOne
    private Rider riderId;

    // driver's rating from the rider
    @Basic(optional = false)
    @NotNull
    @Column(name = "driverRating")
    private int driverRating;

    // Default Constructor
    public RideRider() {

    }

    // Param Constructor
    public RideRider(Ride rideId, Rider riderId, Integer id) {
        this.rideId = rideId;
        this.riderId = riderId;
        this.id = id;
    }


    // Getter and Setter methods for the attributes (columns)

    public Ride getRideId() {
        return rideId;
    }

    public void setRideId(Ride rideId) {
        this.rideId = rideId;
    }

    public Rider getRiderId() {
        return riderId;
    }

    public void setRiderId(Rider riderId) {
        this.riderId = riderId;
    }

    // Generate and return a hash code value for the object with database primary key id
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }


    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Rider)) {
            return false;
        }
        Rider other = (Rider) object;
        return (this.id != null || other.getId() == null) && (this.id == null || this.id.equals(other.getId()));
    }

    // Return String representation of database primary key id
    @Override
    public String toString() {
        return id.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(int driverRating) {
        this.driverRating = driverRating;
    }
}
