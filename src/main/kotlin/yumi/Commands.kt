package yumi

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import org.trapgirls.kjda.builders.colorAwt
import org.trapgirls.kjda.builders.embed
import java.awt.Color
import kotlin.reflect.KFunction
import kotlin.reflect.full.*

private const val CHAR : Char = '/'
private val CommandList : HashMap<String, KFunction<*>> = HashMap()
private var helpText : String = ""

fun initializeCommand() {
    for (member in commands::class.declaredMemberFunctions)
        if (member.findAnnotation<Command>() != null)
            CommandList.put(member.findAnnotation<Command>()?.name ?: "", member)
    val list : MutableList<String> = mutableListOf()
    for((key, value) in CommandList)
        list.add("**$key** : ${value.findAnnotation<Command>()?.description}\n")
    helpText = list.joinToString("\n")
}

class Listener : ListenerAdapter() {

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent?) {
       val message = event?.message?.contentRaw ?: return
        if(!message.startsWith(CHAR))  return
        val args : List<String> = message.split("\\s+".toRegex())
        commands.callCommands(args[0].removeRange(0, 1), args.subList(1, args.size), event)

    }
}


class Commands {
    fun callCommands(cmd: String, args: List<String>, event : GuildMessageReceivedEvent) {
        for((key, value) in CommandList)
            if(key.equals(cmd.toLowerCase()))
                value.call(this, event, args)
    }

    @Command(name = "help", description = "Affiche toutes les commandes du bot.")
    fun help(event: GuildMessageReceivedEvent, args : List<String>) {
        event.channel.sendMessage(embed {
            title = "Liste des commmandes :"
            description = StringBuilder(helpText)
            colorAwt = Color.decode("0x0000AA")
        }).queue()
    }
}

