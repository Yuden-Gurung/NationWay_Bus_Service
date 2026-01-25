/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 * Controller class for managing user authentication in the NationWay Bus
 * Service system. Handles login verification for both admin and regular user
 * accounts.
 *
 * Note: In a production system, credentials should be stored securely in a
 * database with proper password hashing and encryption. This implementation is
 * for educational/demonstration purposes only.
 *
 * @author lalit
 */
public class LoginController {

    // Admin credentials
    private final String adminUsername = "admin";
    private final String adminPassword = "admin123";
    private final String userUsername = "user";
    private final String userPassword = "user123";

    /**
     * Authenticates a user as an administrator. Validates the provided
     * credentials against stored admin credentials.
     *
     * @param username The username provided by the user
     * @param password The password provided by the user
     * @return true if credentials match admin account, false otherwise
     */
    public boolean loginAsAdmin(String username, String password) {

        if (username == null || password == null) {
            return false;
        }

        return username.equals(adminUsername)
                && password.equals(adminPassword);
    }

    /**
     * Authenticates a user as a regular user. Validates the provided
     * credentials against stored user credentials.
     *
     * @param username The username provided by the user
     * @param password The password provided by the user
     * @return true if credentials match user account, false otherwise
     */
    public boolean loginAsUser(String username, String password) {

        if (username == null || password == null) {
            return false;
        }

        return username.equals(userUsername)
                && password.equals(userPassword);
    }
}
