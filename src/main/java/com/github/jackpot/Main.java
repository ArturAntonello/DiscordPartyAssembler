package com.github.jackpot;

import listeners.MyListener;
import listeners.MyReactionAddListener;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;

public class Main {
    public static void main(String[] args) {

        // Enable debug logging
        FallbackLoggerConfiguration.setDebug(true);

        // Enable trace logging
        FallbackLoggerConfiguration.setTrace(true);

        DiscordApi api = new DiscordApiBuilder()
                .setToken("INSERT BOT TOKEN HERE")
                .login().join();

        api.addListener(new MyListener());
        api.addReactionAddListener(new MyReactionAddListener());
    }

}
