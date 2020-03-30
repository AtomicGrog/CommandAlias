package grogware.commandalias;

import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.client.ClientCommandHandler;
import grogware.commandalias.config.ConfigHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;
import java.util.Set;

/**
 * @author AtomicGrog
 */

@Mod(modid= CommandAlias.MODID, name= CommandAlias.MODNAME, clientSideOnly=true, guiFactory="grogware.commandalias.config.CommBindGuiFactory", acceptedMinecraftVersions = "[1.12.2,1.13)")

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

        System.out.println("***** READING CONFIG!!! *****");
        //Iterate though the aliases each will be part of an alias section, names dont matter, context for each is alias=command
        Set<Map.Entry<String, Property>> aliases = config.getCategoryContent("aliases");

        if(aliases != null) {
            for (Map.Entry<String, Property> entry : aliases) {
                System.out.println(entry.getKey() + ":" + entry.getValue().getString());
                ClientCommandHandler.instance.registerCommand(new CommandHandler(entry.getKey().toString(), entry.getValue().getString()));
            }
        } else {
            System.out.println("******  Aliases was null!!!! ********");
            config.writeConfig("aliases", "home","help");
        }

        // Loading default commands + users defined aliases;

        // Default;
        //    ClientCommandHandler.instance.registerCommand(new CommandHandler("alias"));
        // User defined
          ClientCommandHandler.instance.registerCommand(new CommandHandler("boo", "/me says boo"));
        }

 //       syncConfig();
    }
}
