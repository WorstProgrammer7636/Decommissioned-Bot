package commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class RickRoll extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        Member member = event.getMember();

        Message message = event.getMessage();
        String content = message.getContentRaw();
        String[] spliced = content.split("\\s+");

        if (spliced[0].equalsIgnoreCase("-rickroll") && spliced.length == 1) {
            event.getChannel().sendMessage("Who do you want to rickroll?").queue();
            event.getChannel().sendMessage("Type in this format: -rickroll [@user] \n The command works by making the bot DM the user you mentioned").queue();
        } else if (spliced[0].equalsIgnoreCase("-rickroll") && spliced.length == 2 && (spliced[1].equalsIgnoreCase("@everyone") || spliced[1].equalsIgnoreCase("@here"))) {
            event.getChannel().sendMessage("That's too brutal. I can't do that").queue();
        } else if (spliced[0].equalsIgnoreCase("-rickroll") && spliced.length == 2 && spliced[1].startsWith("<@")) {
                List<Member> mentionedMembers = message.getMentionedMembers();

                for (Member m : mentionedMembers){
                    try {
                        Member me = mentionedMembers.get(0);
                        me.getUser().openPrivateChannel().complete().sendMessage("Inutile Developer Face Reveal Video: \n <https://www.youtube.com/watch?v=NfSGm9DDQ3o>").queue();
                    } catch (UnsupportedOperationException e){
                        event.getChannel().sendMessage("You can't rickroll me bitch!").queue();
                    }
                }
        }
    }
}
