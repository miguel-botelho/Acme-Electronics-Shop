package com.cmov.tomislaaaav.acme_electronics_shop.Structures;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by m_bot on 09/11/2017.
 */

public class User implements Serializable{

    public String id;
    public String name = "No";
    public String address;
    public String NIF;
    public String creditCardType;
    public String email;
    public String validity;
    public String creditCardNumber;
    public String publicKey;
    public String privateKey;

    public User() {

    }

    public User(String id, String name, String address, String NIF, String creditCardType, String email, String validity, String creditCardNumber, String publicKey, String privateKey) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.NIF = NIF;
        this.creditCardType = creditCardType;
        this.email = email;
        this.validity = validity;
        this.creditCardNumber = creditCardNumber;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getNIF() {
        return NIF;
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public String getEmail() {
        return email;
    }

    public String getValidity() {
        return validity;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    public void setCreditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public void saveObject(File f){
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(f + "/save_object.bin"))); //Select where you wish to save the file...
            oos.writeObject(this); // write the class as an 'object'
            oos.flush(); // flush the stream to insure all of the information was written to 'save_object.bin'
            oos.close();// close the stream
        }
        catch(Exception ex)
        {
            Log.v("Serialization Save Err", ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void loadSerializedObject(File f)
    {
        try
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(f + "/save_object.bin")));
            Object o = ois.readObject();
            this.id = ((User) o).id;
            this.address = ((User) o).address;
            this.name = ((User) o).name;
            this.email = ((User) o).email;
            this.NIF = ((User) o).NIF;
            this.creditCardType = ((User) o).creditCardType;
            this.creditCardNumber = ((User) o).creditCardNumber;
            this.validity = ((User) o).validity;
            this.publicKey = ((User) o).publicKey;
            this.privateKey = ((User) o).privateKey;
        }
        catch(Exception ex)
        {
            Log.v("Serialization Read Err",ex.getMessage());
            ex.printStackTrace();
        }
    }

}
