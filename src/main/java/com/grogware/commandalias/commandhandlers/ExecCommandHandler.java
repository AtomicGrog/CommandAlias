package com.grogware.commandalias.commandhandlers;

import com.grogware.commandalias.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.config.Property;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExecCommandHandler extends CommandBase {

    private String command = "";
    private String usage = "";
    private static ConfigHandler config;

    void LoadConfig() {
        config = new ConfigHandler();
        config.init("config/CommandAlias.cfg");
    }

    public ExecCommandHandler(String masterCommand)
    {
        LoadConfig();
        this.command = masterCommand;
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

            switch(command) {
            case "alias": {
                if(strings.length == 0) {
                    iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "No alias sub command i.e. /alias list"));
                }
                else {
                    switch (strings[0].toLowerCase()) {
                        case "list": {
                            aliasList(iCommandSender, strings);
                            break;
                        }
                        case "add": {
                            aliasAdd(iCommandSender, strings);
                            break;
                        }
                        case "remove": {
                            aliasRemove(iCommandSender, strings);
                            break;
                        }
                        case "help": {
                            aliasHelp(iCommandSender);
                            break;
                        }
                        case "verbose": {
                            aliasToggleVerbose(iCommandSender);
                            break;
                        }
                        case "enable": {
                            aliasEnable(iCommandSender);
                            break;
                        }
                        case "disable": {
                            aliasDisable(iCommandSender);
                            break;
                        }
                        default: {
                            iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "Unknown sub-command: " + strings[0]));
                        }
                    }

                }
                break;
            }
        }
    }

    void aliasList(ICommandSender iCommandSender, String[] strings) {
        if(strings.length>1) {
            iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "Usage: /alias list"));
        }
        else {
            Set<Map.Entry<String, Property>> aliases = config.getCategoryContent("aliases");

            if(aliases != null) {
                for (Map.Entry<String, Property> entry : aliases) {
                    iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW +
                            "Alias '" + entry.getKey() + "': '" + entry.getValue().getString()+"'"));
                }
            }
            else {
                iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "No Aliases Defined"));
            }
        }
    }

    void aliasAdd(ICommandSender iCommandSender, String[] strings) {
        if (strings.length < 3) {
            iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "Usage: /alias add <command> <alias command>"));
        } else {
            String newAlias = strings[1];
            StringBuffer newCommandLine = new StringBuffer("");

            if (config.hasKey("aliases", newAlias)) {
                iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "alias '" + newAlias + "' already exists"));
            }
            else {
                for (int x=2; x<strings.length; x++) {
                    newCommandLine.append(strings[x]+" ");
                }
                if(isVerbose()) iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "new alias '" + newAlias + "' added"));

                ClientCommandHandler.instance.registerCommand(new CommandHandler(newAlias, newCommandLine.toString().trim()));
                config.writeConfig("aliases", newAlias, newCommandLine.toString().trim());
            }
        }
    }

    void aliasRemove(ICommandSender iCommandSender, String[] strings) {
        if (strings.length != 2) {
            iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "Usage: /alias remove <command>"));
        } else {
            String oldAlias = strings[1];

            if (!config.hasKey("aliases", oldAlias)) {
                Minecraft.getMinecraft().player.sendChatMessage("alias " + oldAlias + " does not exist");
            }
            else {
                config.removeConfig("aliases", oldAlias);
                if(isVerbose()) iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "alias " + oldAlias + " removed"));
                if(isVerbose()) iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "alias: Not command will not be removed or be re-usable until client has been restarted"));
                Map<String, ICommand> commands = ClientCommandHandler.instance.getCommands();

                ((CommandHandler) commands.get(oldAlias)).setInactive(); // quite presumptious...
            }
        }
    }

    void aliasHelp(ICommandSender iCommandSender) {
        iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "Alias usage:"));
        iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + ""));
        iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "/alias list"));
        iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "    lists defined aliases"));
        iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "/alias add <alias> <command>:"));
        iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "    e.g. /alias add boo /me says boo!!!"));
        iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "/alias remove <alias>:"));
        iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "    e.g. /alias remove boo"));
        iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "/alias verbose"));
        iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "    toggles verboseness of response to requests"));
        iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "/alias enable/disable"));
        iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "    enables/disables all alias commands"));
        iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "/alias help"));
        iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "    this!"));
        iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + ""));
        iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "Tip: If you want the alias to do anything other than send chat ensure that the command starts with appropriate prefix i.e. '/home' and not just 'home'"));

    }

    void aliasToggleVerbose(ICommandSender iCommandSender) {
        if(isVerbose()) {
            config.writeConfig("settings", "verbose", false);
            iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "verbose messaging enabled"));
        } else {
            config.writeConfig("settings", "verbose", true);
        }
    }

    boolean isVerbose() {
        if(config.hasKey("settings", "verbose")) {
            return config.getBoolean("settings", "verbose");
        } else {
            return true;
        }
    }

    void aliasEnable(ICommandSender iCommandSender) {
        config.writeConfig("settings", "enabled", true);
        if(isVerbose()) {
            iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "aliases enabled"));
        }
    }

    void aliasDisable(ICommandSender iCommandSender) {
        config.writeConfig("settings", "enabled", false);
        if(isVerbose()) {
            iCommandSender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "aliases disabled"));
        }
    }

    boolean isEnabled() {
        if(config.hasKey("settings", "enabled")) {
            return config.getBoolean("settings", "enabled");
        } else {
            return true;
        }
    }


}
