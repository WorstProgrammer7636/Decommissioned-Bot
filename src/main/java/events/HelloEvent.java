package events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;

public class HelloEvent extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        if (event.getAuthor().isBot()){
            return;
        }
        String messageSent = event.getMessage().getContentRaw();
        if (messageSent.equalsIgnoreCase("-hello")){
            EmbedBuilder menu = new EmbedBuilder();
            event.getChannel().sendMessage("Hey there! What can I get started for you?\n").queue();
            menu.setTitle("MENU: \n");
            menu.addField("Currently we have:", "Coffee\n Tea \n Bagel \n Chocolate", false);
            event.getChannel().sendMessage(menu.build()).queue();


            //figure out how to input messages from user after bot sends message (practically allow a short or full conversation between bot and user)
            String message = event.getMessage().getContentRaw();


            if (message.length() > 1){
                event.getChannel().sendMessage("I'm sorry, it seems like what you ordered is not on our menu :frowning:").queue();
            } else if (message.length() == 1 && message.equalsIgnoreCase("Coffee")){
                event.getChannel().sendMessage(":coffee:").queue();
            }

        }


        if (messageSent.equalsIgnoreCase("-info")){
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("INFORMATION");
            info.addField("Creators", "hold up#7636 & Nard#8888", false);
            info.setColor(0xf45642);
            info.setDescription("Welcome and thanks for using Inutile! If you would like to view commands, type -commands");
            info.setFooter("Created by hold up and 0Nard", event.getMember().getUser().getAvatarUrl());
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(info.build()).queue();
            info.clear();
        }

        if (messageSent.equalsIgnoreCase("-commands")){
            EmbedBuilder justTitle = new EmbedBuilder();
            //update as more functions are added
            justTitle.setTitle("LIST OF COMMANDS");
            event.getChannel().sendMessage(justTitle.build()).queue();
            event.getChannel().sendMessage("```\n -calculate \n -piglatin \n -translate```").queue();

            justTitle.setTitle("ADMINISTRATOR COMMANDS");
            event.getChannel().sendMessage(justTitle.build()).queue();
            event.getChannel().sendMessage("```\n -mute \n -unmute```").queue();

        }
    }

}
