/*
 * Created by Aditya Sarin on 2021.11.25
 * Copyright © 2021 Aditya Sarin. All rights reserved.
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.DriverPhoto;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// Driver Photo Facade

@Stateless
public class DriverPhotoFacade extends AbstractFacade<DriverPhoto> {

    @PersistenceContext(unitName = "RideNowPU")
    private EntityManager entityManager;


    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public DriverPhotoFacade() {
        super(DriverPhoto.class);
    }

    // Find Photos By Driver Primary Key
    public List<DriverPhoto> findPhotosByDriverPrimaryKey(Integer primaryKey) {

        return (List<DriverPhoto>) entityManager.createNamedQuery("DriverPhoto.findPhotosByUserDatabasePrimaryKey")
                .setParameter("primaryKey", primaryKey)
                .getResultList();
    }

}
