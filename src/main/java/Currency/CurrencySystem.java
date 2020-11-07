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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CurrencySystem extends ListenerAdapter {
    EventWaiter waiter = new EventWaiter();

    public CurrencySystem(EventWaiter waiter) {
        this.waiter = waiter;
    }

    private HashMap<User, Integer> playerTimer = new HashMap<>();
    private int o = 0;

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        long guild = event.getGuild().getIdLong();
        User org = null;
        EmbedBuilder eb = new EmbedBuilder();
        try {
            org = event.getMember().getUser();
        } catch (NullPointerException e) {

        }

        checkTimer();

        if (event.getAuthor().isBot() || !canGetMoney(org) || org == null) {
            if (!canGetMoney(org) && event.getMessage().getContentRaw().equalsIgnoreCase("-beg")) {
                eb.setTitle("Stop Begging!");
                eb.setDescription("Dude, if you keep begging, someone is going to harass you. Wait **"
                        + playerTimer.get(org) + "** seconds.");

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
        } else if (event.getMessage().getContentRaw().equalsIgnoreCase("-beg")) {
            try {

                long member = event.getMember().getIdLong();
                checkadd(member, org, event.getChannel());
                setPlayerTime(org, 30);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // SEPERATE COMMANDS
        String[] split = event.getMessage().getContentRaw().split("\\s+");
        if (split[0].equalsIgnoreCase("-bal")) {
            int length = split.length;
            if (length >= 2) {
                boolean found = false;
                String name = "";
                for (int j = 1; j < length; j++) {
                    name += split[j];
                    if (j < length - 1) {
                        name += " ";
                    }
                }
                int size = event.getGuild().getMembersByEffectiveName(name, true).size();
                found = false;
                for (int i = 0; i < size; i++) {

                    found = true;
                    try {
                        getMoney(guild, event.getGuild().getMembersByEffectiveName(name, true).get(0).getIdLong(),
                                event.getChannel(), event.getGuild().getMembersByEffectiveName(name, true).get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (found == false) {
                    event.getChannel()
                            .sendMessage("Please type in the nickname of someone in the server! (Don't ping!)").queue();
                }

            } else {
                try {
                    long member = event.getMember().getIdLong();

                    getMoney(guild, member, event.getChannel(), event.getMember());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkadd(long member, User org, TextChannel channel) throws IOException {
        ArrayList<Long> MemberIDS = new ArrayList<Long>();
        ArrayList<Integer> MemberMoney = new ArrayList<Integer>();
        BufferedReader myReader = new BufferedReader(new FileReader("MemberMoney"));
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
        boolean found = false;
        for (int i = 0; i < MemberIDS.size(); i++) {
            if (MemberIDS.get(i) == member) {
                int a = random.nextInt(750);
                if (a <= 400) {
                    MemberMoney.set(i, MemberMoney.get(i) + a);
                    channel.sendMessage("You got " + a + " coins from a kind fellow.").queue();
                } else {
                    channel.sendMessage("You didn't get any coins from begging. Get a job!").queue();
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

        PrintWriter myWriter = new PrintWriter(new BufferedWriter(new FileWriter("MemberMoney")));

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

    private void getMoney(long guild, long member, TextChannel channel, Member org) throws IOException {
        ArrayList<Long> MemberIDS = new ArrayList<Long>();
        ArrayList<Integer> MemberMoney = new ArrayList<Integer>();
        BufferedReader myReader = new BufferedReader(new FileReader("MemberMoney"));
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

        boolean found = false;
        for (int i = 0; i < MemberIDS.size(); i++) {
            if (MemberIDS.get(i) == member) {
                found = true;

                EmbedBuilder XP = new EmbedBuilder();
                XP.setAuthor(org.getEffectiveName() + "'s Balance", null, org.getUser().getAvatarUrl());
                int coins = MemberMoney.get(i);

                int R = (int) (Math.random() * 256);
                int G = (int) (Math.random() * 256);
                int B = (int) (Math.random() * 256);
                Color color = new Color(R, G, B); // random color, but can be bright or dull

                Random random = new Random();
                final float hue = random.nextFloat();
                final float saturation = 0.9f;// 1.0 for brilliant, 0.0 for dull
                final float luminance = 1.0f; // 1.0 for brighter, 0.0 for black
                color = Color.getHSBColor(hue, saturation, luminance);
                XP.addField("Coin Information", "Wallet: **" + coins + "** coins.", false);
                XP.setColor(color);
                channel.sendMessage(XP.build()).queue();
                break;
            }

        }
        if (found == false) {
            channel.sendMessage("Error: User Not Found!").queue();
        }
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

    protected boolean canGetMoney(User member) {
        return !playerTimer.containsKey(member);
    }
}
