package com.example.demo.Servise;

import java.time.Duration;
import java.util.Locale;
import java.util.Map;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.AuthDTOS;
import com.example.demo.Entity.UsersEntity;
import com.example.demo.rep.usersRep;

@Service
public class RedisSerivce {

    private static final Duration USER_TTL = Duration.ofDays(2);

    private final StringRedisTemplate redis;
    private final JWT_UTILS jwt;
    private final usersRep rep;

    public RedisSerivce(StringRedisTemplate redis, JWT_UTILS jwt,usersRep rep) {
        this.redis = redis;
        this.jwt = jwt;
        this.rep=rep;
    }
  

    public String register(AuthDTOS user) {
        try{
        if (user == null|| user.name == null || user.name.isBlank()|| user.password == null || user.password.isBlank() || user.mail == null || user.mail.isBlank()) {
            throw new IllegalArgumentException("BAD_REQUEST");
        }

        String mail = user.mail.trim().toLowerCase(Locale.ROOT);
        String key = userKey(mail);

        if (Boolean.TRUE.equals(redis.hasKey(key))) {
            throw new IllegalStateException("GONE");
        }
        redis.opsForHash().putAll(key, Map.of("name", user.name.trim(),"mail", mail,"password", user.password));
        redis.expire(key, USER_TTL);
            
        save_user(user);
        return "Create"+jwt.generalToken(user.name);

        }catch(Exception e){
            return "error"+e;
        }

    }

  
    public String login(AuthDTOS dto) {
        UsersEntity finded_user = rep.findByUsername(dto.name);
		String token = jwt.generalToken(dto.name);

		if (finded_user == null) {
			return "Forbiden";
		} else {
			if (dto.password.equals(finded_user.getPassword())) {
				return token;
			} else {
				return "Forbiden";
			}

		}
    }

    public String getUserName(String mail) {
        if (mail == null || mail.isBlank()) {
            throw new IllegalArgumentException("Mail is required");
        }

        Object name = redis.opsForHash().get(userKey(mail), "name");

        if (name == null) {
            throw new IllegalArgumentException("User not found");
        }

        return name.toString();
    }


    public String getTtl(String mail) {
        Long ttl = redis.getExpire(userKey(mail));

        return String.valueOf(ttl == null ? -1L : ttl);
    }

    private String userKey(String mail) {
        return "user:" + mail.trim().toLowerCase(Locale.ROOT);
    }
     public void save_user(AuthDTOS user){
        UsersEntity saveObj=new UsersEntity();
        saveObj.setUsername(user.name);
        saveObj.setPassword(user.password);
        saveObj.setEmail(user.mail);
        rep.save(saveObj);
    } 


}