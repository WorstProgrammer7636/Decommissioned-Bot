package events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.*;
import java.util.StringTokenizer;

public class StoreData extends ListenerAdapter {

    public void onGuildJoin(GuildJoinEvent e) {
        long guild = e.getGuild().getIdLong();
        appendUsingPrintWriter("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/enable.in", guild);
    }

    public void onGuildLeave(GuildLeaveEvent e){

    }

    private static void appendUsingPrintWriter(String filePath, long guild) {
        File file = new File(filePath);
        FileWriter fr = null;
        BufferedWriter br = null;
        PrintWriter pr = null;
        try {
            // to append to file, you need to initialize FileWriter using below constructor
            fr = new FileWriter(file, true);
            br = new BufferedWriter(fr);
            pr = new PrintWriter(br);
            pr.println(guild + " true");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                pr.close();
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
