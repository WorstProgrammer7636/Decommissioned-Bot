package commands;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.ShardManager;

public class Kick extends ListenerAdapter {
	EventWaiter waiter = new EventWaiter();

	public Kick(EventWaiter waiter) {
		this.waiter = waiter;
	}

	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		Message message = e.getMessage();
		String content = message.getContentRaw();
		MessageChannel channel = e.getChannel();
		String reason = "";
		if (content.startsWith("-kick")) {
			if (e.getMember().hasPermission(Permission.KICK_MEMBERS)) {
				String[] spliced = content.split("\\s+");
				TextChannel textChannel = e.getGuild().getTextChannelsByName("log", true).get(0);
				int length = spliced.length;
				if (length == 1) {
					channel.sendMessage(
							"To use this command, type '-kick @<user> [reason]. A reason must be provided for this command to work.")
							.queue();
				} else if (length == 2) {
					channel.sendMessage(
							"In order to use this command, you must provide a reason for doing so as kicking someone is a serious action.")
							.queue();
				} else if (length > 2) {
					if (spliced[1].startsWith("<@")) {
						List<Member> mentions = message.getMentionedMembers();

						Member mentionedMember = mentions.get(0);
						if (mentionedMember.hasPermission(Permission.KICK_MEMBERS)) {
							channel.sendMessage(
									"This person can also kick members, so you are not allowed to kick them.").queue();
						} else {
							for (int i = 2; i < length; i++) {
								reason += spliced[i];
								reason += " ";
							}
							long originchannel = channel.getIdLong();
							long user = e.getAuthor().getIdLong();
							channel.sendMessage(
									"Are you sure? Type 'yes' in this channel to confirm that you want to kick "
											+ mentionedMember.getEffectiveName() + " from the server.")
									.queue();
							initWaiter(user, textChannel, originchannel, e.getChannel(), e.getJDA().getShardManager(),
									reason, mentionedMember);
						}
					}else {
						channel.sendMessage(
								"To use this command, type '-kick @<user> [reason]. A reason must be provided for this command to work.")
								.queue();
					}
				}
			} else {
				channel.sendMessage("You failed to account for the fact that you are not allowed to do this.").queue();
			}
		}
	}

	private void initWaiter(long user, TextChannel log, long originchannel, TextChannel channel,
			ShardManager shardmanager, String reason, Member member) {
		waiter.waitForEvent(GuildMessageReceivedEvent.class, (event) -> {
			long nchannel = event.getChannel().getIdLong();
			long nuser = event.getMember().getUser().getIdLong();

			return originchannel == nchannel && user == nuser
					&& event.getMessage().getContentRaw().equalsIgnoreCase("yes");
		}, (event) -> {

			EmbedBuilder info = new EmbedBuilder();
			member.kick().queue();
			info.setTitle("NEW KICK");
			info.addField("Info:", member.getEffectiveName() + " was kicked by " + event.getMember().getEffectiveName(),
					false);
			info.addField("Reason Given:", reason, false);
			info.setColor(0xeb3434);
			info.setFooter(event.getMember().getUser().getAsTag());
			log.sendMessage(info.build()).queue();
			event.getChannel().sendMessage("The member has been kicked! :white_check_mark:").queue();

		}, 10, TimeUnit.SECONDS, () -> {

			channel.sendMessage("You didn't respond with 'yes' in time! Retry the command if you want to kick someone.")
					.queue();
			;
		});
	}
}
