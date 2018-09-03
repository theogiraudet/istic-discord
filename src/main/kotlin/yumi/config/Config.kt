package yumi.config

import arrow.core.Either
import arrow.core.flatMap
import com.beust.klaxon.Klaxon
import yumi.logger
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

data class Config(
        val token: String,
        val ownerID: String?
)

sealed class Error {
    object FileNotFound : Error()
    object IncorrectJson : Error()
    object IncorrectToken : Error()
    object IncorrectOwnerId : Error()
}

fun fileToString(path: Path): Either<Error, String> =
        if (Files.exists(path)) Either.Right(Files.lines(path).reduce { str1, str2 -> str1 + "\n" + str2 }.get())
        else Either.Left(Error.FileNotFound)

fun readJsonConfig(json: String): Config? = Klaxon().parse<Config>(json)

fun isConfigRead(config: Config?): Either<Error, Config> =
        if (config != null) Either.Right(config)
        else Either.left(Error.IncorrectJson)

fun isFieldsCorrect(config: Config): Either<Error, Config> {
    return when {
        config.token.length < 58 -> Either.left(Error.IncorrectToken)
        config.ownerID?.length != 18 -> Either.left(Error.IncorrectOwnerId)
        else -> Either.right(config)
    }
}

fun fileToConfig(path: Path): Either<Error, Config> =
        fileToString(path)
                .map { readJsonConfig(it) }
                .flatMap { isConfigRead(it) }
                .flatMap { isFieldsCorrect(it) }

fun gatherConfig(path: String): Config {
    val configEither = fileToConfig(Paths.get(path))
    when (configEither) {
        is Either.Left -> when (configEither.a) {
            is Error.FileNotFound -> logger.error { "File not found" }
            is Error.IncorrectJson -> logger.error { "Unable to read Json file" }
            is Error.IncorrectToken -> logger.error { "Incorrect Token, token must be 58 char long or more" }
            is Error.IncorrectOwnerId -> logger.error { "Incorrect OwnerId, ownerId must be 18 char long" }
        }
        is Either.Right -> {
            logger.info { "Config file validated" }
            return configEither.b
        }
    }
    throw Exception("Unable to read Config file")
}

