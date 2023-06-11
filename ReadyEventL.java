/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.discordbot.Events;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;

/**
 *
 * @author Eduard
 */
public class ReadyEventL implements EventListener{
    @Override
    public void onEvent(GenericEvent event)
    {
        if(event instanceof ReadyEvent)
        {
            System.out.println("Bot-ul este online si pregatit!");
        }
    }
}
