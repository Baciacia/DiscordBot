package com.mycompany.discordbot.Events;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageEventL extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);

        String message = event.getMessage().getContentDisplay().toLowerCase();

        if (message.startsWith("cate ore de")) {
            // Obține materia din întrebare
            String[] words = message.split(" ");
            String materia = words[3];

            materia = materia.trim();

            String url = "jdbc:mysql://localhost:3306/testtttt";
            String username = "root";
            String password = "";

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                // Interogare pentru a verifica numărul de ore pentru materia specificată în orar
                String query = "SELECT COUNT(*) FROM test2 WHERE nume = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, materia);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            int numarOre = resultSet.getInt(1);
                            event.getChannel().sendMessage("Ai " + numarOre + " ore de " + materia).queue();
                        } else {
                            event.getChannel().sendMessage("Nu am găsit informații despre orele de " + materia).queue();
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                event.getChannel().sendMessage("A apărut o eroare în interogarea bazei de date.").queue();
            }
        } else {
            if (message.startsWith("ce ora am ")) {
                String[] words = message.split(" ");
                String zi = words[3];
                String oraS = words[6];

                String url = "jdbc:mysql://localhost:3306/testtttt";
                String username = "root";
                String password = "";

                try (Connection connection = DriverManager.getConnection(url, username, password)) {
                    // Interogare pentru a verifica ora pentru ziua specificată
                    String query = "SELECT nume FROM test2 WHERE zi = ? AND oraS = ?";
                    try (PreparedStatement statement = connection.prepareStatement(query)) {
                        statement.setString(1, zi);
                        statement.setString(2, oraS);
                        try (ResultSet resultSet = statement.executeQuery()) {
                            if (resultSet.next()) {
                                String materia = resultSet.getString("nume");
                                event.getChannel().sendMessage("Ai ora de " + materia + " în ziua de " + zi + " la ora " + oraS).queue();
                            } else {
                                String materia = resultSet.getString("nume");
                                event.getChannel().sendMessage("Nu am găsit informații despre ora de " + materia + " în ziua de " + zi).queue();
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    event.getChannel().sendMessage("A apărut o eroare în interogarea bazei de date.").queue();
                }
            }
        }
    }

}

   
