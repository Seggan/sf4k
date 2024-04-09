package io.github.seggan.sf4k.serial.serializers

import kotlinx.serialization.SerializationException
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.UUID

object PlayerSerializer : DelegatingSerializer<Player, UUID>(
    UUIDSerializer,
    Player::class.java.simpleName
) {
    override fun toData(value: Player): UUID = value.uniqueId
    override fun fromData(value: UUID): Player = Bukkit.getPlayer(value)
        ?: throw SerializationException("Player with UUID $value not found")
}

object OfflinePlayerSerializer : DelegatingSerializer<OfflinePlayer, UUID>(
    UUIDSerializer,
    OfflinePlayer::class.java.simpleName
) {
    override fun toData(value: OfflinePlayer): UUID = value.uniqueId
    override fun fromData(value: UUID): OfflinePlayer = Bukkit.getOfflinePlayer(value)
}