import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    static int MAX_HISTORY_SIZE = 100;
    static String[] bookingHistory = new String[MAX_HISTORY_SIZE];
    static int historyIndex = 0;

    static void showHall(int rows, int columns, boolean[][] seatStatus, String time) {
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                char columnName = (char) ('A' + i - 1);
                int showIndex = getShowIndex(time);
                if (seatStatus[i][j]) {
                    System.out.print("|" + columnName + "-" + j + "::BO| ");
                } else {
                    System.out.print("|" + columnName + "-" + j + "::AV| ");
                }
            }
            System.out.println();
        }
    }

    static int getShowIndex(String time) {
        if (time.equalsIgnoreCase("A")) {
            return 0; // Morning show
        } else if (time.equalsIgnoreCase("B")) {
            return 1; // Afternoon show
        } else if (time.equalsIgnoreCase("C")) {
            return 2; // Night show
        } else {
            return -1; // Invalid show time
        }
    }

    static void booking(String booking, String seat, boolean[][] seatStatus, String time, int id) {
        int showIndex = getShowIndex(time);

        if (showIndex == -1) {
            System.out.println("Invalid show time.");
            return;
        }

        String[] seats = seat.split(",");
        for (String s : seats) {
            String[] parts = s.split("-");
            int row = parts[0].charAt(0) - 'A' + 1;
            int column = Integer.parseInt(parts[1]);

            // Check if the seat is already booked
            if (seatStatus[row][column]) {
                System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                System.out.println("# Error: Seat " + s + " is already booked. Please choose another seat.");
                System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                return;
            }

            seatStatus[row][column] = booking.equalsIgnoreCase("Y");
        }

        if (booking.equalsIgnoreCase("Y")) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");
            String datetime = now.format(formatter);

            String historyEntry = time + " | CSTAD Hall | " + seat + " | Student ID: " + id + " | " + datetime;
            bookingHistory[historyIndex++] = historyEntry;

            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
            System.out.println("# " + seat + " Booking successfully.");
            System.out.println("# Booking Details:");
            System.out.println("# Show: " + time);
            System.out.println("# Hall: CSTAD Hall");
            System.out.println("# Seat: " + seat);
            System.out.println("# Student ID: " + id);
            System.out.println("# Datetime: " + datetime);
            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        } else {
            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
            System.out.println("# Booking is false.");
            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        }
    }


    static void rebootShowtime(boolean[][] seatStatus, int rows, int columns) {
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                seatStatus[i][j] = false; // Set all seats to Available
            }
        }

        // Clear the booking history
        historyIndex = 0;

        System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        System.out.println("# Showtime rebooted. All seats are now available.");
        System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
    }


    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        System.out.println("CADT HALL BOOKING SYSTEM");
        System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");

        System.out.print("Config total rows in hall: ");
        int rows = input.nextInt();
        System.out.print("Config total columns in hall: ");
        int columns = input.nextInt();
        input.nextLine();

        boolean[][] seatStatus = new boolean[rows + 1][columns + 1]; // [rows][columns] for seats

        String option = "";

        do {
            System.out.println("[[ Application Menu ]]");
            System.out.println("<A> Booking");
            System.out.println("<B> Hall");
            System.out.println("<C> Showtime");
            System.out.println("<D> Reboot Showtime");
            System.out.println("<E> History");
            System.out.println("<F> Exit");

            System.out.print("Please select menu no: ");
            option = input.nextLine().toUpperCase();

            switch (option) {
                case "A": {
                    System.out.print("Please select show time (A | B | C): ");
                    String timeInput = input.nextLine().toUpperCase();
                    String time = (timeInput.equals("A") || timeInput.equals("B") || timeInput.equals("C")) ? timeInput : "A"; // Default to "A" if input is invalid

                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    System.out.println("# Hall " + time);
                    showHall(rows, columns, seatStatus, time);
                    System.out.println("# INSTRUCTION");
                    System.out.println("# Single: C-1");
                    System.out.println("# Multiple (separate by comma): C-1,C-2");
                    System.out.print("Please select available seat: ");
                    String seat = input.nextLine().toUpperCase();
                    System.out.print("Please enter student ID: ");
                    int id = input.nextInt();
                    input.nextLine();
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");

                    System.out.print("Are you sure to book? (Y/n): ");
                    String booking = input.nextLine().toUpperCase();
                    booking(booking, seat, seatStatus, time, id);
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                }
                break;
                case "B": {
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    System.out.println("# Hall - Morning");
                    showHall(rows, columns, seatStatus, "A");
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    System.out.println("# Hall - Afternoon");
                    showHall(rows, columns, seatStatus, "B");
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    System.out.println("# Hall - Night");
                    showHall(rows, columns, seatStatus, "C");
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                }
                break;
                case "C": {
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    System.out.println("# Daily Showtime of CSTAD Hall:");
                    System.out.println("# A) Morning (10:10AM - 12:30PM)");
                    System.out.println("# B) Afternoon (03:00PM - 05:30PM)");
                    System.out.println("# C) Night (07:00PM - 09:30PM)");
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                }
                break;
                case "D": {
                    rebootShowtime(seatStatus, rows, columns);
                }
                break;

                case "E": {
                    showHistory();
                }
                break;
            }
        } while (!option.equals("F"));

        input.close();
    }

    static void showHistory() {
        System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        System.out.println("# Booking History:");
        for (int i = 0; i < historyIndex; i++) {
            System.out.println("# " + bookingHistory[i]);
            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        }
    }
}
