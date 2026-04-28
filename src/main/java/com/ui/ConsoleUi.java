package com.ui;

import java.util.List;
import java.util.Scanner;

import com.datamodell.Note;

public class ConsoleUi {
    
        private final Scanner in = new Scanner(System.in);

    public String ask(String label) {
        System.out.print(label);
        return in.nextLine().trim();
    }

    public Integer askInt(String label) {
        try { return Integer.parseInt(ask(label)); }
        catch (NumberFormatException e) {
            System.out.println("Ogiltigt nummer.");
            return null;
        }
    }

    public void info(String msg)  { System.out.println(msg); }
    public void ok(String msg)    { System.out.println("✔ " + msg); }
    public void error(String msg) { System.out.println("✖ " + msg); }

    public void printNotes(List<Note> notes) {
        if (notes.isEmpty()) { info("(inga notes)"); return; }
        for (Note n : notes) {
            System.out.printf("#%d [%s] %s%n    %s%n",
                    n.getId(), n.getUsername(), n.getTitle(), n.getContent());
        }
    }
}
