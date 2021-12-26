package edu.vt.EntityBeans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

// Rider EntityBean

@Entity
@Table(name = "rider")

@NamedQueries({
        @NamedQuery(name = "Rider.findAll", query = "SELECT u FROM Rider u"),
        @NamedQuery(name = "Rider.findByName", query = "SELECT u FROM Rider u WHERE u.name = :name"),
        @NamedQuery(name = "Rider.findByUsername", query = "SELECT u FROM Rider u WHERE u.name = :name"),
        @NamedQuery(name = "Rider.findByPassword", query = "SELECT u FROM Rider u WHERE u.password = :password"),
        @NamedQuery(name = "Rider.findByEmail", query = "SELECT u FROM Rider u WHERE u.email = :email")})

public class Rider implements Serializable {

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
    @Column(name = "total_spend")
    private float totalSpend;

    @Basic(optional = false)
    @Size(min = 1, max = 256)
    @Column(name = "interests")
    private String interest;

    @Basic(optional = false)
    @NotNull
    @Column(name = "number_of_trips")
    private int numberOfTrips;

    @Basic(optional = false)
    @Size(min = 1, max = 50)
    @Column(name = "cellPhoneCarrier")
    private String cellPhoneCarrier;

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
    public Rider() {

    }

    // Param Constructor
    public Rider(Integer id, String name, String email, String password, String phoneNo, String gender, Date dateOfBirth, float totalSpend, String interest, int numberOfTrips) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.totalSpend = totalSpend;
        this.interest = interest;
        this.numberOfTrips = numberOfTrips;

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

    public float getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(float totalSpend) {
        this.totalSpend = totalSpend;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public int getNumberOfTrips() {
        return numberOfTrips;
    }

    public void setNumberOfTrips(int numberOfTrips) {
        this.numberOfTrips = numberOfTrips;
    }

    public String getCellPhoneCarrier() {
        return cellPhoneCarrier;
    }

    public void setCellPhoneCarrier(String cellPhoneCarrier) {
        this.cellPhoneCarrier = cellPhoneCarrier;
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
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    // Return String representation of database primary key id
    @Override
    public String toString() {
        return id.toString();
    }
}
