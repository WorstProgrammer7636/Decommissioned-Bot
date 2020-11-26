package events;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelloEvent extends ListenerAdapter {
    public String prefix(long id) throws NumberFormatException, IOException {

        BufferedReader br = new BufferedReader(new FileReader("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/Prefixes"));
        StringTokenizer st = null;
        String line;
        while ((line = br.readLine()) != null) {
            st = new StringTokenizer(line);
            if (id == Long.parseLong(st.nextToken())) {
                br.close();
                String prefix = st.nextToken();

                return prefix;
            }
        }
        br.close();
        return "ERROR";
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String messageSent = event.getMessage().getContentRaw();
        String[] parts = messageSent.split(" ");
        String prefix = "";
        try {
            prefix = prefix(event.getGuild().getIdLong());
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
        if (parts[0].equalsIgnoreCase("-info") || parts[0].equalsIgnoreCase((prefix) + "info")) {
            EmbedBuilder info = new EmbedBuilder();
            info.setAuthor("Inutile Bot");
            info.setTitle("BOT/SERVER INFORMATION");
            info.addField("Moderation Commands Information",
                    "If your moderation commands are not working, try some of the following: \n 1. Ensure that Inutile has Administrator! \n 2. Make sure that Inutile is above 'Muted' in your "
                            + "server's role hierarchy! \n 3. Make sure that Inutile has access to the 'log' channel if your server has one. ",
                    false);
            if (prefix.equalsIgnoreCase("-")) {

                info.addField("Custom Prefix",
                        "Your server does not have a custom prefix! This server is using the default prefix '-' \n ** To change your server's prefix, do '"
                                + prefix
                                + "changeprefix [newprefix]'.** \n **-info and -commands will always be usable with '-' along with any new prefixes! " +
                                "\nNOTE: CHANGING PREFIXES DOES NOT WORK YET. THIS FEATURE WILL BE FINISHED AND ADDED SOON!**",
                        false);


                info.addField("Custom Prefixes",
                                "\nNOTE: CUSTOM PREFIXES ARE CURRENTLY UNDERGOING DEVELOPMENT AND WILL BE ADDED SOON**",
                        false);
            } else {

                info.addField("Custom Prefix", "Your server has a custom prefix! It is '" + prefix
                                + "' An example usage of this prefix would be \n '" + prefix
                                + "color'. \n **To change your server's prefix, do '" + prefix
                                + "changeprefix [newprefix]'.** \n **-info and -commands will always be usable with '-' along with any new prefixes! **",
                        false);


            }
            info.setColor(0x03a5fc);
            info.setDescription(
                    "Thank you for using Inutile, the multipurpose bot! \n **To see the currently available commands, do -commands.** \n \n ***To use the majority of the commands, you must use the prefix '"
                            + prefix + "'***");
            info.setFooter("Created by Nard and hold up");
            info.addField("Still Having Issues or Have Suggestions For The Bot?",
                    "Join Our Community and Help Server! \n [Support Server](https://discord.gg/FmvFezPzu6) \n Invite the bot to another server! Use this link: \n [Inutile Bot](https://discord.com/api/oauth2/authorize?client_id=765713285965807657&permissions=8&scope=bot)",
                    false);
            info.addField("Visit our website!",
                    "Website Link: \n [Inutile Bot Official Website](https://worstprogrammer7636.github.io/) \n",
                    false);
            event.getChannel().sendMessage(info.build()).queue();
            info.clear();
        }
        if (parts[0].equalsIgnoreCase("-commands") || parts[0].equalsIgnoreCase((prefix) + "commands")) {
            EmbedBuilder justTitle = new EmbedBuilder();
            if (parts.length == 1) {
                justTitle.setTitle("CHOOSE A TYPE:");
                justTitle.addField("Command Types:",
                        "**Normal** (commands normal) \n **Currency** (commands currency) \n **Moderation** (commands moderation)",
                        false);
                justTitle.setFooter(
                        "To see further details about a category, do -commands [Normal/Currency/Moderation]. To see more about a specific command, "
                                + "just type it in. This bot also has censorship implemented that will delete "
                                + "VERY bad messages.");
                justTitle.setColor(0x03a5fc);
                event.getChannel().sendMessage(justTitle.build()).queue();
            } else {
                if (parts[1].equalsIgnoreCase("Normal")) {
                    justTitle.setColor(0x03a5fc);

                    justTitle.setTitle("Normie Commands");
                    justTitle.addField("Command List",
                            "```\n " + prefix + "calculate\n " + prefix + "piglatin \n " + prefix + "info \n " + prefix
                                    + "rickroll \n " + prefix + "translate \n " + prefix + "meme \n " + prefix + "av```",
                            false);
                    justTitle.setFooter("To further see what a command does, just type it in");
                    event.getChannel().sendMessage(justTitle.build()).queue();
                    justTitle.clear();
                } else if (parts[1].equalsIgnoreCase("currency")) {
                    justTitle.setColor(0x03a5fc);

                    justTitle.setTitle("Currency Commands");

                    justTitle.addField("Command List",
                            "```\n " + prefix + "xp \n " + prefix + "leaderboard \n " + prefix + "beg \n " + prefix
                                    + "search \n " + prefix + "rob \n " + prefix + "slots \n " + prefix + "jackpot \n "
                                    + prefix + "kill \n " + prefix + "give \n " + prefix + "bal \n " + prefix
                                    + "shop```",
                            false);
                    justTitle.setFooter("To further see what a command does, just type it in");
                    event.getChannel().sendMessage(justTitle.build()).queue();
                    justTitle.clear();

                } else if (parts[1].equalsIgnoreCase("Moderation")) {
                    justTitle.setColor(0x03a5fc);

                    justTitle.setTitle("Mods Only Commands");

                    justTitle.addField("Command List",
                            "```\n " + prefix + "toggle \n " + prefix + "permmute \n " + prefix + "unmute \n " + prefix
                                    + "tempmute \n " + prefix + "hardmute \n " + prefix + "changeprefix \n " + prefix
                                    + "kick \n " + prefix + "permban \n " + prefix + "tempban \n```",
                            false);
                    justTitle.setFooter("To further see what a command does, just type it in");
                    event.getChannel().sendMessage(justTitle.build()).queue();
                    justTitle.clear();
                }

            }
        }
    }

}
