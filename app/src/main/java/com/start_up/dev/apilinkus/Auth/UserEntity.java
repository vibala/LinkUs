package com.start_up.dev.apilinkus.Auth;

import java.util.Date;

/**
 * Created by Vignesh on 11/9/2016.
 */

public class UserEntity {

        private String id;

        private String lastName;

        private String firstName;

        private String email;

        private String password;

        private String sexe;

        private Date dateofBirth;

        private Role role;

        public String getSexe() {
            return sexe;
        }

        public void setSexe(String sexe) {
            this.sexe = sexe;
        }

        public Date getDateofBirth() {
            return dateofBirth;
        }

        public void setDateofBirth(Date dateofBirth) {
            this.dateofBirth = dateofBirth;
        }


        public UserEntity() {
            // Default Constructor
        }
        public UserEntity(String lastName, String firstName, String email, String password, String sexe,Date dateofBirth,String role) {
            this.lastName = lastName;
            this.firstName = firstName;
            this.email = email;
            this.password = password;
            this.sexe = sexe;
            this.dateofBirth = dateofBirth;
            this.role = Role.valueOf(role);
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
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

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }

        @Override
        public String toString() {
           return "\nFirst Name:" + getFirstName()
                   + "\nLast Name: " + getLastName()
                   + "\nEmail: " + getEmail()
                   + "\nPassword: " + getPassword()
                   + "\nRole: " + getRole().name();
        }
}
