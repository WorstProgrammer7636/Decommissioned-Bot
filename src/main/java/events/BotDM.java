package events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;

public class BotDM extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        Message message = e.getMessage();
        String content = message.getContentRaw();
        String[] spliced = content.split("\\s+");

        Member member = e.getMember();

        if (spliced.length == 1 && spliced[0].equalsIgnoreCase("-DM")){
            member.getUser().openPrivateChannel().queue(channel -> {
                channel.sendMessage("Gday Gday! This is inutile customer service. How can I help?").queue();
            });
        }

    }
}
