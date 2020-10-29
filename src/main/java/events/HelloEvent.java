package events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
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
            info.setColor(Color.BLUE);
            info.setDescription("Welcome and thanks for using Inutile! Below are all the commands! You can simply type those commands the bot" +
                    " will give you further instructions on how to use the commands");
            info.setFooter("Created by hold up and Nard", event.getMember().getUser().getAvatarUrl());
            //update as more functions are added
            info.addField("LIST OF COMMANDS", "```\n -info \n -calculate \n -piglatin \n -translate \n -meme \n -rickroll```", false);
            info.addField("ADMINISTRATOR COMMANDS", "```\n -mute \n -tempmute \n -hardmute \n -unmute```", false);
            info.addField("GOT ANY MORE QUESTIONS OR SUGGESTIONS?", "Join our community and help server! \n https://discord.gg/RVT8ywu \n" +
                    "\n Invite this bot to your own servers: https://discord.com/api/oauth2/authorize?client_id=765713285965807657&permissions=0&scope=bot", false);
            event.getChannel().sendMessage(info.build()).queue();
            info.clear();

        }
    }

}
