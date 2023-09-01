package com.upgrad.Booking.Model;

import java.time.LocalDate;
import java.util.Objects;

public class BookingRequestEntity {
    private LocalDate fromDate;
    private LocalDate toDate;
    private String aadharNumber;
    private int numOfRooms;

    //Constructors
    public BookingRequestEntity(LocalDate fromDate, LocalDate toDate, String aadharNumber, int numOfRooms) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.aadharNumber = aadharNumber;
        this.numOfRooms = numOfRooms;
    }

    public BookingRequestEntity(){}

    //Getters and Setters
    public String getAadharNumber() {
        return aadharNumber;
    }
    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public int getNumOfRooms() {
        return numOfRooms;
    }

    public void setNumOfRooms(int numOfRooms) {
        this.numOfRooms = numOfRooms;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return "BookingRequestEntity{" +
                "fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", aadharNumber='" + aadharNumber + '\'' +
                ", numOfRooms=" + numOfRooms +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingRequestEntity that)) return false;
        return numOfRooms == that.numOfRooms && Objects.equals(fromDate, that.fromDate) && Objects.equals(toDate, that.toDate) && Objects.equals(aadharNumber, that.aadharNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromDate, toDate, aadharNumber, numOfRooms);
    }
}
