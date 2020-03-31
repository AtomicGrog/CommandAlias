*** Only just started the port from 1.12.2... right now this isnt even viable code, dont use it for any MC version ***

A refactored from MC 1.12.* to 1.15.2 mod that allows command aliases to be created i.e: "/samplecommand" can be defined to type any command or text sequence as though the player had done it themselves.

I developed this initially because I use a lot of "/home xxx" type commands in the packs I play and it would be nice simpify i.e.

"/ae2" as an alias of "/home ae2"
and
"/nether" as an alias of "/home nether"

Having done that it became apparent that additional aliases are nice:

"/day" as an alias of "/time set day" and so on.

I doubt this mod is unique, its probably a subset of abilities in other mods but to date i've never actually made a mod (i've fixed plenty...) and never seen these features.

Am aware that some might see this as exploitable. Expectially in pvp where having shortcuts would be an advantage. To that i'd say that this would probably be the least of your worries, if a server was to allow clients to add mods this would be a pretty safe one compared to what could be used to cheat in pvp.

Note(s):
-----

The commands you alias follow MC chat syntax i.e. if you want to perform an actual command rather than just type in chat then you need to pre-fix it with the / yourself.

Other potential issues at this point:
-------------------------------------

It's likely that with a lack of checks that commands/aliases could be added that cant effectively be stored in a json config file. Nothing at this stage will stop you using json markup characters etc, and result could be a corrupt config file.

This is kinda pre-alpha... no real testing to date. But its lightweight and developed for fun. If you dont like it or it causes problems i'd simply suggest you delete it.
