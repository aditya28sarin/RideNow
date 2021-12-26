/*
 * Created by Aditya Sarin on 2021.11.25
 * Copyright © 2021 Aditya Sarin. All rights reserved.
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.RiderPhoto;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// RiderPhoto Facade

@Stateless
public class RiderPhotoFacade extends AbstractFacade<RiderPhoto> {

    @PersistenceContext(unitName = "RideNowPU")
    private EntityManager entityManager;


    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public RiderPhotoFacade() {
        super(RiderPhoto.class);
    }


    // find photos by rider primary key
    public List<RiderPhoto> findPhotosByRiderPrimaryKey(Integer primaryKey) {

        return (List<RiderPhoto>) entityManager.createNamedQuery("RiderPhoto.findPhotosByUserDatabasePrimaryKey")
                .setParameter("primaryKey", primaryKey)
                .getResultList();
    }

}
