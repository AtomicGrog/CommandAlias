package com.grogware.commandalias;

import com.grogware.commandalias.commandhandlers.CommandHandler;
import com.grogware.commandalias.commandhandlers.ExecCommandHandler;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.client.ClientCommandHandler;
import com.grogware.commandalias.config.ConfigHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;
import java.util.Set;

/**
 * @author AtomicGrog
 */

@Mod(modid= CommandAlias.MODID, name= CommandAlias.MODNAME, clientSideOnly=true, acceptedMinecraftVersions = "[1.12.2,1.13)")

public class CommandAlias {
    public static final String MODID="commandalias";
    public static final String MODNAME="command alias";
    private static ConfigHandler config;

    public static void syncConfig() {
//        ConfigHandler.COMMANDS = COMMANDS.getStringList();
//        if(config.hasChanged())
//            config.save();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){

        config = new ConfigHandler();
        config.init("config/CommandAlias.cfg");

        if (event.getSide() == Side.CLIENT) {

        //setup the 'alias' command as a exec command (i.e. cannot be removed)
        ClientCommandHandler.instance.registerCommand(new ExecCommandHandler("alias"));


            // System.out.println("***** READING CONFIG!!! *****");
        //Iterate though the aliases each will be part of an alias section, names dont matter, context for each is alias=command
        Set<Map.Entry<String, Property>> aliases = config.getCategoryContent("aliases");

        if(aliases != null) {
            for (Map.Entry<String, Property> entry : aliases) {
                System.out.println(entry.getKey() + ":" + entry.getValue().getString());
                ClientCommandHandler.instance.registerCommand(new CommandHandler(entry.getKey().toString(), entry.getValue().getString()));
            }
        }

        // Loading default commands + users defined aliases;

        // Default;
        //    ClientCommandHandler.instance.registerCommand(new CommandHandler("alias"));
        // User defined
        }

 //       syncConfig();
    }
}
