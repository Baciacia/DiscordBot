/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.discordbot.Events;

import java.security.SecureRandom;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.net.URL;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

/**
 *
 * @author Eduard
 */
public class AnswerSlashEventL extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        String commandName = event.getName();

        switch (commandName) {
            case "orar" :
                handleOrarCommand(event);
            case"structure" :
                handleStructuraAnScolarCommand(event);
            case"adaugaelev" :
                handleAdaugaElevCommand(event);
            case"setpref" :
                handleSetPrefCommand(event);
            case "afisaremesaje" :
                handleAfisareMesajeCommand(event);

            default :
                event.reply("Comanda necunoscută.").setEphemeral(true).queue();
        }
    }

    private void handleOrarCommand(SlashCommandInteractionEvent event) {
        String url = "jdbc:mysql://localhost:3306/testtttt";
        String username = "root";
        String password = "";
        String startParam = event.getOption("start") != null ? event.getOption("start").getAsString() : null;
        String endParam = event.getOption("end") != null ? event.getOption("end").getAsString() : null;

        // Definim zilele lucrătoare și orele de început și final
        String[] zile = {"Luni", "Marti", "Miercuri", "Joi", "Vineri"};
        String[] oreStart = {"08:00", "09:00", "10:00", "11:00", "12:00"};
        String[] oreEnd = {"09:00", "10:00", "11:00", "12:00", "13:00"};

        // Număr maxim de ore pe zi
        int maxOrePeZi = 6;

        // Declaram un StringBuilder pentru construirea orarului
        StringBuilder orarBuilder = new StringBuilder();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Creăm declarația SQL pentru inserarea în baza de date
            String insertQuery = "INSERT INTO test2 (Zi, oraS, oraF, nume) VALUES (?, ?, ?, ?)";

            // Creăm declarația SQL pentru selectarea orarului generat
            String selectQuery = "SELECT * FROM test2";

            // Creăm declarația SQL pentru ștergerea orarului generat
            String deleteQuery = "DELETE FROM test2";

            // Stergem orarul existent din baza de date
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                deleteStatement.executeUpdate();
            }

            // Generăm orarul random
            for (String zi : zile) {
                // Generăm un număr random de ore pentru ziua curentă
                int numarOre = 5;

                // Dacă numărul de ore este 0, trecem la următoarea zi
               

                // Generăm ora de început și ora de final pentru fiecare oră
                for (int i = 0; i < numarOre; i++) {
                    // Generăm un index random pentru orele de început și final

                    // Obținem orele de început și final corespunzătoare indexului generat
                    String oraStart = oreStart[i];
                    String oraFinal = oreEnd[i];

                    // Generăm un nume random pentru materie
                    String numeMaterie = generateRandomMaterie();

                    // Salvăm informațiile în baza de date
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setString(1, zi);
                        insertStatement.setString(2, oraStart);
                        insertStatement.setString(3, oraFinal);
                        insertStatement.setString(4, numeMaterie);

                        insertStatement.executeUpdate();
                    }
                }
            }

            // Selectăm orarul generat din baza de date
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                ResultSet resultSet = selectStatement.executeQuery();

                // Construim orarul pentru afișare
                while (resultSet.next()) {
                    String zi = resultSet.getString("Zi");
                    String oraStart = resultSet.getString("oraS");
                    String oraFinal = resultSet.getString("oraF");
                    String numeMaterie = resultSet.getString("nume");

                    orarBuilder.append(zi).append(" | ").append(oraStart).append(" - ").append(oraFinal)
                            .append(" | ").append(numeMaterie).append("\n");
                }
            }

            // Afișăm orarul în Discord
            String orar = orarBuilder.toString();
            event.reply("Orarul generat este:\n```" + orar + "```").queue();

        } catch (SQLException e) {
            event.reply("A apărut o eroare la generarea și afișarea orarului.").queue();
            e.printStackTrace();
        }
    }

// Funcție pentru generarea unui nume de materie random
    private String generateRandomMaterie() {
        String[] materii = {"Matematică", "Informatică", "Fizică", "Chimie", "Biologie", "Geografie", "Istorie", "Limba Română", "Limba Engleză"};
        int randomIndex = new SecureRandom().nextInt(materii.length);
        int lungimeMaxima = 50;
        String numeMaterie = materii[randomIndex];
        if (numeMaterie.length() > lungimeMaxima) {
            numeMaterie = numeMaterie.substring(0, lungimeMaxima);
        }

        return materii[randomIndex];
    }
    
    private void handleStructuraAnScolarCommand(SlashCommandInteractionEvent event) {
       
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataInceput = LocalDate.parse(event.getOption("datastart").getAsString(), formatter);
        LocalDate dataFinal = LocalDate.parse(event.getOption("datafinal").getAsString(), formatter);


        String structuraAnScolar = generareStructuraAnScolar(dataInceput, dataFinal);

        // Salvare în baza de date
        String url = "jdbc:mysql://localhost:3306/testtttt";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "INSERT INTO structura_an_scolar (data_inceput, data_final, structura) VALUES (?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, dataInceput.toString());
                statement.setString(2, dataFinal.toString());
                statement.setString(3, structuraAnScolar);

                statement.executeUpdate();
                event.reply("Structura anului școlar:\n" + structuraAnScolar).setEphemeral(true).queue();

            }
        } catch (SQLException e) {
            e.printStackTrace();
            event.reply("A apărut o eroare la salvarea structurii anului școlar în baza de date.").setEphemeral(true).queue();
        }

        // Afișare pe Discord
        event.getGuild().getTextChannelById("canal-id").sendMessage("Structura anului școlar:\n" + structuraAnScolar).queue();
    }

    private String generareStructuraAnScolar(LocalDate dataInceput, LocalDate dataFinal) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        StringBuilder structura = new StringBuilder();

        // Adăugăm perioada de vacanță de vară
        LocalDate vacantaVaraInceput = LocalDate.of(dataInceput.getYear(), ((dataInceput.getMonthValue() + 9)%12), 16);
        LocalDate vacantaVaraFinal = LocalDate.of(dataInceput.getYear(), ((dataInceput.getMonthValue() + 12)%12), 14);
        structura.append("Vacanța de vară: ").append(vacantaVaraInceput.format(formatter)).append(" - ").append(vacantaVaraFinal.format(formatter)).append("\n");

        // Adăugăm perioada de vacanță de iarnă
        LocalDate vacantaIarnaInceput = LocalDate.of(dataInceput.getYear() + 1, 12, 20);
        LocalDate vacantaIarnaFinal = LocalDate.of(dataInceput.getYear() + 1, 1, 5);
        structura.append("Vacanța de iarnă: ").append(vacantaIarnaInceput.format(formatter)).append(" - ").append(vacantaIarnaFinal.format(formatter)).append("\n");

        // Adăugăm celelalte perioade din an
        LocalDate currentDate = dataInceput.plusDays(1);
        while (!currentDate.isAfter(dataFinal)) {
            structura.append("Semestrul ").append(currentDate.getYear() - dataInceput.getYear() + 1).append(":\n");
            structura.append("Perioada ").append(currentDate.format(formatter)).append(" - ");

            currentDate = currentDate.plusMonths(4).plusDays(14);
            structura.append(currentDate.minusDays(1).format(formatter)).append("\n");

            currentDate = currentDate.plusDays(2);
        }

        return structura.toString();
    }
    
    private void handleAdaugaElevCommand(SlashCommandInteractionEvent event) {
    String nume = event.getOption("nume").getAsString();

    // Conectare la baza de date
    String url = "jdbc:mysql://localhost:3306/testtttt";
    String username = "root";
    String password = "";

    try (Connection connection = DriverManager.getConnection(url, username, password)) {
        String query = "INSERT INTO elevi (nume, materie_pref) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nume);
            statement.setNull(2, Types.VARCHAR);

            statement.executeUpdate();
            event.reply("Elevul " + nume + " a fost adăugat în baza de date.").setEphemeral(true).queue();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        event.reply("A apărut o eroare la adăugarea elevului în baza de date.").setEphemeral(true).queue();
    }
}

    private void handleSetPrefCommand(SlashCommandInteractionEvent event) {
    String nume = event.getOption("nume").getAsString();
    String materie = event.getOption("materie").getAsString();

    // Salvare în baza de date
    String url = "jdbc:mysql://localhost:3306/testtttt";
    String username = "root";
    String password = "";

    try (Connection connection = DriverManager.getConnection(url, username, password)) {
        String query = "UPDATE elevi SET materie_pref = ? WHERE nume = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, materie);
            statement.setString(2, nume);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                event.reply("Materia preferată a fost actualizată pentru elevul " + nume).setEphemeral(true).queue();
            } else {
                event.reply("Nu s-a găsit niciun elev cu numele " + nume).setEphemeral(true).queue();
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        event.reply("A apărut o eroare la actualizarea materiei preferate în baza de date.").setEphemeral(true).queue();
    }
}

    private void handleAfisareMesajeCommand(SlashCommandInteractionEvent event) {
    try {
        URL feedUrl = new URL("https://www.edu.ro/rss.xml"); // Înlocuiește URL-ul cu fluxul RSS dorit

        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(feedUrl));

        TextChannel channel = (TextChannel) event.getChannel();
        
        List<SyndEntry> entries = feed.getEntries();

        for (SyndEntry entry : entries) {
            String title = entry.getTitle();
            String description = entry.getDescription().getValue();

            // Construiește mesajul
            String message = "Titlu: " + title + "\n\n"
                            + "Descriere: " + description+ "\n";
            
            if (message.length() > 2000) {
                // Dacă mesajul depășește 2000 de caractere, taie-l la lungimea maximă
                message = message.substring(0, 2000);
            }
            
            channel.sendMessage(message).queue();
        }

        event.reply("Mesajele din fluxul RSS au fost afișate.").setEphemeral(true).queue();
    } catch (Exception e) {
        event.reply("A apărut o eroare la afișarea mesajelor din fluxul RSS.").setEphemeral(true).queue();
        e.printStackTrace();
    }
}

}
