package events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelloEvent extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String messageSent = event.getMessage().getContentRaw();
        if (messageSent.equalsIgnoreCase("-hello")){
            event.getChannel().sendMessage("Hey there! What can I get started for you?").queue();
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
