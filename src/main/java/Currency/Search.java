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
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Search extends ListenerAdapter {
    EventWaiter waiter = new EventWaiter();

    public Search(EventWaiter waiter) {
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

        if (event.getAuthor().isBot() || !canSearch(org) || org == null) {
            if (!canSearch(org) && event.getMessage().getContentRaw().contains("-search")) {
                eb.setTitle("Nothing Here!");
                eb.setDescription("You already searched this area for money! Wait **" + playerTimer.get(org)
                        + "** seconds to walk to a new area!");

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
        } else if (event.getMessage().getContentRaw().contains("-search")) {

            long member = org.getIdLong();
            try {
                check(member, org, event.getChannel());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    // SEPERATE COMMANDS

    private void check(long member, User org, TextChannel channel) throws IOException {
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
        String[] locations = { "Bush", "The Dev House", "Closet", "Underwear", "Middle School", "Trash Can", "Shirt",
                "Doll", "Backyard", "Porch" };
        Random random = new Random();
        String a = locations[random.nextInt(locations.length - 1)];
        String b = locations[random.nextInt(locations.length - 1)];
        String c = locations[random.nextInt(locations.length - 1)];

        while (true) {
            if (a.equals(b)) {
                a = locations[random.nextInt(locations.length - 1)];
            } else if (b.equals(c)) {
                b = locations[random.nextInt(locations.length - 1)];
            } else if (c.equals(a)) {
                a = locations[random.nextInt(locations.length - 1)];
            } else {
                break;
            }

        }
        boolean found = false;
        for (int i = 0; i < MemberIDS.size(); i++) {
            if (MemberIDS.get(i) == member) {

                setPlayerTime(org, 20);
                channel.sendMessage("Pick a spot to search: " + a + ", " + b + ", " + c).queue();
                found = true;
                initWaiter(channel, org, a, b, c);
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

    public void initWaiter(TextChannel channel, User user, String a, String b, String c) {
        waiter.waitForEvent(GuildMessageReceivedEvent.class, (event) -> {
            if (event.getChannel().equals(channel) && event.getAuthor().equals(user)) {
                return true;

            }
            return false;
        }, (event) -> {
            if (event.getMessage().getContentRaw().equalsIgnoreCase(a)) {
                setPlayerTime(user, 20);
                try {
                    getMoney(a, user, channel);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (event.getMessage().getContentRaw().equalsIgnoreCase(b)) {
                setPlayerTime(user, 20);
                try {
                    getMoney(b, user, channel);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (event.getMessage().getContentRaw().equalsIgnoreCase(c)) {
                setPlayerTime(user, 20);
                try {
                    getMoney(c, user, channel);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                channel.sendMessage("That's not a option my guy!").queue();
                setPlayerTime(user, 20);
            }
        }, 20, TimeUnit.SECONDS, () -> {
            channel.sendMessage("Kind of rude, you didn't select an option.").queue();
            setPlayerTime(user, 20);
        });
    }

    public void getMoney(String place, User user, TextChannel channel) throws IOException {
        ArrayList<Long> MemberIDS = new ArrayList<Long>();
        ArrayList<Integer> MemberMoney = new ArrayList<Integer>();
        BufferedReader myReader = new BufferedReader(new FileReader("MemberMoney"));
        StringTokenizer st = null;
        Random random = new Random();
        String line;
        while ((line = myReader.readLine()) != null) {
            st = new StringTokenizer(line);

            long x = Long.parseLong(st.nextToken());
            int z = Integer.parseInt(st.nextToken());
            MemberIDS.add(x);
            MemberMoney.add(z);

        }
        String[] locations = { "Bush", "The Dev House", "Closet", "Underwear", "Middle School", "Trash Can", "Shirt",
                "Doll", "Backyard", "Porch" };

        for (int i = 0; i < MemberIDS.size(); i++) {
            if (MemberIDS.get(i) == user.getIdLong()) {
                if (place.equalsIgnoreCase("Bush")) {
                    int amount = random.nextInt(100) + 57;
                    channel.sendMessage("You dove into a bush and found " + amount + " coins.").queue();
                    MemberMoney.set(i, MemberMoney.get(i) + amount);
                } else if (place.equalsIgnoreCase("The Dev House")) {
                    int amount = random.nextInt(400) + 300;
                    channel.sendMessage(
                            "You got " + amount + " coins from the developer house. WAIT A MINUTE DID YOU ROB US???")
                            .queue();
                    MemberMoney.set(i, MemberMoney.get(i) + amount);
                } else if (place.equalsIgnoreCase("Closet")) {
                    int amount = random.nextInt(100) + 100;
                    channel.sendMessage("You searched your closet and found " + amount + " coins.").queue();
                    MemberMoney.set(i, MemberMoney.get(i) + amount);
                } else if (place.equalsIgnoreCase("Underwear")) {
                    int amount = random.nextInt(200) + 50;
                    channel.sendMessage(
                            "You searched your underwear and found " + amount + " coins. You also found a lot of poop!")
                            .queue();
                    MemberMoney.set(i, MemberMoney.get(i) + amount);
                } else if (place.equalsIgnoreCase("Middle School")) {
                    int amount = random.nextInt(150) + 50;
                    channel.sendMessage("You went to your local middle school and found " + amount
                            + " coins. Did you take some kid's lunch money?").queue();
                    MemberMoney.set(i, MemberMoney.get(i) + amount);
                } else if (place.equalsIgnoreCase("Trash Can")) {
                    int amount = random.nextInt(100) + 10;
                    channel.sendMessage("You searched your trash can and found " + amount
                            + " coins before reeling back from the terrible smell.").queue();
                    MemberMoney.set(i, MemberMoney.get(i) + amount);
                } else if (place.equalsIgnoreCase("Shirt")) {
                    int amount = random.nextInt(50) + 50;
                    channel.sendMessage(
                            "You searched your shirt and found " + amount + " coins. How is that even possible?")
                            .queue();
                    MemberMoney.set(i, MemberMoney.get(i) + amount);
                } else if (place.equalsIgnoreCase("Doll")) {
                    int amount = random.nextInt(200) + 300;
                    channel.sendMessage("You searched your sibling's doll and found " + amount
                            + " coins. Looks like you found SOMEONE's secret stash!").queue();
                    MemberMoney.set(i, MemberMoney.get(i) + amount);
                } else if (place.equalsIgnoreCase("Backyard")) {
                    int amount = random.nextInt(200) + 50;
                    channel.sendMessage("You searched your backyard and found " + amount
                            + " coins. I wonder who dropped this much money?").queue();
                    MemberMoney.set(i, MemberMoney.get(i) + amount);
                } else if (place.equalsIgnoreCase("Porch")) {
                    int amount = random.nextInt(100) + 50;
                    channel.sendMessage("You searched your porch and found " + amount
                            + " coins. I wonder who dropped this much money?").queue();
                    MemberMoney.set(i, MemberMoney.get(i) + amount);
                }

            }
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

        myReader.close();
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

    protected boolean canSearch(User member) {
        return !playerTimer.containsKey(member);
    }
}
