package Currency;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Rob extends ListenerAdapter {
    EventWaiter waiter = new EventWaiter();

    public Rob(EventWaiter waiter) {
        this.waiter = waiter;
    }

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

    private HashMap<User, Integer> playerTimer = new HashMap<>();
    private int o = 0;

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        User org = null;
        EmbedBuilder eb = new EmbedBuilder();
        try {
            org = event.getMember().getUser();
        } catch (NullPointerException e) {

        }

        String prefix = "";
        try {
            prefix = prefix(event.getGuild().getIdLong());
        } catch (NumberFormatException | IOException f) {
            f.printStackTrace();
        }

        checkTimer();

        if (event.getAuthor().isBot() || !canRob(org) || org == null) {
            if (!canRob(org) && (event.getMessage().getContentRaw().contains(prefix + "rob")
                    || event.getMessage().getContentRaw().contains(prefix + "steal"))) {
                eb.setTitle("What is your plan?");
                eb.setDescription("Dude, if you are going to rob someone, at least make a plan! Wait **"
                        + playerTimer.get(org) + "** seconds to rob again!");

                int R = (int) (Math.random() * 256);
                int G = (int) (Math.random() * 256);
                int B = (int) (Math.random() * 256);
                Color color = new Color(R, G, B);
                Random random = new Random();
                final float hue = random.nextFloat();
                final float saturation = 0.9f;
                final float luminance = 1.0f;
                color = Color.getHSBColor(hue, saturation, luminance);
                eb.setColor(color);
                event.getChannel().sendMessage(eb.build()).queue();
            }
        } else if (event.getMessage().getContentRaw().contains(prefix + "rob")
                || event.getMessage().getContentRaw().contains(prefix + "steal")) {
            try {
                String split[] = event.getMessage().getContentRaw().split("\\s+");
                if (split.length == 1) {
                    event.getChannel().sendMessage("Do that again, but this time mention someone to rob!").queue();
                } else if (split[1].startsWith("<@")) {
                    long mentionedMember = 0;
                    User orgrob = null;
                    try {
                        mentionedMember = event.getMessage().getMentionedMembers().get(0).getIdLong();
                        orgrob = event.getMessage().getMentionedMembers().get(0).getUser();
                    } catch (IndexOutOfBoundsException e) {
                        event.getChannel().sendMessage("Don't ping a role!").queue();
                    }
                    long member = event.getMember().getIdLong();
                    if (member == mentionedMember) {
                        event.getChannel().sendMessage("Why in the world would you try to rob yourself???").queue();
                    } else if (orgrob != null) {
                        checkadd(member, org, event.getChannel(), mentionedMember, orgrob);
                    }

                } else {
                    event.getChannel().sendMessage("Do that again, but this time MENTION (@user) someone to rob!")
                            .queue();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // SEPERATE COMMANDS

    private void checkadd(long member, User org, TextChannel channel, long robbed, User orgrob) throws IOException {
        ArrayList<Long> MemberIDS = new ArrayList<Long>();
        ArrayList<Integer> MemberMoney = new ArrayList<Integer>();
        BufferedReader myReader = new BufferedReader(new FileReader("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/MemberMoney"));
        StringTokenizer st = null;
        String line;
        while ((line = myReader.readLine()) != null) {
            st = new StringTokenizer(line);

            long x = Long.parseLong(st.nextToken());
            int z = Integer.parseInt(st.nextToken());
            MemberIDS.add(x);
            MemberMoney.add(z);
        }

        myReader.close();

        Random random = new Random();
        int a = random.nextInt(2);
        boolean found = false;
        boolean robbedfound = false;
        for (int i = 0; i < MemberIDS.size(); i++) {
            if (MemberIDS.get(i) == member) {
                if (MemberMoney.get(i) < 500) {
                    channel.sendMessage("You prepared to rob " + orgrob.getName()
                            + " before realizing you needed 500 coins in order to not get beaten up"
                            + " by the police. You managed to get away before the police arrived!").queue();

                } else {
                    for (int j = 0; j < MemberIDS.size(); j++) {
                        if (MemberIDS.get(j) == robbed) {
                            robbedfound = true;
                            if (MemberMoney.get(j) < 500) {
                                channel.sendMessage("You arrived at " + orgrob.getName()
                                        + "'s house and realized they have no money. You managed to"
                                        + " get away before the police arrived!").queue();

                            } else if (a == 1) {
                                int robbedamount = random.nextInt(MemberMoney.get(j));
                                channel.sendMessage("You stole " + robbedamount + " from " + orgrob.getName() + "!")
                                        .queue();
                                MemberMoney.set(i, MemberMoney.get(i) + robbedamount);
                                MemberMoney.set(j, MemberMoney.get(j) - robbedamount);
                                setPlayerTime(org, 90);

                            } else {
                                channel.sendMessage(
                                        "You got caught **HAHAHAHA**. \n You paid up 500 coins to the police!").queue();
                                MemberMoney.set(i, MemberMoney.get(i) - 500);
                                setPlayerTime(org, 90);

                            }
                        }
                    }
                    if (robbedfound == false) {
                        channel.sendMessage("You tried to rob " + orgrob.getName()
                                + " before realizing this person doesn't have an active currency account!").queue();

                    }
                }

                found = true;
                break;

            }

        }
        if (found == false) {
            MemberIDS.add(member);
            MemberMoney.add(0);
            channel.sendMessage("New account registered!").queue();

        }

        PrintWriter myWriter = new PrintWriter(new BufferedWriter(new FileWriter("/Users/5kyle/IdeaProjects/KekBot/GuildData(Ignore)/MemberMoney")));

        for (int i = 0; i < MemberIDS.size(); i++) {

            String value = String.valueOf(MemberIDS.get(i));
            String money = String.valueOf(MemberMoney.get(i));

            myWriter.print(value);
            myWriter.print(" ");
            myWriter.print(money);
            myWriter.println();

        }
        myWriter.close();
    }

    public void checkTimer() {
        if (o == 0) {
            o = 1;
            startTimer();
        } else {

        }
    }

    public void startTimer() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ArrayList<User> remove = new ArrayList<User>();

                for (User member : playerTimer.keySet()) {
                    setPlayerTime(member, getPlayerTime(member) - 1);
                    if (getPlayerTime(member) == 0) {
                        remove.add(member);
                    }

                }
                for (int i = 0; i < remove.size(); i++) {
                    playerTimer.remove(remove.get(i));
                }
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    private int getPlayerTime(User member) {

        return playerTimer.get(member);
    }

    protected void setPlayerTime(User member, int num) {
        playerTimer.put(member, num);
    }

    protected boolean canRob(User member) {
        return !playerTimer.containsKey(member);
    }
}