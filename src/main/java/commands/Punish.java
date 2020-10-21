package commands;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.ShardManager;

public class Punish extends ListenerAdapter {
	EventWaiter waiter = new EventWaiter();
	public Punish(EventWaiter waiter) {
		this.waiter=waiter;
	}
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();
		long user = event.getMember().getUser().getIdLong();
		String content = message.getContentRaw();
		MessageChannel channel = event.getChannel();
		String reason = "";
		long channelID=channel.getIdLong();
		if (content.startsWith("-permmute"))
			if (event.getMember().hasPermission(Permission.MANAGE_ROLES)) {

				String[] spliced = content.split("\\s+");
				TextChannel textChannel = event.getGuild().getTextChannelsByName("log", true).get(0);

				int length = spliced.length;
				if (length >= 2) {
					if (length == 2) {
						if (spliced[1].startsWith("<@")) {
							Role role = event.getGuild().getRolesByName("Muted", false).get(0);
							List<Member> mentionedMembers = message.getMentionedMembers();
							for (Member mentionedMember : mentionedMembers) {
								if (mentionedMember.hasPermission(Permission.MANAGE_ROLES)) {
									channel.sendMessage(
											"This person is also able to change roles, I can't do that! If you have administrator, please remove these roles!")
											.queue();
								} else if (RoleFind(mentionedMember)) {
									channel.sendMessage("This person was already muted!").queue();
								} else {
									
									channel.sendMessage("Are you sure? Type 'yes' in this channel to confirm this permmute.").queue();;
									initWaiter(channelID, user, event.getJDA().getShardManager(), role, mentionedMember, textChannel, "None", channel);
								}
							}

						} else {
							channel.sendMessage(
									"Can you use the command correctly PLEASE? Just do -mute @<user>! SIMPLE!").queue();

						}
					} else if (length > 2) {
						if (spliced[1].startsWith("<@")) {
							reason = "";
							for (int i = 2; i < length; i++) {
								reason += spliced[i];
								reason += " ";
							}
							Role role = event.getGuild().getRolesByName("Muted", false).get(0);
							List<Member> mentionedMembers = message.getMentionedMembers();
							for (Member mentionedMember : mentionedMembers) {
								if (mentionedMember.hasPermission(Permission.MANAGE_ROLES)) {
									channel.sendMessage("This person is also able to change roles, I can't do that! If you have administrator, please remove these roles!")
											.queue();
								} else if (RoleFind(mentionedMember)) {
									channel.sendMessage("This person was already muted!").queue();
								} else {
									channel.sendMessage("Are you sure? Type 'yes' in this channel to confirm this permmute.").queue();;
									initWaiter(channelID, user, event.getJDA().getShardManager(), role, mentionedMember, textChannel, reason, channel);
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
	private void initWaiter(long channel, long user, ShardManager shardmanager, Role role, Member member, TextChannel log, String reason, MessageChannel sendchannel) {
		waiter.waitForEvent(MessageReceivedEvent.class, (event) -> {
			long nchannel = event.getChannel().getIdLong();
			long nuser = event.getMember().getUser().getIdLong();
			
			return channel == nchannel && user == nuser && event.getMessage().getContentRaw().equalsIgnoreCase("yes");
			}, (event)->{
				event.getGuild().addRoleToMember(member, role).queue();
				EmbedBuilder info = new EmbedBuilder();
				info.setTitle("NEW PERMMUTE");
				info.addField("Info:", member.getEffectiveName() + " was muted by "
						+ event.getMember().getEffectiveName(), false);
				info.addField("Reason Given:", reason, false);
				info.setColor(0xeb3434);
				info.setFooter(event.getMember().getUser().getAsTag());
				log.sendMessage(info.build()).queue();
				event.getChannel().sendMessage("Mute successful! :white_check_mark:").queue();
				
			}, 10, TimeUnit.SECONDS, () -> {
				sendchannel.sendMessage("You didn't respond with 'yes' in time! Retry the command if you want to mute someone.").queue();;
			});
	}
}
