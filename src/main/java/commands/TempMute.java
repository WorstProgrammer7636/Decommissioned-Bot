package commands;

import java.util.List;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TempMute extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();
        if (content.startsWith("-tempmute"))
            if (event.getMember().hasPermission(Permission.MANAGE_ROLES)) {

                String[] spliced = content.split("\\s+");
                TextChannel textChannel = event.getGuild().getTextChannelsByName("log", true).get(0);

                int length = spliced.length;
                if (length >= 3) {
                    if (length == 3) {
                        if (spliced[1].startsWith("<@")) {
                            Role role = event.getGuild().getRolesByName("Muted", false).get(0);
                            List<Member> mentionedMembers = message.getMentionedMembers();
                            int time = 0;
                            try {
                                time = Integer.parseInt(spliced[2]);
                                for (Member mentionedMember : mentionedMembers) {
                                    if (mentionedMember.hasPermission(Permission.MANAGE_ROLES)) {
                                        channel.sendMessage(
                                                "This person is also able to change roles, I can't do that! If you have administrator, please try hard mute!")
                                                .queue();
                                    } else if (RoleFind(mentionedMember)) {
                                        channel.sendMessage("This person was already muted!").queue();
                                    } else {
                                        event.getGuild().addRoleToMember(mentionedMember, role).queue();
                                        EmbedBuilder info = new EmbedBuilder();
                                        info.setTitle("NEW TEMPMUTE");
                                        info.addField("Info:", mentionedMember.getEffectiveName() + " was muted by "
                                                + event.getMember().getEffectiveName(), false);
                                        info.addField("Reason Given:", "None", false);
                                        info.addField("Time:", time + " minute(s)", false);
                                        info.setColor(0xeb3434);
                                        info.setFooter(event.getMember().getUser().getAsTag());
                                        textChannel.sendMessage(info.build()).queue();
                                        info.clear();
                                        channel.sendMessage("Tempmute successful! :white_check_mark:").queue();
                                        event.getGuild().removeRoleFromMember(mentionedMember, role).queueAfter(time,
                                                TimeUnit.MINUTES);
                                        info.setTitle("NEW UNMUTE");
                                        info.addField("Info:", mentionedMember.getEffectiveName()
                                                + " has OFFICIALLY(may have been unmuted earlier)"
                                                + "finished their tempmute of " + time + " minute(s) initiated by "
                                                + event.getMember().getEffectiveName(), false);
                                        info.setColor(0x03a5fc);
                                        info.setFooter(event.getMember().getUser().getAsTag());

                                        textChannel.sendMessage(info.build()).queueAfter(time, TimeUnit.MINUTES);
                                        info.clear();
                                    }
                                }

                            } catch (NumberFormatException e) {
                                channel.sendMessage(
                                        "Can you use the command correctly PLEASE? Just do -mute @<user> [time in minutes][reason]! SIMPLE!")
                                        .queue();

                            }

                        } else {
                            channel.sendMessage(
                                    "Can you use the command correctly PLEASE? Just do -mute @<user> [time in minutes][reason]! SIMPLE!")
                                    .queue();

                        }
                    } else if (length > 3) {
                        if (spliced[1].startsWith("<@")) {
                            String reason = "";
                            for (int i = 3; i < length; i++) {
                                reason += spliced[i];
                                reason += " ";
                            }
                            Role role = event.getGuild().getRolesByName("Muted", false).get(0);
                            List<Member> mentionedMembers = message.getMentionedMembers();
                            int time = 0;
                            try {
                                time = Integer.parseInt(spliced[2]);
                                for (Member mentionedMember : mentionedMembers) {
                                    if (mentionedMember.hasPermission(Permission.MANAGE_ROLES)) {
                                        channel.sendMessage(
                                                "This person is also able to change roles, I can't do that!").queue();
                                    } else if (RoleFind(mentionedMember)) {
                                        channel.sendMessage("This person was already muted!").queue();
                                    } else {
                                        event.getGuild().addRoleToMember(mentionedMember, role).queue();
                                        EmbedBuilder info = new EmbedBuilder();
                                        info.setTitle("NEW TEMPMUTE");
                                        info.addField("Info:", mentionedMember.getEffectiveName() + " was muted by "
                                                + event.getMember().getEffectiveName(), false);
                                        info.addField("Reason Given:", reason, false);
                                        info.addField("Time:", time + " minute(s)", false);
                                        info.setColor(0xeb3434);
                                        info.setFooter(event.getMember().getUser().getAsTag());
                                        textChannel.sendMessage(info.build()).queue();
                                        info.clear();
                                        channel.sendMessage("Tempmute successful! :white_check_mark:").queue();
                                        event.getGuild().removeRoleFromMember(mentionedMember, role).queueAfter(time,
                                                TimeUnit.MINUTES);
                                        info.setTitle("NEW UNMUTE");
                                        info.addField("Info:", mentionedMember.getEffectiveName()
                                                + " has OFFICIALLY(may have been unmuted earlier)"
                                                + "finished their tempmute of " + time + " minute(s) initiated by "
                                                + event.getMember().getEffectiveName(), false);
                                        info.setColor(0x03a5fc);
                                        info.setFooter(event.getMember().getUser().getAsTag());

                                        textChannel.sendMessage(info.build()).queueAfter(time, TimeUnit.MINUTES);
                                        info.clear();
                                    }
                                }

                            } catch (NumberFormatException e) {
                                channel.sendMessage(
                                        "Can you use the command correctly PLEASE? Just do -mute @<user> [time in minutes][reason]! SIMPLE!")
                                        .queue();

                            }

                        } else {
                            channel.sendMessage(
                                    "Can you use the command correctly PLEASE? Just do -tempmute @<user> [time in minutes][reason]! SIMPLE!")
                                    .queue();
                        }

                    }
                } else {
                    channel.sendMessage(
                            "Can you use the command correctly PLEASE? Just do -tempmute @<user> [time in minutes][reason]! SIMPLE!")
                            .queue();
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
