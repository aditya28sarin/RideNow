package edu.vt.EntityBeans;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

// Location EntityBean
@Entity

@Table(name = "location")


@NamedQueries({
        @NamedQuery(name = "Location.findAll", query = "SELECT u FROM Location u"),
        @NamedQuery(name = "Location.findById", query = "SELECT u FROM Location u WHERE u.id = :id"),
        @NamedQuery(name = "Location.findByLandmarkName", query = "SELECT u FROM Location u WHERE u.landmarkName = :landmarkName"),
        @NamedQuery(name = "Location.findByZipCode", query = "SELECT u FROM Location u WHERE u.zipCode = :zipCode")})

public class Location implements  Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "latitude")
    private String latitude;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "longitude")
    private String longitude;

    @Basic(optional = false)
    @Size(min = 1, max = 256)
    @Column(name = "Landmark_Name")
    private String landmarkName;

    @Basic(optional = false)
    @Column(name = "zip_code")
    private int zipCode;

    // Default Constructor
    public Location(){

    }

    // Param Constructors
    public Location(Integer id) {
        this.id = id;
    }


    public Location(Integer id, String latitude, String longitude, String landmarkName, int zipCode) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.landmarkName = landmarkName;
        this.zipCode = zipCode;
    }

    // Getter and Setter methods for the attributes (columns)

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLandmarkName() {
        return landmarkName;
    }

    public void setLandmarkName(String landmarkName) {
        this.landmarkName = landmarkName;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
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
        if (!(object instanceof Location)) {
            return false;
        }
        Location other = (Location) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    // Return String representation of database primary key id
    @Override
    public String toString() {
        return id.toString();
    }
}
