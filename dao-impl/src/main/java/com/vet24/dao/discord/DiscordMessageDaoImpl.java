package com.vet24.dao.discord;


import com.vet24.dao.ReadWriteDaoImpl;
import com.vet24.models.discord.DiscordMessage;
import org.springframework.stereotype.Repository;

@Repository
public class DiscordMessageDaoImpl extends ReadWriteDaoImpl<Long, DiscordMessage> implements DiscordMessageDao {

}