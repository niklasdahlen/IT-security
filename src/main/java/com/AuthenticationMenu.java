package com;

import java.sql.SQLException;
import java.util.Optional;

public class AuthenticationMenu {
    private final ConsoleUi io;
    private final UserDao userDao;

    public AuthenticationMenu(ConsoleUi io, UserDao userDao) {
        this.io = io;
        this.userDao = userDao;
    }

    public User register() throws SQLException {
        String username = io.ask("Användarnamn: ");
        String password = io.ask("Lösenord: ");
        if (username.isBlank() || password.length() < 6) {
            io.error("Användarnamn krävs och lösenord måste vara minst 6 tecken.");
            return null;
        }
        if (userDao.usernameExists(username)) {
            io.error("Användarnamnet är upptaget. Försök igen.");
            return null;
        }
        userDao.register(username, password);
        io.ok("Konto skapat. Nu kan du logga in.");
        return null;
    }

    public User login() throws SQLException {
        String username = io.ask("Användarnamn: ");
        String password = io.ask("Lösenord: ");
        Optional<User> user = userDao.login(username, password);
        if (user.isEmpty()) {
            io.error("Fel användarnamn eller lösenord.");
            return null;
        }
        io.ok("Välkommen, " + user.get().getUsername());
        return user.get();
    }
}