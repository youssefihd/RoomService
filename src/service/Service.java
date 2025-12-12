package service;

import entities.Booking;
import entities.Room;
import entities.User;
import enums.RoomType;
import exceptions.InvalidDateException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Service {

    private ArrayList<Room> rooms;
    private ArrayList<User> users;
    private ArrayList<Booking> bookings;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Creates a new Service instance.
     */
    public Service() {
        this.rooms = new ArrayList<>();
        this.users = new ArrayList<>();
        this.bookings = new ArrayList<>();
    }


    public void setRoom(int roomId, RoomType roomType, int roomPricePerNight) {
        if (roomType == null) {
            throw new IllegalArgumentException("Room type cannot be null");
        }
        if (roomPricePerNight <= 0) {
            throw new IllegalArgumentException("Room price must be positive");
        }

        // Find existing room
        Room existingRoom = findRoomById(roomId);

        if (existingRoom != null) {
            // Update existing room
            existingRoom.setType(roomType);
            existingRoom.setPrice(roomPricePerNight);
            System.out.println(existingRoom.toString());
        } else {
            // Create new room
            Room newRoom = new Room(roomId,roomPricePerNight,roomType);
            rooms.add(newRoom);
            System.out.println(newRoom.toString());
        }
    }

    public void setUser(int userId,String fname,String lname,int balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }

        User existingUser = findUserById(userId);

        if (existingUser != null) {
            // Update existing user
            existingUser.setBalance(balance);
            System.out.println("User " + fname+" "+lname+ " updated successfully.");
        } else {
            // Create new user
            User newUser = new User(userId,fname,lname,balance);
            users.add(newUser);
            System.out.println("User " + fname+" "+lname+ " updated successfully.");
        }
    }


    public void bookRoom(int userId, int roomNumber, Date checkIn, Date checkOut) {
        try {
            validateDates(checkIn, checkOut);

            // Find user
            User user = findUserById(userId);
            if (user == null) {
                System.out.println("❌ Booking failed: User " + userId + " not found.");
                return;
            }

            // Find room
            Room room = findRoomById(roomNumber);
            if (room == null) {
                System.out.println("❌ Booking failed: Room " + roomNumber + " not found.");
                return;
            }

            // Calculate nights and cost
            int numberOfNights = calculateNights(checkIn, checkOut);
            int totalCost = numberOfNights * room.getPrice();

            // Check balance
            if (!user.hasSufficientBalance(totalCost)) {
                System.out.println("❌ Booking failed: User " + userId + " has insufficient balance. " +
                        "Required: " + totalCost + ", Available: " + user.getBalance());
                return;
            }

            // Check room availability
            if (!isRoomAvailable(roomNumber, checkIn, checkOut)) {
                System.out.println("❌ Booking failed: Room " + roomNumber +
                        " is not available from " + DATE_FORMAT.format(checkIn) +
                        " to " + DATE_FORMAT.format(checkOut));
                return;
            }
    LocalDate createdAt = LocalDate.now();
            // Create booking with snapshots
            Booking booking = new Booking(user, room, checkIn,checkOut, createdAt,totalCost);
            bookings.add(booking);

            // Deduct balance
            user.deductBalance(totalCost);

            System.out.println("✅ Booking successful! User " + userId + " booked Room " + roomNumber +
                    " from " + DATE_FORMAT.format(checkIn) + " to " + DATE_FORMAT.format(checkOut) +
                    " (" + numberOfNights + " nights). Total cost: " + totalCost +
                    ". Remaining balance: " + user.getBalance());

        } catch (InvalidDateException e) {
            System.out.println("❌ Booking failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Booking failed: " + e.getMessage());
        }
    }


    public void printAll() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                            ALL ROOMS AND BOOKINGS");
        System.out.println("=".repeat(80));


        System.out.println("-".repeat(80));
        if (rooms.isEmpty()) {
            System.out.println("No rooms available.");
        } else {
            for (int i = rooms.size() - 1; i >= 0; i--) {
                Room room = rooms.get(i);
                System.out.println(room.toString());
            }
        }


        System.out.println("-".repeat(80));
        if (bookings.isEmpty()) {
            System.out.println("No bookings available.");
        } else {
            // Sort by creation time (newest first)
            ArrayList<Booking> sortedBookings = new ArrayList<>(bookings);
            sortedBookings.sort((b1, b2) -> b2.getCreatedAt().compareTo(b1.getCreatedAt()));
            for (Booking booking : sortedBookings) {
                System.out.println("\nBooking ID: " + booking.getId());
                System.out.println("  User ID: " + booking.getUser().getId() +
                        " | Balance at Booking: " + booking.getUser().getBalance());
                System.out.println("  Room Number: " + booking.getRoom().getId() +
                        " | Type at Booking: " + booking.getRoom().getType() +
                        " | Price/Night at Booking: " + booking.getRoom().getPrice());
                System.out.println("  Check-in: " + DATE_FORMAT.format(booking.getCheckIn()) +
                        " | Check-out: " + DATE_FORMAT.format(booking.getCheckOut()));
                System.out.println("  Nights: " + calculateNights(booking.getCheckIn(), booking.getCheckOut())+
                        " | Total Cost: " + booking.getTotalCost());
            }
        }

        System.out.println("\n" + "=".repeat(80) + "\n");
    }


    public void printAllUsers() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                               ALL USERS");
        System.out.println("=".repeat(80));

        if (users.isEmpty()) {
            System.out.println("No users available.");
        } else {
            System.out.println("\n👥 USERS (Newest to Oldest):");
            System.out.println("-".repeat(80));
            // Print in reverse order (newest first)
            for (int i = users.size() - 1; i >= 0; i--) {
                User user = users.get(i);
                System.out.println("User ID: " + user.getId() + " | Balance: " + user.getBalance());
            }
        }

        System.out.println("\n" + "=".repeat(80) + "\n");
    }



    private Room findRoomById(int id) {
        for (Room room : rooms) {
            if (room.getId() == id) {
                return room;
            }
        }
        return null;
    }


    private User findUserById(int userId) {
        for (User user : users) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return null;
    }


    private void validateDates(Date checkIn, Date checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new InvalidDateException("Check-in and check-out dates cannot be null");
        }

        // Normalize dates to ignore time component
        Date normalizedCheckIn = normalizeDate(checkIn);
        Date normalizedCheckOut = normalizeDate(checkOut);

        if (!normalizedCheckOut.after(normalizedCheckIn)) {
            throw new InvalidDateException("Check-out date must be after check-in date. " +
                    "Check-in: " + DATE_FORMAT.format(checkIn) + ", Check-out: " + DATE_FORMAT.format(checkOut));
        }
    }

    private Date normalizeDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getTime();
    }


    private int calculateNights(Date checkIn, Date checkOut) {
        Date normalizedCheckIn = normalizeDate(checkIn);
        Date normalizedCheckOut = normalizeDate(checkOut);

        long diffInMillis = normalizedCheckOut.getTime() - normalizedCheckIn.getTime();
        return (int) (diffInMillis / (1000 * 60 * 60 * 24));
    }

    private boolean isRoomAvailable(int roomId, Date checkIn, Date checkOut) {
        Date normalizedCheckIn = normalizeDate(checkIn);
        Date normalizedCheckOut = normalizeDate(checkOut);

        for (Booking booking : bookings) {
            if (booking.getRoom().getId() == roomId) {
                Date bookingCheckIn = normalizeDate(booking.getCheckIn());
                Date bookingCheckOut = normalizeDate(booking.getCheckOut());

                if (normalizedCheckIn.before(bookingCheckOut) && normalizedCheckOut.after(bookingCheckIn)) {
                    return false;
                }
            }
        }

        return true;
    }
}

