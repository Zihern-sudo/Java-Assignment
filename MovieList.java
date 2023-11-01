/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cinematicket2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.InputMismatchException;

public class MovieList {

    public static void browseMovieList() {
        System.out.println("Current Available Movies:");
        String filename = "movies.txt";
        Map<String, MovieInfo> movies = readMovieData(filename);

        if (movies.isEmpty()) {
            System.out.println("No movies available.");
            return;
        }

        // Initialize a flag to control the loop
        boolean continueBrowsing = true;

        while (continueBrowsing) {
            displayMovieList(movies);
            System.out.println();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the number of the movie you want to know more about (0 to return to menu): ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                if (choice == 0) {
                    continueBrowsing = false;
                } else if (choice >= 1 && choice <= movies.size()) {
                    String selectedMovie = findMovieByIndex(movies, choice);
                    if (selectedMovie != null) {
                        displayMovieDetails(movies.get(selectedMovie));

                        // Ask the user if they want to check the info for the next movie
                        boolean validResponse = false;
                        while (!validResponse) {
                            System.out.print("Do you want to check the info for the next movie? (Y/N): ");
                            String response = scanner.nextLine();
                            if (response.equalsIgnoreCase("Y")) {
                                validResponse = true;
                            } else if (response.equalsIgnoreCase("N")) {
                                continueBrowsing = false;
                                validResponse = true;
                            } else {
                                System.out.println("Invalid input. Please enter 'Y' or 'N'.");
                            }
                        }
                    } else {
                        System.out.println("Invalid choice. Please select a valid movie.");
                    }
                } else {
                    System.out.println("Invalid choice. Please select a valid movie or enter 0 to return to the menu.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number or 0 to return to the menu.");
                scanner.nextLine(); // Consume the invalid input
            }
        }
    }

    public static Map<String, MovieInfo> readMovieData(String filename) {
        Map<String, MovieInfo> movies = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            String movieName = null;
            String category = null;
            String description = null; // Initialize description
            String directedBy = null; // Initialize directedBy
            String hall = null;
            int duration = 0;
            StringBuilder timings = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Movie Name: ")) {
                    // If a new movie entry starts, create a MovieInfo object
                    if (movieName != null) {
                        MovieInfo movieInfo = new MovieInfo(movieName, category, description, directedBy, hall, duration, timings.toString());
                        movies.put(movieName, movieInfo);

                        // Reset values for the next movie
                        movieName = null;
                        category = null;
                        description = null;
                        directedBy = null;
                        hall = null;
                        duration = 0;
                        timings.setLength(0);
                    }

                    // Extract movie name
                    movieName = line.substring("Movie Name: ".length());
                } else if (line.startsWith("Category: ")) {
                    // Extract category
                    category = line.substring("Category: ".length());
                } else if (line.startsWith("Description: ")) {
                    // Extract description
                    description = line.substring("Description: ".length());
                } else if (line.startsWith("Directed by: ")) {
                    // Extract director information
                    directedBy = line.substring("Directed by: ".length());
                } else if (line.startsWith("Hall: ")) {
                    // Extract hall
                    hall = line.substring("Hall: ".length());
                } else if (line.startsWith("Duration: ")) {
                    // Extract duration
                    String durationStr = line.substring("Duration: ".length());
                    duration = Integer.parseInt(durationStr.split(" ")[0]);
                } else if (line.startsWith("- ")) {
                    // Extract timings
                    timings.append(line.substring(2)).append(",");
                }
            }

            // Add the last movie entry
            if (movieName != null) {
                MovieInfo movieInfo = new MovieInfo(movieName, category, description, directedBy, hall, duration, timings.toString());
                movies.put(movieName, movieInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movies;
    }

    public static void displayMovieList(Map<String, MovieInfo> movies) {
        int index = 1;
        for (String movie : movies.keySet()) {
            System.out.println(index + ". " + movie);
            index++;
        }
    }

    public static String findMovieByIndex(Map<String, MovieInfo> movies, int index) {
        int i = 1;
        for (String movie : movies.keySet()) {
            if (i == index) {
                return movie;
            }
            i++;
        }
        return null;
    }

    public static void displayMovieDetails(MovieInfo movieInfo) {
        System.out.println("Movie: " + movieInfo.getMovieName());
        System.out.println("Category: " + movieInfo.getCategory());
        System.out.println("Description: " + movieInfo.getDescription());
        System.out.println("Directed by: " + movieInfo.getDirectedBy());
        System.out.println("Hall: " + movieInfo.getHall());
        System.out.println("Duration: " + movieInfo.getDuration() + " mins");

        // Display all time slots for the movie
        System.out.println("Available Time Slots:");
        String[] timeSlots = movieInfo.getTimeSlots().split(",");
        for (int i = 0; i < timeSlots.length; i++) {
            System.out.println((i + 1) + ". " + timeSlots[i].trim());
        }

        // Extra newline for spacing
        System.out.println();
    }

}

class MovieInfo {

    private String movieName;
    private String category;
    private String description; // New field for movie description
    private String directedBy; // New field for director information
    private String hall;
    private int duration;
    private String timings;

    public MovieInfo(String movieName, String category, String description, String directedBy, String hall, int duration, String timings) {
        this.movieName = movieName;
        this.category = category;
        this.description = description;
        this.directedBy = directedBy;
        this.hall = hall;
        this.duration = duration;
        this.timings = timings;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getDescription() {
        return description;
    }

    public String getDirectedBy() {
        return directedBy;
    }

    public String getCategory() {
        return category;
    }

    public String getHall() {
        return hall;
    }

    public int getDuration() {
        return duration;
    }

    public String getTimeSlots() {
        return timings;
    }

}
