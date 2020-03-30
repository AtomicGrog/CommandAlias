package com.grogware.commandalias.commandhandlers;

import com.grogware.commandalias.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.command.ICommand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommandHandler extends CommandBase {

    private String command = "";
    private String usage = "";
    private String alias = "";
    private static ConfigHandler config;
    private boolean active = true;

    void LoadConfig() {
        config = new ConfigHandler();
        config.init("config/CommandAlias.cfg");
    }

    public CommandHandler(String command, String alias)
    {
        LoadConfig();
        this.command = command;
        this.alias = alias;
        this.usage = "usage: "+command+ " usa a /alias of " +alias;
        System.out.println("Alias setup for " + command);

    }

    public void setAlias(String alias) {
        this.alias = alias;
        this.active = true;
    }

    @Override
    public String getName() {
        return this.command;
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return usage;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(this.command);
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {

        if(isEnabled()) {
            if(active) {
                StringBuffer commandLine = new StringBuffer("");
                for (String entry : strings) {
                    commandLine.append(entry).append(" ");
                }
                Minecraft.getMinecraft().player.sendChatMessage(alias + " " + commandLine.toString().trim());
            }
        }
        else if (isVerbose())
        {
            iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "hint: no action taken, aliases disabled"));
        }
    }

    public boolean isActive() {
        return active;
    };

    public void setInactive() {
        active = false;
    }

    boolean isEnabled() {
        if(config.hasKey("settings", "enabled")) {
            return config.getBoolean("settings", "enabled");
        } else {
            return true;
        }
    }

    boolean isVerbose() {
        if(config.hasKey("settings", "verbose")) {
            return config.getBoolean("settings", "verbose");
        } else {
            return true;
        }
    }
}
