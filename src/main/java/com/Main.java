package com;

import java.sql.SQLException;

public class Main {
    private final ConsoleUi io = new ConsoleUi();
    private final UserDao userDao = new UserDao();
    private final NoteDao noteDao = new NoteDao();
    private final AuthenticationMenu auth = new AuthenticationMenu(io, userDao);
    private final NoteMenu notes = new NoteMenu(io, noteDao, userDao);
    private User current = null;

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        io.info("Notes-app");
        while (true) {
            try {
                if (current == null) startMenu();
                else if (current.isAdmin()) adminMenu();
                else userMenu();
            } catch (SQLException e) {
                io.error("Databasfel: " + e.getMessage());
            }
        }
    }

    private void startMenu() throws SQLException {
        io.info("\n1) Logga in\n2) Registrera\n0) Avsluta");
        switch (io.ask("Val: ")) {
            case "1" -> current = auth.login();
            case "2" -> auth.register();
            case "0" -> { io.info("Hej då!"); System.exit(0); }
            default  -> io.error("Ogiltigt val");
        }
    }

    private void userMenu() throws SQLException {
        io.info("\n-- " + current.getUsername() + " (USER) --");
        io.info("1) Skapa note  2) Mina notes  3) Ändra  4) Radera  5) Byt lösen  9) Logga ut");
        handleCommon(io.ask("Val: "));
    }

    private void adminMenu() throws SQLException {
        io.info("\n== " + current.getUsername() + " (ADMIN) ==");
        io.info("1) Skapa  2) Mina  3) Ändra  4) Radera  5) Byt lösen  6) Alla notes  9) Logga ut");
        String val = io.ask("Val: ");
        if (val.equals("6")) notes.listAll();
        else handleCommon(val);
    }

    private void handleCommon(String val) throws SQLException {
        switch (val) {
            case "1" -> notes.create(current);
            case "2" -> notes.listOwn(current);
            case "3" -> notes.update(current);
            case "4" -> notes.delete(current);
            case "5" -> notes.changePassword(current);
            case "9" -> { current = null; io.info("Utloggad."); }
            default  -> io.error("Ogiltigt val");
        }
    }
}