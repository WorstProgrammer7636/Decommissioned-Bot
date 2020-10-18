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
import net.dv8tion.jda.api.exceptions.ContextException;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HardMute extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String content = message.getContentRaw();
        MessageChannel channel = event.getChannel();
        if (content.startsWith("-hardmute"))
            if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {

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
                                    List<Role> roles = mentionedMember.getRoles();
                                    if (mentionedMember.hasPermission(Permission.ADMINISTRATOR)) {
                                        channel.sendMessage(
                                                "This person is also an adminstrator, so I cannot actually remove their roles unless you remove their administrator roles!")
                                                .queue();
                                    } else if (RoleFind(mentionedMember)) {
                                        channel.sendMessage("This person was already muted!").queue();
                                    } else {
                                        for (Role current : roles) {
                                            event.getGuild().removeRoleFromMember(mentionedMember, current).queue();
                                        }
                                        event.getGuild().addRoleToMember(mentionedMember, role).queue();
                                        EmbedBuilder info = new EmbedBuilder();
                                        info.setTitle("NEW HARDMUTE");
                                        info.addField("Info:", mentionedMember.getEffectiveName() + " was muted by "
                                                + event.getMember().getEffectiveName(), false);
                                        info.addField("Reason Given:", "None", false);
                                        info.addField("Time:", time + " minute(s)", false);
                                        info.setColor(0xeb3434);
                                        info.setFooter(event.getMember().getUser().getAsTag());
                                        textChannel.sendMessage(info.build()).queue();
                                        info.clear();
                                        channel.sendMessage("Hardmute successful! :white_check_mark:").queue();
                                        event.getGuild().removeRoleFromMember(mentionedMember, role).queueAfter(time,
                                                TimeUnit.MINUTES);
                                        for (Role current : roles) {
                                            event.getGuild().addRoleToMember(mentionedMember, current).queueAfter(time,
                                                    TimeUnit.MINUTES);
                                        }
                                        info.setTitle("NEW UNMUTE");
                                        info.addField("Info:", mentionedMember.getEffectiveName()
                                                + " has OFFICIALLY(may have been unmuted earlier)"
                                                + "finished their hardmute of " + time + " minute(s) initiated by "
                                                + event.getMember().getEffectiveName(), false);
                                        info.setColor(0x03a5fc);
                                        info.setFooter(event.getMember().getUser().getAsTag());

                                        textChannel.sendMessage(info.build()).queueAfter(time, TimeUnit.MINUTES);
                                        info.clear();

                                    }
                                }
                            } catch (NumberFormatException e) {
                                channel.sendMessage(
                                        "Can you use the command correctly PLEASE? Just do -hardmute @<user> [time in minutes][reason]! SIMPLE!")
                                        .queue();
                            } catch (HierarchyException e) {
                                channel.sendMessage(
                                        "Did you try to hardmute someone of a higher role than me? Unfortunately, I can't remove that role")
                                        .queue();
                                ;
                            }
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
                                    if (mentionedMember.hasPermission(Permission.ADMINISTRATOR)) {
                                        channel.sendMessage(
                                                "This person is also an adminstrator, so I cannot actually remove their roles unless you remove their administrator roles!")
                                                .queue();
                                    } else if (RoleFind(mentionedMember)) {
                                        channel.sendMessage("This person was already muted!").queue();
                                    } else {
                                        List<Role> roles = mentionedMember.getRoles();

                                        event.getGuild().addRoleToMember(mentionedMember, role).queue();
                                        for (Role current : roles) {
                                            event.getGuild().removeRoleFromMember(mentionedMember, current).queue();
                                        }
                                        EmbedBuilder info = new EmbedBuilder();
                                        info.setTitle("NEW HARDMUTE");
                                        info.addField("Info:", mentionedMember.getEffectiveName() + " was muted by "
                                                + event.getMember().getEffectiveName(), false);
                                        info.addField("Reason Given:", reason, false);
                                        info.addField("Time:", time + " minute(s)", false);
                                        info.setColor(0xeb3434);
                                        info.setFooter(event.getMember().getUser().getAsTag());
                                        textChannel.sendMessage(info.build()).queue();
                                        info.clear();
                                        channel.sendMessage("Hardmute successful! :white_check_mark:").queue();
                                        event.getGuild().removeRoleFromMember(mentionedMember, role).queueAfter(time,
                                                TimeUnit.MINUTES);
                                        for (Role current : roles) {
                                            event.getGuild().addRoleToMember(mentionedMember, current).queueAfter(time,
                                                    TimeUnit.MINUTES);
                                        }
                                        info.setTitle("NEW UNMUTE");
                                        info.addField("Info:", mentionedMember.getEffectiveName()
                                                + " has OFFICIALLY(may have been unmuted earlier)"
                                                + "finished their hardmute of " + time + " minute(s) initiated by "
                                                + event.getMember().getEffectiveName(), false);
                                        info.setColor(0x03a5fc);
                                        info.setFooter(event.getMember().getUser().getAsTag());

                                        textChannel.sendMessage(info.build()).queueAfter(time, TimeUnit.MINUTES);
                                        info.clear();
                                    }
                                }
                            } catch (NumberFormatException e) {
                                channel.sendMessage(
                                        "Can you use the command correctly PLEASE? Just do -hardmute @<user> [time in minutes][reason]! SIMPLE!")
                                        .queue();
                            }
                        } else {
                            channel.sendMessage(
                                    "Can you use the command correctly PLEASE? Just do -hardmute @<user> [time in minutes][reason]! SIMPLE!")
                                    .queue();
                        }

                    } else {
                        channel.sendMessage(
                                "Can you use the command correctly PLEASE? Just do -hardmute @<user> [time in minutes][reason]! SIMPLE!")
                                .queue();
                    }
                } else {
                    channel.sendMessage(
                            "Can you use the command correctly PLEASE? Just do -hardmute @<user> [time in minutes][reason]! SIMPLE!")
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