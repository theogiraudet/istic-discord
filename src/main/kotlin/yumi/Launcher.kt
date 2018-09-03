package yumi

import mu.KotlinLogging
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import org.trapgirls.kjda.*
import org.trapgirls.kjda.builders.colorAwt
import org.trapgirls.kjda.builders.embed
import yumi.config.gatherConfig
import java.awt.Color


var logger = KotlinLogging.logger { }

fun main(args: Array<String>) {
    val config = gatherConfig(args[0])
    client(AccountType.BOT) {
        token { config.token }
        game { "Version de développement" }
        status { OnlineStatus.ONLINE }
        this += Runner()
    }
}

class Runner : ListenerAdapter() {

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent?) {
        event!!.member.user.openPrivateChannel().queue {
            it.sendMessage(embed {
                title = "Bienvenue sur le serveur non officiel de l'ISTIC !"
                description = StringBuilder("Pour obtenir l'accès complet à votre promotion, aller dans le salon accueil")
                colorAwt = Color.decode("0x01B8EA")
            }).queue()
        }

    }
}
