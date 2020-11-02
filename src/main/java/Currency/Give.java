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

public class Give extends ListenerAdapter {
    EventWaiter waiter = new EventWaiter();

    public Give(EventWaiter waiter) {
        this.waiter = waiter;
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

        checkTimer();

        if (event.getAuthor().isBot() || !canRob(org) || org == null) {
            if (!canRob(org) && event.getMessage().getContentRaw().contains("-give")) {
                eb.setTitle("Processing...");
                eb.setDescription(
                        "The teller at the bank is going to be annoyed if you keep coming back to make transactions! Wait **"
                                + playerTimer.get(org) + "** seconds to give again!");

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
        } else if (event.getMessage().getContentRaw().contains("-give")) {
            try {
                String split[] = event.getMessage().getContentRaw().split("\\s+");
                if (split.length == 1) {
                    event.getChannel()
                            .sendMessage("Do that again, but this time put it in the format: -give [@user] [amount]")
                            .queue();
                } else if (split[1].startsWith("<@") && split.length == 3) {
                    long mentionedMember = 0;
                    User orggive = null;
                    try {
                        mentionedMember = event.getMessage().getMentionedMembers().get(0).getIdLong();
                        orggive = event.getMessage().getMentionedMembers().get(0).getUser();
                    } catch (IndexOutOfBoundsException e) {
                        event.getChannel().sendMessage("Don't ping a role!").queue();
                    }
                    long member = event.getMember().getIdLong();
                    int x = 0;
                    try {
                        x = Integer.parseInt(split[2]);
                    } catch (NumberFormatException e) {
                        event.getChannel().sendMessage("Don't try to break me! Send an INTEGER amount of money!")
                                .queue();
                    }
                    if (member == mentionedMember) {
                        event.getChannel().sendMessage("Doesn't seem like a smart idea to give yourself money... ")
                                .queue();
                    } else if (x != 0 && orggive != null) {
                        checkadd(member, org, event.getChannel(), mentionedMember, orggive, x);
                    }

                } else {
                    event.getChannel()
                            .sendMessage("Do that again, but this time put it in the format: -give [@user] [amount]")
                            .queue();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // SEPERATE COMMANDS

    private void checkadd(long member, User org, TextChannel channel, long gived, User orggive, int given)
            throws IOException {
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
        boolean givefound = false;
        for (int i = 0; i < MemberIDS.size(); i++) {
            if (MemberIDS.get(i) == member) {

                for (int j = 0; j < MemberIDS.size(); j++) {
                    if (MemberIDS.get(j) == gived) {
                        givefound = true;
                        if (MemberMoney.get(i) < given) {
                            channel.sendMessage("Nice try, I know you don't have that much money!").queue();
                        } else if (given < 0) {
                            channel.sendMessage("Nice try, you ain't gonna get the money though.").queue();
                        } else {
                            MemberMoney.set(i, MemberMoney.get(i) - given);
                            MemberMoney.set(j, MemberMoney.get(j) + given);
                            setPlayerTime(org, 15);
                            channel.sendMessage("You gave " + orggive.getName() + " " + given + " coins!").queue();

                        }
                    }
                }
                if (givefound == false) {
                    channel.sendMessage("You tried to give " + orggive.getName()
                            + " money before realizing this person doesn't have an active currency account!").queue();

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