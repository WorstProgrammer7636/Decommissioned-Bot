package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class Unmute extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();
        if (content.startsWith("-unmute"))
            if (event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
                {
                    String[] spliced = content.split("\\s+");
                    TextChannel textChannel = event.getGuild().getTextChannelsByName("log", true).get(0);

                    int length = spliced.length;
                    if (length == 2) {
                        if (spliced[1].startsWith("<@")) {
                            Role target = event.getGuild().getRolesByName("Muted", false).get(0);

                            List<Member> mentionedMembers = message.getMentionedMembers();
                            for (Member mentionedMember : mentionedMembers) {

                                if (mentionedMember.hasPermission(Permission.MANAGE_ROLES)) {
                                    channel.sendMessage(
                                            "I don't think that person could've been muted in the first place.")
                                            .queue();

                                } else if (RoleFind(mentionedMember)) {
                                    channel.sendMessage("I don't think that person was muted in the first place.")
                                            .queue();
                                } else {
                                    event.getGuild().removeRoleFromMember(mentionedMember, target).queue();
                                    EmbedBuilder info = new EmbedBuilder();
                                    info.setTitle("NEW UNMUTE");
                                    info.addField("Info:", mentionedMember.getEffectiveName() + " was unmuted by "
                                            + event.getMember().getEffectiveName(), false);
                                    info.setColor(0x03a5fc);
                                    textChannel.sendMessage(info.build()).queue();
                                    channel.sendMessage("Unmute successful! :white_check_mark:").queue();

                                }
                            }

                        } else {
                            channel.sendMessage(
                                    "Can you use the command correctly PLEASE? Just do -unmute @<user>! SIMPLE!")
                                    .queue();

                        }
                    } else {
                        channel.sendMessage(
                                "Can you use the command correctly PLEASE? Just do -unmute @<user>! SIMPLE!").queue();
                    }

                }
            } else {
                channel.sendMessage("You failed to account for the fact that you can't do that.").queue();
            }
    }

    public boolean RoleFind(Member mentionedMember) {
        List<Role> roles = mentionedMember.getRoles();
        if (roles.stream().filter(role -> role.getName().equals("Muted")).findFirst().orElse(null) == null) {
            return true;
        } else {
            return false;
        }

    }
}
