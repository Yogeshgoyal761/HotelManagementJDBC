package org.example;
import java.sql.*;
import java.util.*;

public class HotelManagementJDBC {
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hotel_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Yogesh@8209";

    public static void main(String[] args) {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Initialize database (create tables if they don't exist)
            initializeDatabase();

            Hotel hotel = new Hotel();
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n=== Hotel Management System ===");
                System.out.println("1. Check-in Guest");
                System.out.println("2. Check-out Guest");
                System.out.println("3. View Available Rooms");
                System.out.println("4. View All Rooms");
                System.out.println("5. Add New Room");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        hotel.checkIn();
                        break;
                    case 2:
                        hotel.checkOut();
                        break;
                    case 3:
                        hotel.displayAvailableRooms();
                        break;
                    case 4:
                        hotel.displayAllRooms();
                        break;
                    case 5:
                        hotel.addNewRoom();
                        break;
                    case 6:
                        System.out.println("Exiting system...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }

    private static void initializeDatabase() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Create rooms table with additional fields
            String createRoomsTable = "CREATE TABLE IF NOT EXISTS rooms (" +
                    "room_number INT PRIMARY KEY, " +
                    "room_type VARCHAR(50) NOT NULL DEFAULT 'Standard', " +
                    "price_per_night DECIMAL(10,2) NOT NULL DEFAULT 100.00, " +
                    "max_occupancy INT NOT NULL DEFAULT 2, " +
                    "amenities TEXT, " +
                    "guest_name VARCHAR(100), " +
                    "occupied BOOLEAN DEFAULT false)";
            stmt.execute(createRoomsTable);

            // Check if we need to insert default rooms
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM rooms");
            rs.next();
            if (rs.getInt(1) == 0) {
                // Insert default rooms
                String insertRooms = "INSERT INTO rooms (room_number, room_type, price_per_night, max_occupancy, amenities) VALUES " +
                        "(101, 'Standard', 99.99, 2, 'TV, WiFi, AC'), " +
                        "(102, 'Standard', 99.99, 2, 'TV, WiFi, AC'), " +
                        "(103, 'Standard', 99.99, 2, 'TV, WiFi, AC'), " +
                        "(201, 'Deluxe', 149.99, 3, 'TV, WiFi, AC, Mini-bar'), " +
                        "(202, 'Deluxe', 149.99, 3, 'TV, WiFi, AC, Mini-bar'), " +
                        "(301, 'Suite', 249.99, 4, 'TV, WiFi, AC, Mini-bar, Jacuzzi')";
                stmt.executeUpdate(insertRooms);
            }
        }
    }

    static class Hotel {
        public void displayAvailableRooms() {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                         "SELECT room_number, room_type, price_per_night FROM rooms WHERE occupied = false")) {

                System.out.println("\nAvailable Rooms:");
                System.out.println("--------------------------------------------------");
                System.out.printf("%-10s %-15s %-15s\n", "Room No.", "Type", "Price/Night");
                System.out.println("--------------------------------------------------");

                while (rs.next()) {
                    System.out.printf("%-10d %-15s $%-14.2f\n",
                            rs.getInt("room_number"),
                            rs.getString("room_type"),
                            rs.getDouble("price_per_night"));
                }
                System.out.println("--------------------------------------------------");
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            }
        }

        public void displayAllRooms() {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                         "SELECT * FROM rooms ORDER BY room_number")) {

                System.out.println("\nAll Rooms:");
                System.out.println("------------------------------------------------------------------------------------------------");
                System.out.printf("%-10s %-15s %-15s %-10s %-10s %-30s\n",
                        "Room No.", "Type", "Price/Night", "Occupancy", "Status", "Amenities");
                System.out.println("------------------------------------------------------------------------------------------------");

                while (rs.next()) {
                    String status = rs.getBoolean("occupied") ?
                            "Occupied (" + rs.getString("guest_name") + ")" : "Available";
                    System.out.printf("%-10d %-15s $%-14.2f %-10d %-10s %-30s\n",
                            rs.getInt("room_number"),
                            rs.getString("room_type"),
                            rs.getDouble("price_per_night"),
                            rs.getInt("max_occupancy"),
                            status,
                            rs.getString("amenities"));
                }
                System.out.println("------------------------------------------------------------------------------------------------");
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            }
        }

        public void checkIn() {
            Scanner scanner = new Scanner(System.in);

            // First show available rooms
            displayAvailableRooms();

            System.out.print("\nEnter room number: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // Check if room exists and is available
                PreparedStatement checkStmt = conn.prepareStatement(
                        "SELECT room_type, price_per_night FROM rooms WHERE room_number = ? AND occupied = false");
                checkStmt.setInt(1, roomNumber);
                ResultSet rs = checkStmt.executeQuery();

                if (!rs.next()) {
                    System.out.println("Error: Room " + roomNumber + " is not available or doesn't exist!");
                    return;
                }

                String roomType = rs.getString("room_type");
                double price = rs.getDouble("price_per_night");

                System.out.printf("Confirm booking %s room %d for $%.2f per night? (yes/no): ",
                        roomType, roomNumber, price);
                String confirmation = scanner.nextLine();

                if (!confirmation.equalsIgnoreCase("yes")) {
                    System.out.println("Booking cancelled.");
                    return;
                }

                // Perform check-in
                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE rooms SET guest_name = ?, occupied = true WHERE room_number = ?");
                updateStmt.setString(1, guestName);
                updateStmt.setInt(2, roomNumber);

                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Success: " + guestName + " checked into Room " + roomNumber);
                } else {
                    System.out.println("Error: Check-in failed!");
                }
            } catch (SQLException e) {
                System.err.println("Database error during check-in: " + e.getMessage());
            }
        }

        public void checkOut() {
            Scanner scanner = new Scanner(System.in);

            // Show occupied rooms
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                         "SELECT room_number, guest_name FROM rooms WHERE occupied = true")) {

                System.out.println("\nOccupied Rooms:");
                System.out.println("---------------------");
                while (rs.next()) {
                    System.out.println("Room " + rs.getInt("room_number") +
                            " - " + rs.getString("guest_name"));
                }
                System.out.println("---------------------");
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                return;
            }

            System.out.print("\nEnter room number to check-out: ");
            int roomNumber = scanner.nextInt();

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // Check if room exists and is occupied
                PreparedStatement checkStmt = conn.prepareStatement(
                        "SELECT guest_name, room_type, price_per_night FROM rooms WHERE room_number = ? AND occupied = true");
                checkStmt.setInt(1, roomNumber);
                ResultSet rs = checkStmt.executeQuery();

                if (!rs.next()) {
                    System.out.println("Error: Room " + roomNumber + " is not occupied or doesn't exist!");
                    return;
                }

                String guestName = rs.getString("guest_name");
                String roomType = rs.getString("room_type");
                double price = rs.getDouble("price_per_night");

                System.out.print("Enter number of nights stayed: ");
                int nights = scanner.nextInt();
                double total = nights * price;

                System.out.printf("Checking out %s from %s room %d. Total charge: $%.2f. Confirm? (yes/no): ",
                        guestName, roomType, roomNumber, total);
                scanner.nextLine(); // Consume newline
                String confirmation = scanner.nextLine();

                if (!confirmation.equalsIgnoreCase("yes")) {
                    System.out.println("Check-out cancelled.");
                    return;
                }

                // Perform check-out
                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE rooms SET guest_name = NULL, occupied = false WHERE room_number = ?");
                updateStmt.setInt(1, roomNumber);

                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.printf("Success: %s checked out from Room %d. Total charge: $%.2f\n",
                            guestName, roomNumber, total);
                } else {
                    System.out.println("Error: Check-out failed!");
                }
            } catch (SQLException e) {
                System.err.println("Database error during check-out: " + e.getMessage());
            }
        }

        public void addNewRoom() {
            Scanner scanner = new Scanner(System.in);

            System.out.println("\n=== Add New Room ===");
            System.out.print("Enter room number: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter room type (Standard/Deluxe/Suite): ");
            String roomType = scanner.nextLine();

            System.out.print("Enter price per night: ");
            double price = scanner.nextDouble();

            System.out.print("Enter maximum occupancy: ");
            int maxOccupancy = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter amenities (comma separated): ");
            String amenities = scanner.nextLine();

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO rooms (room_number, room_type, price_per_night, max_occupancy, amenities) " +
                                "VALUES (?, ?, ?, ?, ?)");

                stmt.setInt(1, roomNumber);
                stmt.setString(2, roomType);
                stmt.setDouble(3, price);
                stmt.setInt(4, maxOccupancy);
                stmt.setString(5, amenities);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Success: Room " + roomNumber + " added to the hotel!");
                } else {
                    System.out.println("Error: Failed to add room.");
                }
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) { // Duplicate entry
                    System.out.println("Error: Room " + roomNumber + " already exists!");
                } else {
                    System.err.println("Database error: " + e.getMessage());
                }
            }
        }
    }
}