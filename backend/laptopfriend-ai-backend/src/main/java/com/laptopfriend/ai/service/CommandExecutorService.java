package com.laptopfriend.ai.service;

import com.laptopfriend.ai.entity.VoiceInteraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


@Service
public class CommandExecutorService {
    private static final Logger logger =
            LoggerFactory.getLogger(CommandExecutorService.class);

    // Safe command whitelist(expand later)
    private final Map<String , String> COMMAND_MAP = new HashMap<>();

    public CommandExecutorService(){
        COMMAND_MAP.put("open notepad" , "notepad.exe");
        COMMAND_MAP.put("notepad kholo", "notepad.exe");
        COMMAND_MAP.put("नोटपॅड उघडा", "notepad.exe");

        COMMAND_MAP.put("open calculator", "calc.exe");
        COMMAND_MAP.put("कॅल्क्युलेटर उघडा", "calc.exe");

        COMMAND_MAP.put("open browser", "cmd.exe /c start chrome");  // Windows
        COMMAND_MAP.put("ब्राउजर उघडा", "cmd.exe /c start chrome");
        COMMAND_MAP.put("open chrome", "cmd.exe /c start chrome");

        COMMAND_MAP.put("open desktop", "explorer.exe %USERPROFILE%\\Desktop");
        COMMAND_MAP.put("डेस्कटॉप दाखवा", "explorer.exe %USERPROFILE%\\Desktop");
        COMMAND_MAP.put("डेस्कटॉप उघडा", "explorer.exe %USERPROFILE%\\Desktop");

        COMMAND_MAP.put("open terminal", "cmd.exe");
        COMMAND_MAP.put("terminal उघडा", "cmd.exe");
        // Linux/Mac alternatives
        COMMAND_MAP.put("open terminal", System.getProperty("os.name").toLowerCase().contains("win") ?
                "cmd.exe" : "gnome-terminal");  // Linux

        COMMAND_MAP.put("shutdown", "shutdown /s /t 60");  // 60s warning!
    }

    public String executeCommand(String translatedText , VoiceInteraction interaction)
    {
        String cleanText = translatedText.toLowerCase().trim();
        // find matching command
        for (Map.Entry<String , String> entry : COMMAND_MAP.entrySet()){
            if(cleanText.contains(entry.getKey().toLowerCase()))
            {
                return safeExecute(entry.getValue(), interaction);
            }
        }
        return searchAndExecute(cleanText , interaction);
    }

    private String safeExecute(String command , VoiceInteraction interaction)
    {
        try {
            logger.info("Executing safe command: {}",command);
            interaction.setCommandExecuted(command);
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null)
            {
                output.append(line).append("\n");
            }
            int exitCode = process.waitFor();
            return String.format("Command '%s' executed (exit code : %d)%s",command,exitCode,output.length()>0 ? "\nOutput : "+output : "");
        }
        catch (Exception e)
        {
            logger.error("Error executing command : {}",command,e);
            interaction.setCommandExecuted("ERROR: " + e.getMessage());
            return "Command failed: " + e.getMessage();
        }
    }

    private String searchAndExecute(String text , VoiceInteraction interaction) {
        // Simple Keyword matching (Expand with LLP later)
        if(Pattern.compile(".*(file|folder).*").matcher(text).find())
        {
            return safeExecute("explorer.exe",interaction);
        }
        return "Command not recognized. Try : open notepad , open calculator , open browser , open desktop ";
    }
}
