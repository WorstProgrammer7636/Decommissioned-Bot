package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Meme extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e){
        String[] spliced = e.getMessage().getContentRaw().split(" ");

        JSONParser parser = new JSONParser();
        String postLink = "";
        String title = "";
        String url = "";

        if (spliced[0].equalsIgnoreCase("-meme")){
            try {
                URL memeUrl = new URL("https://meme-api.herokuapp.com/gimme");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(memeUrl.openConnection().getInputStream()));

                String lines;
                while ((lines = bufferedReader.readLine()) != null){
                    JSONArray array = new JSONArray();
                    array.add(parser.parse(lines));

                    for (Object o : array){
                        JSONObject jsonObject = (JSONObject) o;

                        postLink = (String)jsonObject.get("postLink");
                        title = (String) jsonObject.get("title");
                        url = (String) jsonObject.get("url");
                    }

                }
                bufferedReader.close();

                EmbedBuilder builder = new EmbedBuilder().setTitle(title, postLink).setImage(url).setColor(Color.ORANGE);
                e.getChannel().sendMessage(builder.build()).queue();

            } catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
                e.getChannel().sendMessage("Whoops. Looks like something went wrong! Please try again later").queue();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                e.getChannel().sendMessage("Whoops. Looks like something went wrong! Please try again later").queue();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
                e.getChannel().sendMessage("Whoops. Looks like something went wrong! Please try again later").queue();
            }
        }
    }
}
