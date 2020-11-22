package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class Mute extends ListenerAdapter {
    public boolean RoleFind (Member mentionedMember) {
        List<Role> roles = mentionedMember.getRoles();
        if (roles.stream().filter(role -> role.getName().equals("Muted")).findFirst().orElse(null) != null) {
            return true;
        } else {
            return false;
        }
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        //setup
        Message message = e.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = e.getChannel();
        String[] spliced = content.split("\\s+");
        int length = spliced.length;

        //informative command
        if (spliced[0].equalsIgnoreCase("-mute") && spliced.length == 1) {
            if (!(e.getMember().hasPermission(Permission.MANAGE_ROLES))) {
                channel.sendMessage("You failed to account for the fact you do not have permission to perform this command.").queue();
                return;
            }

            channel.sendMessage("Please tell me who you want to mute. Type in the format: -mute @exampleuser [time](m/d/w) [Reason (optional)]").queue();
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("MUTE COMMAND");
            builder.addField("Usage", "To use this command, type in the following format: -mute @exampleuser [time][Reason]", false);
            builder.setDescription("If you choose not to add a time, the user will be muted permanently. Putting a reason is completely optional" +
                    "\n Example 1: -mute @baduser 3d being inappropriate");
            channel.sendMessage(builder.build()).queue();
            builder.clear();


            return;
        }

        //permanent mute
        if (spliced[0].equalsIgnoreCase("-mute") && spliced.length == 2) {
            if (!(e.getMember().hasPermission(Permission.MANAGE_ROLES))) {
                channel.sendMessage("You failed to account for the fact you do not have permission to perform this command.").queue();
                return;
            }
                List<Member> mentionedMembers = message.getMentionedMembers();
                if (mentionedMembers.size() == 0) {
                    channel.sendMessage("You did not mention anyone to mute").queue();
                    return;
                } else {
                    for (Member mentionedMember : mentionedMembers) {
                        if (mentionedMember.hasPermission(Permission.MANAGE_ROLES)) {
                            channel.sendMessage(
                                    "This person is also able to change roles, I can't do that! If you have administrator, please try hard mute!")
                                    .queue();
                            return;
                        }
                    }

                    //mute user
                }
        }


        //timed mute
        if (spliced[0].equalsIgnoreCase("-mute") && spliced.length == 3){
            if (!(e.getMember().hasPermission(Permission.MANAGE_ROLES))) {
                channel.sendMessage("You failed to account for the fact you do not have permission to perform this command.").queue();
                return;
            }

            List<Member> mentionedMembers = message.getMentionedMembers();
            if (mentionedMembers.size() == 0) {
                channel.sendMessage("You did not mention anyone to mute").queue();
                return;
            } else {
                for (Member mentionedMember : mentionedMembers) {
                    if (mentionedMember.hasPermission(Permission.MANAGE_ROLES)) {
                        channel.sendMessage(
                                "This person is also able to change roles, I can't do that! If you have administrator, please try hard mute!")
                                .queue();
                        return;
                    }
                }

                //mute user
            }

        }
    }
}
