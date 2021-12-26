/*
 * Created by Aditya Sarin on 2021.11.25
 * Copyright © 2021 Aditya Sarin. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.Rider;
import edu.vt.EntityBeans.RiderPhoto;
import edu.vt.FacadeBeans.RiderPhotoFacade;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;
import org.imgscalr.Scalr;
import org.primefaces.event.CaptureEvent;
import org.primefaces.model.file.UploadedFile;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.inject.Named;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;


// Rider Photo Controller

@Named("riderPhotoController")
@SessionScoped
public class RiderPhotoController implements Serializable {

    // Instance Variables

    private String filename;
    private UploadedFile file;


    @EJB
    private RiderPhotoFacade riderPhotoFacade;

    //    Getter and Setter Methods
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    //  Instance Methods
    public String capturedPhoto() {
        return Constants.PHOTOS_URI_RIDER + filename;
    }

    //   Handle User Photo Capture
    public void onCapture(CaptureEvent captureEvent) {


        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        Rider signedInUser = (Rider) sessionMap.get("rider");


        filename = signedInUser.getId() + "_tempFile";

        String absolutePathOfFilename = Constants.PHOTOS_ABSOLUTE_PATH_RIDER + filename;

        File capturedPhotoTemporaryFile = new File(absolutePathOfFilename);

        FileImageOutputStream imageOutput;

        try {
            byte[] capturedPhotoImageData = captureEvent.getData();

            imageOutput = new FileImageOutputStream(capturedPhotoTemporaryFile);

            imageOutput.write(capturedPhotoImageData, 0, capturedPhotoImageData.length);

            imageOutput.close();

        } catch (IOException ex) {
            Methods.showMessage("Fatal Error",
                    "Unable to write captured photo image!",
                    "See: " + ex.getMessage());
            return;
        }

        deletePhoto();

        RiderPhoto newPhoto = new RiderPhoto("png", signedInUser);

        riderPhotoFacade.create(newPhoto);


        RiderPhoto photo = riderPhotoFacade.findPhotosByRiderPrimaryKey(signedInUser.getId()).get(0);

        try {
            File photoTempFile = new File(absolutePathOfFilename);

            InputStream inputStream = new FileInputStream(photoTempFile);

            File capturedPhotoFile = inputStreamToFile(inputStream, photo.getPhotoFilename());

            saveThumbnail(capturedPhotoFile, photo);

        } catch (IOException ex) {
            Methods.showMessage("Fatal Error",
                    "Unable to convert temp file into input stream!",
                    "See: " + ex.getMessage());
        }
    }

    //    Remove Captured Photo to Redo
    public String redo() {
        filename = "";
        return "/userPhoto/ChangePhoto?faces-redirect=true";
    }

    //    Handle User Photo to Upload
    public String upload() {

        Methods.preserveMessages();

        if (file.getSize() == 0) {
            Methods.showMessage("Information", "No File Selected!",
                    "You need to choose a file first before clicking Upload.");
            return "";
        }

        String mimeFileType = file.getContentType();

        if (mimeFileType.startsWith("image/")) {

            String fileExtension = mimeFileType.subSequence(6, mimeFileType.length()).toString();

            String fileExtensionInCaps = fileExtension.toUpperCase();

            switch (fileExtensionInCaps) {
                case "JPG":
                case "JPEG":
                case "PNG":
                case "GIF":
                    // File is an acceptable image type
                    break;
                default:
                    Methods.showMessage("Fatal Error", "Unrecognized File Type!",
                            "Selected file type is not a JPG, JPEG, PNG, or GIF!");
                    return "";
            }
        } else {
            Methods.showMessage("Fatal Error", "Unrecognized File Type!",
                    "Selected file type is not a JPG, JPEG, PNG, or GIF!");
            return "";
        }

        storePhotoFile(file);

        // Redirect to show the Profile page
        return "/riderAccount/profile?faces-redirect=true";
    }

    //   Store the Rider File
    public void storePhotoFile(UploadedFile file) {

        Methods.preserveMessages();

        try {
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
            Rider signedInUser = (Rider) sessionMap.get("rider");

            deletePhoto();

            String mimeFileType = file.getContentType();

            String fileExtension = mimeFileType.startsWith("image/") ? mimeFileType.subSequence(6, mimeFileType.length()).toString() : "png";

            RiderPhoto newPhoto = new RiderPhoto(fileExtension, signedInUser);

            riderPhotoFacade.create(newPhoto);

            RiderPhoto photo = riderPhotoFacade.findPhotosByRiderPrimaryKey(signedInUser.getId()).get(0);

            InputStream inputStream = file.getInputStream();

            File uploadedFile = inputStreamToFile(inputStream, photo.getPhotoFilename());

            saveThumbnail(uploadedFile, photo);

            Methods.showMessage("Information", "Your photo has been uploaded",
                    "");

        } catch (IOException ex) {
            Methods.showMessage("Fatal Error",
                    "Something went wrong while storing the user's photo file!",
                    "See: " + ex.getMessage());
        }
    }


    /**
     * @param inputStream of bytes to be written into file with name targetFilename
     * @return the created file targetFile
     */
    private File inputStreamToFile(InputStream inputStream, String targetFilename) throws IOException {

        File targetFile = null;

        try {

            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);

            targetFile = new File(Constants.PHOTOS_ABSOLUTE_PATH_RIDER, targetFilename);

            OutputStream outStream;


            outStream = new FileOutputStream(targetFile);

            outStream.write(buffer);
            outStream.close();

        } catch (IOException ex) {
            Methods.showMessage("Fatal Error",
                    "Something went wrong in input stream to file!",
                    "See: " + ex.getMessage());
        }
        return targetFile;
    }

    //    Save thumbnail Image
    private void saveThumbnail(File inputFile, RiderPhoto inputPhoto) {

        try {

            BufferedImage uploadedPhoto = ImageIO.read(inputFile);


            BufferedImage thumbnailPhoto = Scalr.resize(uploadedPhoto, Constants.THUMBNAIL_SIZE);

            File thumbnailPhotoFile = new File(Constants.PHOTOS_ABSOLUTE_PATH_RIDER, inputPhoto.getThumbnailFileName());

            ImageIO.write(thumbnailPhoto, inputPhoto.getExtension(), thumbnailPhotoFile);

        } catch (IOException ex) {
            Methods.showMessage("Fatal Error",
                    "Something went wrong while saving the thumbnail file!",
                    "See: " + ex.getMessage());
        }
    }

    //   Delete Rider Photo and Thumbnail
    public void deletePhoto() {

        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        Rider signedInUser = (Rider) sessionMap.get("rider");

        Integer primaryKey = signedInUser.getId();

        List<RiderPhoto> photoList = riderPhotoFacade.findPhotosByRiderPrimaryKey(primaryKey);

        if (!photoList.isEmpty()) {

            RiderPhoto photo = photoList.get(0);

            try {

                Files.deleteIfExists(Paths.get(photo.getPhotoFilePath()));


                Files.deleteIfExists(Paths.get(photo.getThumbnailFilePath()));


                riderPhotoFacade.remove(photo);

            } catch (IOException ex) {
                Methods.showMessage("Fatal Error",
                        "Something went wrong while deleting the user photo file!",
                        "See: " + ex.getMessage());
            }
        }

    }
}
