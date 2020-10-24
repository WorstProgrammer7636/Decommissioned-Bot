package commands;

import java.awt.Color;
import java.util.Random;

import com.fasterxml.jackson.databind.JsonNode;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Meme extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        String message = event.getMessage().getContentRaw();
        if (message.equalsIgnoreCase("-meme")) {
            WebUtils.ins.getJSONObject("https://apis.duncte123.me/meme").async((json) -> {
                if (!json.get("success").asBoolean()) {
                    channel.sendMessage("Something went wrong, try again later!").queue();
                    System.out.println(json);
                    return;
                }
                int R = (int)(Math.random()*256);
                int G = (int)(Math.random()*256);
                int B= (int)(Math.random()*256);
                Color color = new Color(R, G, B); //random color, but can be bright or dull

                //to get rainbow, pastel colors
                Random random = new Random();
                final float hue = random.nextFloat();
                final float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
                final float luminance = 1.0f; //1.0 for brighter, 0.0 for black
                color = Color.getHSBColor(hue, saturation, luminance);

                final JsonNode data = json.get("data");
                final String title = data.get("title").asText();
                final String url = data.get("url").asText();
                final String image = data.get("image").asText();
                final EmbedBuilder embed = EmbedUtils.embedImageWithTitle(title, url, image);
                embed.setColor(color);
                channel.sendMessage(embed.build()).queue();
            });
        }
    }
}
