package com.grogware.commandalias;

import com.grogware.commandalias.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.config.Property;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommandHandler implements ICommand {

    private String command = "";
    private String usage = "";
    private String alias = "";
    private static ConfigHandler config;

    private void LoadConfig() {
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

    public CommandHandler(String masterCommand)
    {
        LoadConfig();
        this.command = command;
        this.alias = "internal";
        this.usage = "usage: "+ masterCommand;
        System.out.println("Alias setup for " + masterCommand);

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

        StringBuffer commandLine = new StringBuffer("");

        for (String entry:strings) {
            commandLine.append(entry+" ");
        }

        switch(alias) {
            case "internal" : {
                switch(command) {
                    case "alias": {
                        //Minecraft.getMinecraft().player.sendChatMessage("ALIAS COMMAND: " + commandLine.toString().trim());
                        if(strings.length == 0) {
                            Minecraft.getMinecraft().player.sendChatMessage("No alias sub command i.e. /alias list");
                        }
                        else {
                            switch (strings[0].toLowerCase()) {
                                case "list": {
                                    if(strings.length>1) {
                                        Minecraft.getMinecraft().player.sendChatMessage("Usage: /alias list");

                                    }
                                    else {
                                        Set<Map.Entry<String, Property>> aliases = config.getCategoryContent("aliases");

                                        if(aliases != null) {
                                            for (Map.Entry<String, Property> entry : aliases) {
                                                Minecraft.getMinecraft().player.sendChatMessage("Alias " + entry.getKey() + ": " + entry.getValue().getString());
                                            }
                                        }
                                        else {
                                            Minecraft.getMinecraft().player.sendChatMessage("No Aliases Found!");
                                        }
                                    }
                                    break;
                                }
                                case "add": {
                                    if (strings.length < 3) {
                                        Minecraft.getMinecraft().player.sendChatMessage("Usage: /alias add <command> <alias command>");
                                    } else {
                                        String newAlias = strings[1];
                                        StringBuffer newCommandLine = new StringBuffer("");

                                        if (config.hasKey("aliases", newAlias)) {
                                            Minecraft.getMinecraft().player.sendChatMessage("alias " + newAlias + " already exists");
                                        }
                                        else {
                                            for (int x=2; x<strings.length; x++) {
                                                newCommandLine.append(strings[x]+" ");
                                            }
                                            Minecraft.getMinecraft().player.sendChatMessage("new alias " + newAlias + " added");
                                            config.writeConfig("aliases", newAlias, newCommandLine.toString().trim());
                                        }
                                    }
                                    break;
                                }
                                case "remove": {
                                    if (strings.length != 2) {
                                        Minecraft.getMinecraft().player.sendChatMessage("Usage: /alias remove <command>");
                                    } else {
                                        String oldAlias = strings[1];

                                        if (!config.hasKey("aliases", oldAlias)) {
                                            Minecraft.getMinecraft().player.sendChatMessage("alias " + oldAlias + " does not exist");
                                        }
                                        else {
                                            config.removeConfig("aliases", oldAlias);
                                            Minecraft.getMinecraft().player.sendChatMessage("alias " + oldAlias + " removed");
                                        }
                                    }
                                    break;
                                }
                                default: {
                                    Minecraft.getMinecraft().player.sendChatMessage("Unknown sub-command: " + strings[0]);
                                }
                            }

                        }
                        break;
                    }
                }
                break;
            }
            default: {
                Minecraft.getMinecraft().player.sendChatMessage(alias + " " + commandLine.toString().trim());
            }
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer minecraftServer, ICommandSender iCommandSender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings, BlockPos blockPos) {
        return Arrays.asList("");
    }

    @Override
    public boolean isUsernameIndex(String[] strings, int i) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
