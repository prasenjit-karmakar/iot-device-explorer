package com.device.explorer.dto;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Prasenjit Karmakar
 */
public class DeviceTwinInfo {
    @NotEmpty
    private String manufacturer;
    @NotEmpty
    private String modelNumber;
    @NotEmpty
    private String lastServiceDate;
    @NotEmpty
    private String nextServiceDate;


    public DeviceTwinInfo() {
    }


    public String getManufacturer() {
        return manufacturer;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public String getLastServiceDate() {
        return lastServiceDate;
    }

    public String getNextServiceDate() {
        return nextServiceDate;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public void setLastServiceDate(String lastServiceDate) {
        this.lastServiceDate = lastServiceDate;
    }

    public void setNextServiceDate(String nextServiceDate) {
        this.nextServiceDate = nextServiceDate;
    }
}
