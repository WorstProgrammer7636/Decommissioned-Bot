package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class Punish extends ListenerAdapter{

    public void onGuildMessageReceived (GuildMessageReceivedEvent event){
        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();
        if (content.startsWith("-mute"))
            if (event.getMember().hasPermission(Permission.MANAGE_ROLES)) {

                String[] spliced = content.split("\\s+");
                TextChannel textChannel = event.getGuild().getTextChannelsByName("log", true).get(0);

                int length = spliced.length;
                if (length == 1){
                    channel.sendMessage("You gotta tell me who to mute man. Just do -mute @<user> <reason> (reason is optional)")
                            .queue();
                    return;
                }
                if (length >= 2) {
                    if (length == 2) {
                        if (spliced[1].startsWith("<@")) {
                            Role role = event.getGuild().getRolesByName("Muted", false).get(0);
                            List<Member> mentionedMembers = message.getMentionedMembers();
                            for (Member mentionedMember : mentionedMembers) {
                                if (mentionedMember.hasPermission(Permission.MANAGE_ROLES)) {
                                    channel.sendMessage("This person is also able to change roles, I can't do that!")
                                            .queue();
                                } else if (RoleFind(mentionedMember)) {
                                    channel.sendMessage("This person was already muted!").queue();
                                } else {
                                    event.getGuild().addRoleToMember(mentionedMember, role).queue();
                                    EmbedBuilder info = new EmbedBuilder();
                                    info.setTitle("NEW MUTE");
                                    info.addField("Info:", mentionedMember.getEffectiveName() + " was muted by "
                                            + event.getMember().getEffectiveName(), false);
                                    info.addField("Reason Given:", "None", false);
                                    info.setColor(0xeb3434);
                                    textChannel.sendMessage(info.build()).queue();
                                    channel.sendMessage("Mute successful! :white_check_mark:").queue();
                                }
                            }

                        } else {
                            channel.sendMessage(
                                    "Can you use the command correctly PLEASE? Just do -mute @<user>! SIMPLE!").queue();

                        }
                    } else if (length > 2) {
                        if (spliced[1].startsWith("<@")) {
                            String reason = "";
                            for (int i = 2; i < length; i++) {
                                reason += spliced[i];
                                reason += " ";
                            }
                            Role role = event.getGuild().getRolesByName("Muted", false).get(0);
                            List<Member> mentionedMembers = message.getMentionedMembers();
                            for (Member mentionedMember : mentionedMembers) {
                                if (mentionedMember.hasPermission(Permission.MANAGE_ROLES)) {
                                    channel.sendMessage("This person is also able to change roles, I can't do that!")
                                            .queue();
                                } else if (RoleFind(mentionedMember)) {
                                    channel.sendMessage("This person was already muted!").queue();
                                } else {
                                    event.getGuild().addRoleToMember(mentionedMember, role).queue();
                                    EmbedBuilder info = new EmbedBuilder();
                                    info.setTitle("NEW MUTE");
                                    info.addField("Info:", mentionedMember.getEffectiveName() + " was muted by "
                                            + event.getMember().getEffectiveName(), false);
                                    info.addField("Reason Given:", reason, false);
                                    info.setColor(0xeb3434);
                                    textChannel.sendMessage(info.build()).queue();
                                    channel.sendMessage("Mute successful! :white_check_mark:").queue();
                                }
                            }
                        } else {
                            channel.sendMessage(
                                    "Can you use the command correctly PLEASE? Just do -mute @<user>! SIMPLE!").queue();
                        }

                    }
                }
            } else {
                channel.sendMessage("You failed to account for the fact that you aren't allowed to do this.").queue();
            }

    }

    public boolean RoleFind(Member mentionedMember) {
        List<Role> roles = mentionedMember.getRoles();
        if (roles.stream().filter(role -> role.getName().equals("Muted")).findFirst().orElse(null) != null) {
            return true;
        } else {
            return false;
        }
    }

}
