package commands;

import com.darkprograms.speech.translator.GoogleTranslate;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.JSONException;

import javax.management.ListenerNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Translate extends ListenerAdapter {
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
    public void onGuildMessageReceived(GuildMessageReceivedEvent command){
        String prefix = "";
        try {
            prefix = prefix(command.getGuild().getIdLong());
        } catch (NumberFormatException | IOException f) {
            f.printStackTrace();
        }
        String orgmessage = command.getMessage().getContentRaw();
        String after = orgmessage.trim().replaceAll(" +", " ");
        EmbedBuilder info = new EmbedBuilder();

        //language array (note: some language abvs are deprecated or no longer existent)
        String[] languages = {"en", "es", "fr", "zh", "ko", "el", "ja", "ru", "ar", "aa", "ab", "ae", "af", "ak", "am", "an",
                "ar", "as", "av", "ay", "az", "ba", "be", "bg", "bh", "bi", "bm", "bn", "bo", "br", "bs", "ca", "ce", "ch", "co", "cr", "cs", "cu", "cv",
                "cy", "da", "de", "dv", "dz", "ee", "en", "eo", "es", "et", "eu", "fa", "ff", "fi", "fj", "fo", "fy", "ga", "gd", "gl", "gn", "gu", "gv",
                "ha", "he", "hi", "ho", "hr", "ht", "hu", "hy", "hz", "ia", "id", "ie", "ig", "ii", "ik", "io", "is", "it", "iu", "ja", "jv",
                "ka", "kg", "ki", "kj", "kk", "kl", "km", "kn", "ko", "kr", "ks", "ku", "kv", "kw", "ky", "la", "lb", "lg", "li", "ln", "lo", "lt", "lu", "lv",
                "mg", "mh", "mi", "mk", "ml", "mn", "mr", "ms", "mt", "my", "na", "nb", "nd", "ne", "ng", "nl", "nn", "no", "nr", "nv", "ny",
                "oc", "oj", "om", "or", "os", "pa", "pi", "pl", "ps", "pt", "qu", "rm", "rn", "ro", "ru", "rw", "sa", "sc", "sd", "se", "sg", "si",
                "sk", "sl", "sm", "sn", "so", "sq", "sr", "ss", "st", "su", "sv", "sw", "ta", "te", "tg", "th", "ti", "tk", "tl", "tn", "to", "tr", "ts", "tt", "tw", "ty",
                "ug", "uk", "ur", "uz", "ve", "vi", "vo", "wa", "wo", "xh", "yi", "yo", "za", "zu"};

        String[] message = after.split(" ");
        if (message[0].equalsIgnoreCase(prefix + "translate") && message.length == 1){
            EmbedBuilder description = new EmbedBuilder();
            description.setTitle("TRANSLATE COMMAND");
            description.addField("Info", "This is the translate command! Please type " +
                    prefix + "translate [language abv] [word/sentence]\n For example: [" + prefix + "translate es please do the" +
                    " dishes] will translate 'please do the dishes' into spanish\n ", false);

            description.setDescription("LIST OF POPULAR LANGUAGE ABVs\n en - ENGLISH\n es - SPANISH\n fr - FRENCH\n zh - CHINESE\n ko - KOREAN\n " +
                    "el - GREEK\n ja - JAPANESE\n ru - RUSSIAN \n ar - ARABIC\n ------ \n TO VIEW MORE LANGUAGES: GO HERE:\n " +
                    "(NOTE: SOME ABBREVIATIONS NO LONGER EXIST OR WORK)\n" +
                    "https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes#BR\n");

            command.getChannel().sendMessage(description.build()).queue();
            description.clear();
        } else if (message[0].equalsIgnoreCase(prefix + "translate") && message.length >= 2){
            String sentence = "";

            for (int i = 2; i < message.length; i++){
                sentence += message[i];
                sentence += " ";
            }
            try {
                boolean languageFound = false;
                for (int i = 0; i < languages.length; i++){
                    if (message[1].equalsIgnoreCase(languages[i])){
                        languageFound = true;
                        break;
                    }
                }

                if (languageFound){
                    command.getChannel().sendMessage(GoogleTranslate.translate(message[1], sentence)).queue();
                } else {
                    command.getChannel().sendMessage("Language not found. You can view the currently available languages by typing " + prefix + "translate").queue();
                }

            } catch (IOException e) {
                e.printStackTrace();
                command.getChannel().sendMessage("Language not found/no longer available or you did not type a statement to translate").queue();
            } catch (IllegalArgumentException e) {
                command.getChannel().sendMessage(
                        "You didn't ask me to translate something")
                        .queue();

            } catch (JSONException e) {
                command.getChannel().sendMessage(
                        "You didn't ask me to translate something")
                        .queue();
            }
        }

    }

}
