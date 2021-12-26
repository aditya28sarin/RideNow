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


// RiderPhoto EntityBean
@Entity
// Name of the database table represented
@Table(name = "riderphoto")

@NamedQueries({
        @NamedQuery(name = "RiderPhoto.findPhotosByUserDatabasePrimaryKey", query = "SELECT p FROM RiderPhoto p WHERE p.riderId.id = :primaryKey")
        , @NamedQuery(name = "RiderPhoto.findAll", query = "SELECT u FROM RiderPhoto u")
        , @NamedQuery(name = "RiderPhoto.findById", query = "SELECT u FROM RiderPhoto u WHERE u.id = :id")
        , @NamedQuery(name = "RiderPhoto.findByExtension", query = "SELECT u FROM RiderPhoto u WHERE u.extension = :extension")
})

public class RiderPhoto implements Serializable {

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
    @JoinColumn(name = "rider_id", referencedColumnName = "id")
    @ManyToOne
    private Rider riderId;

    // Default Constructor
    public RiderPhoto() {
    }

    // Called from UserPhotoController
    public RiderPhoto(String fileExtension, Rider id) {
        this.extension = fileExtension;
        riderId = id;
    }


    // Getter and Setter Functions
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExtension() {
        return extension;
    }

    public Rider getRiderId() {
        return riderId;
    }


    // Instance Methods Used Internally
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }


    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RiderPhoto)) {
            return false;
        }
        RiderPhoto other = (RiderPhoto) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return id.toString();
    }


    public String getPhotoFilename() {
        return getRiderId() + "." + getExtension();
    }

    public String getThumbnailFileName() {
        return getRiderId() + "_thumbnail." + getExtension();
    }

    public String getPhotoFilePath() {
        return Constants.PHOTOS_ABSOLUTE_PATH_RIDER + getPhotoFilename();
    }

    public String getThumbnailFilePath() {
        return Constants.PHOTOS_ABSOLUTE_PATH_RIDER + getThumbnailFileName();
    }

}
