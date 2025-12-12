package entities;

import java.time.LocalDate;
import java.util.Date;

public class Booking {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(int numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    private int id;
    private User user;
    private Room room;
    private Date checkIn;
    private Date checkOut;
    private LocalDate createdAt;
    private int numberOfNights;

    public Booking(int id, User user, Date checkIn, Room room, Date checkOut, LocalDate createdAt, int numberOfNights, int totalCost) {
        this.id = id;
        this.user = user;
        this.checkIn = checkIn;
        this.room = room;
        this.checkOut = checkOut;
        this.createdAt = createdAt;
        this.numberOfNights = numberOfNights;
        this.totalCost = totalCost;
    }

    public Booking(User user, Room room, Date checkIn, Date checkOut, LocalDate createdAt, int totalCost) {
        this.user = user;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.createdAt = createdAt;
        this.totalCost = totalCost;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    private int totalCost;

    public Booking(User user, Room room, Date checkIn, Date checkOut, LocalDate createdAt) {
        this.user = user;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.createdAt = createdAt;

    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }


    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }
}
