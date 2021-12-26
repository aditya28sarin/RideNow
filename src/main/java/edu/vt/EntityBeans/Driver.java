package edu.vt.EntityBeans;

import java.io.Serializable;
import java.util.Date;
import javax.enterprise.inject.Typed;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


// Driver EntityBean

@Entity
@Table(name = "driver")

@NamedQueries({
        @NamedQuery(name = "Driver.findAll", query = "SELECT u FROM Driver u"),
        @NamedQuery(name = "Driver.findById", query = "SELECT u FROM Driver u WHERE u.id = :id"),
        @NamedQuery(name = "Driver.findByUsername", query = "SELECT u FROM Driver u WHERE u.name = :name"),
        @NamedQuery(name = "Driver.findByPassword", query = "SELECT u FROM Driver u WHERE u.password = :password"),
        @NamedQuery(name = "Driver.findByEmail", query = "SELECT u FROM Driver u WHERE u.email = :email")})

public class Driver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "name")
    private String name;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "email")
    private String email;

    // To store Salted and Hashed Password Parts
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "password")
    private String password;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "phone_no")
    private String phoneNo;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "gender")
    private String gender;

    @Basic(optional = false)
    @NotNull
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Basic(optional = false)
    @Size(min = 1, max = 256)
    @Column(name = "cities")
    private String cities;

    @Basic(optional = false)
    @NotNull
    @Column(name = "overall_rating")
    private float overallRating;

    @Basic(optional = false)
    @NotNull
    @Column(name = "total_earnings")
    private float totalEarnings;

    @Basic(optional = false)
    @Size(min = 1, max = 256)
    @Column(name = "interests")
    private String interests;

    @Basic(optional = false)
    @NotNull
    @Column(name = "number_of_trips")
    private int numberOfTrips;

    @Basic(optional = false)
    @NotNull
    @Column(name = "total_kms_driven")
    private float totalKmsDriven;

    @JoinColumn(name = "cabid", referencedColumnName = "id")
    @OneToOne
    private Cab cabId;

    // Refers to the number of the selected security question
    @Basic(optional = false)
    @NotNull
    @Column(name = "security_question_number")
    private int securityQuestionNumber;


    // security_answer VARCHAR(128) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "security_answer")
    private String securityAnswer;


    // Default Constructor

    public Driver() {
    }


    // Param Constructor
    public Driver(Integer id) {
        this.id = id;
    }

    public Driver(Integer id, String name, String email, String password, String phoneNo, String gender, Date dateOfBirth, String cities, float overallRating, float totalEarnings, String interests, int numberOfTrips, int totalKmsDriven, Cab cabId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.cities = cities;
        this.overallRating = overallRating;
        this.totalEarnings = totalEarnings;
        this.interests = interests;
        this.numberOfTrips = numberOfTrips;
        this.totalKmsDriven = totalKmsDriven;
        this.cabId = cabId;
    }

    // Getter and Setter methods for the attributes (columns)

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCities() {
        return cities;
    }

    public void setCities(String cities) {
        this.cities = cities;
    }

    public float getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(float overallRating) {
        this.overallRating = overallRating;
    }

    public float getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(float totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public int getNumberOfTrips() {
        return numberOfTrips;
    }

    public void setNumberOfTrips(int numberOfTrips) {
        this.numberOfTrips = numberOfTrips;
    }

    public float getTotalKmsDriven() {
        return totalKmsDriven;
    }

    public void setTotalKmsDriven(float totalKmsDriven) {
        this.totalKmsDriven = totalKmsDriven;
    }

    public Cab getCabId() {
        return cabId;
    }

    public void setCabId(Cab cabId) {
        this.cabId = cabId;
    }

    public int getSecurityQuestionNumber() {
        return securityQuestionNumber;
    }

    public void setSecurityQuestionNumber(int securityQuestionNumber) {
        this.securityQuestionNumber = securityQuestionNumber;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
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
        if (!(object instanceof Driver)) {
            return false;
        }
        Driver other = (Driver) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    // Return String representation of database primary key id
    @Override
    public String toString() {
        return id.toString();
    }


}
