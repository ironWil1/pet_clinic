package com.vet24.dao.discord;


import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.discord.DiscordMessage;
import org.springframework.stereotype.Repository;

@Repository
public class DiscordMessageDaoImpl extends ReadWriteDaoImpl<Long, DiscordMessage> implements DiscordMessageDao {

    @Override
    public void deleteByDiscordMessageId(Long discordMessageId) {
        manager.createQuery("delete from DiscordMessage dMesssage where dMesssage.discordMsgId = :discordMsgId")
                .setParameter("discordMsgId", discordMessageId)
                .executeUpdate();
    }

    @Override
    public DiscordMessage getByDiscordMessageId(Long discordMessageId) {
        return manager.createQuery("select dMessage from DiscordMessage dMessage " +
                        "where dMessage.discordMsgId = :discordMessageId", DiscordMessage.class)
                .setParameter("discordMessageId", discordMessageId)
                .getSingleResult();
    }
}