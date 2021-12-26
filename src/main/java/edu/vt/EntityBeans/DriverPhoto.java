/*
 * Created by Aditya Sarin on 2021.11.25
 * Copyright © 2021 Aditya Sarin. All rights reserved.
 */
package edu.vt.EntityBeans;

import edu.vt.globals.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

// DriverPhoto EntityBean

@Entity

@Table(name = "driverphoto")

@NamedQueries({
        @NamedQuery(name = "DriverPhoto.findPhotosByUserDatabasePrimaryKey", query = "SELECT p FROM DriverPhoto p WHERE p.driverId.id = :primaryKey")
        , @NamedQuery(name = "DriverPhoto.findAll", query = "SELECT u FROM DriverPhoto u")
        , @NamedQuery(name = "DriverPhoto.findById", query = "SELECT u FROM DriverPhoto u WHERE u.id = :id")
        , @NamedQuery(name = "DriverPhoto.findByExtension", query = "SELECT u FROM DriverPhoto u WHERE u.extension = :extension")
})

public class DriverPhoto implements Serializable {

    private static final long serialVersionUID = 1L;

    // id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    // extension ENUM('jpeg', 'jpg', 'png', 'gif') NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "extension")
    private String extension;

    // user_id INT UNSIGNED
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    @ManyToOne
    private Driver driverId;


    // Default Constructor
    public DriverPhoto() {
    }

    // Called from UserPhotoController
    public DriverPhoto(String fileExtension, Driver id) {
        this.extension = fileExtension;
        driverId = id;
    }


    // Getter and Setter Methods
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExtension() {
        return extension;
    }

    public Driver getDriverId() {
        return driverId;
    }


    //    Instance Methods Used Internally
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }


    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DriverPhoto)) {
            return false;
        }
        DriverPhoto other = (DriverPhoto) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return id.toString();
    }


    public String getPhotoFilename() {
        return getDriverId() + "." + getExtension();
    }

    public String getThumbnailFileName() {
        return getDriverId() + "_thumbnail." + getExtension();
    }

    public String getPhotoFilePath() {
        return Constants.PHOTOS_ABSOLUTE_PATH_DRIVER + getPhotoFilename();
    }

    public String getThumbnailFilePath() {
        return Constants.PHOTOS_ABSOLUTE_PATH_DRIVER + getThumbnailFileName();
    }

}
