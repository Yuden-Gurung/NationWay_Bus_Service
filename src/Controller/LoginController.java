/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author lalit
 */
public class LoginController {

    // Admin credentials
    private final String adminUsername = "admin";
    private final String adminPassword = "admin123";
    private final String userUsername = "user";
    private final String userPassword = "user123";

    public boolean loginAsAdmin(String username, String password) {

        if (username == null || password == null) {
            return false;
        }

        return username.equals(adminUsername)
                && password.equals(adminPassword);
    }
    public boolean loginAsUser(String username, String password) {

        if (username == null || password == null) {
            return false;
        }

        return username.equals(userUsername)
                && password.equals(userPassword);
    }
}
