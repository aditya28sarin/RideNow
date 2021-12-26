package edu.vt.EntityBeans;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



// Cab EntityBean

@Entity

@Table(name = "cab")

@NamedQueries({
        @NamedQuery(name = "Cab.findAll", query = "SELECT u FROM Cab u"),
        @NamedQuery(name = "Cab.findById", query = "SELECT u FROM Cab u WHERE u.id = :id")})

public class Cab implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "brand")
    private String brand;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "model")
    private String model;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "registration_no")
    private String registrationNumber;

    @Basic(optional = false)
    @NotNull
    @Column(name = "type")
    private int type;

    @Basic(optional = false)
    @NotNull
    @Column(name = "capacity")
    private int capacity;


    public Cab() {
    }


    // Param Constructors


    public Cab(Integer id) {
        this.id = id;
    }

    public Cab(Integer id, String brand, String model, String registrationNumber, int type, int capacity) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.type = type;
        this.capacity = capacity;
    }


    // Getter and Setter methods for the attributes (columns)

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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
        if (!(object instanceof Cab)) {
            return false;
        }
        Cab other = (Cab) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    // Return String representation of database primary key id
    @Override
    public String toString() {
        return id.toString();
    }

}
