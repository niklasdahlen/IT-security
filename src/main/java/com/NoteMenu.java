package com;

import java.sql.SQLException;
import java.util.Optional;

public class NoteMenu {
    private final ConsoleUi io;
    private final NoteDao noteDao;
    private final UserDao userDao;

    public NoteMenu(ConsoleUi io, NoteDao noteDao, UserDao userDao) {
        this.io = io;
        this.noteDao = noteDao;
        this.userDao = userDao;
    }

    public void create(User current) throws SQLException {
        String title = io.ask("Titel: ");
        String content = io.ask("Innehåll: ");
        if (title.isBlank() || content.isBlank()) {
            io.error("Titel och innehåll krävs.");
            return;
        }
        noteDao.create(current.getId(), title, content);
        io.ok("Sparad.");
    }

    public void listOwn(User current) throws SQLException {
        io.printNotes(noteDao.findByUser(current.getId()));
    }

    public void listAll() throws SQLException {
        io.printNotes(noteDao.findAll());
    }

    public void update(User current) throws SQLException {
        Integer id = io.askInt("Id på noten: ");
        if (id == null) return;
        Optional<Note> note = noteDao.findById(id);
        if (note.isEmpty()) { io.error("Hittar inte noten."); return; }
        if (!canModify(current, note.get())) {
            io.error("Du får bara ändra dina notes.");
            return;
        }
        String title = io.ask("Ny titel: ");
        String content = io.ask("Nytt innehåll: ");
        if (title.isBlank() || content.isBlank()) {
            io.error("Titel och innehåll krävs.");
            return;
        }
        noteDao.update(id, title, content);
        io.ok("Uppdaterad.");
    }

    public void delete(User current) throws SQLException {
        Integer id = io.askInt("Id på noten: ");
        if (id == null) return;
        Optional<Note> note = noteDao.findById(id);
        if (note.isEmpty()) { io.error("Hittar inte noten."); return; }
        if (!canModify(current, note.get())) {
            io.error("Ingen behörighet.");
            return;
        }
        noteDao.delete(id);
        io.ok("Raderad.");
    }

    public void changePassword(User current) throws SQLException {
        String oldPw = io.ask("Nuvarande lösenord: ");
        if (userDao.login(current.getUsername(), oldPw).isEmpty()) {
            io.error("Fel nuvarande lösenord.");
            return;
        }
        String newPw = io.ask("Nytt lösenord: ");
        if (newPw.length() < 6) {
            io.error("Nytt lösenord måste vara minst 6 tecken.");
            return;
        }
        userDao.changePassword(current.getId(), newPw);
        io.ok("Lösenord uppdaterat.");
    }

    private boolean canModify(User current, Note note) {
        return current.isAdmin() || note.getUserId() == current.getId();
    }
}