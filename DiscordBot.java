package com.mycompany.discordbot;


import com.mycompany.discordbot.Events.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.WireFeedInput;

import java.net.URL;
import java.util.List;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class DiscordBot {

    public static void main(String[] args) {
        String Token = "MTExMTIwNDIxMDY4MDI3OTA5MA.GNy-22.bItDIn8nIvJol-amnESwrxG5zZ_72KepQw6A-A";
        JDABuilder jdaBuilder = JDABuilder.createDefault(Token);
        JDA bot = jdaBuilder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new ReadyEventL(), new MessageEventL(), new AnswerSlashEventL())
                .build();

        bot.upsertCommand("orar", "Functie pentru afisarea orarului").setGuildOnly(true).queue();
        bot.upsertCommand("afisaremesaje", "Functie pentru afisarea mesajelor preluate prin RSS").setGuildOnly(true).queue();
        bot.upsertCommand("structure", "Functie pentru setarea semestrului").setGuildOnly(true).addOption(OptionType.STRING, "datastart", "data start", true).addOption(OptionType.STRING, "datafinal", "data final", true).queue();
        bot.upsertCommand("adaugaelev", "Functie pentru adaugarea unui elev").setGuildOnly(true).addOption(OptionType.STRING, "nume", "Numele studentului", true).queue();
        bot.upsertCommand("setpref", "Functie pentru alegerea unei materii preferate").setGuildOnly(true).addOption(OptionType.STRING, "nume", "Numele studentului", true).addOption(OptionType.STRING, "materie", "Numele materiei", true).queue();
        
        
    }
    
    
}
