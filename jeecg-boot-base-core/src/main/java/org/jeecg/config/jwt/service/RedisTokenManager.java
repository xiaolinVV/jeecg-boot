package org.jeecg.config.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.jeecg.config.jwt.def.JwtConstants;
import org.jeecg.config.jwt.model.TokenModel;
import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.wxbase.wxtoken.JwTokenAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 通过Redis存储和验证token的实现类
 * @author ScienJus
 * @date 2015/7/31.
 */
@Component
public class RedisTokenManager implements TokenManager {
	@Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 生成TOKEN
     */
    @Override
    public String createToken(String memberId,String softModel) {
        //使用uuid作为源token
        String token = Jwts.builder().setId(memberId).setSubject(memberId).setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, JwtConstants.JWT_SECRET).compact();
        //存储到redis并设置过期时间
        redisTemplate.boundValueOps(softModel+"member="+memberId).set(token, JwtConstants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return token;
    }

    @Override
    public TokenModel getToken(String token,String memberId) {
        return new TokenModel(memberId, token,"");
    }

    @Override
    public boolean checkToken(TokenModel model,String softModel) {
        if (model == null) {
            return false;
        }
        String token = (String) redisTemplate.boundValueOps(softModel+"member="+model.getMemberId()).get();
        if (token == null || !token.equals(model.getToken())) {
            return false;
        }
        //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        redisTemplate.boundValueOps(softModel+"member="+model.getMemberId()).expire(JwtConstants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return true;
    }

    @Override
    public void deleteToken(String memberId,String softModel) {
        redisTemplate.delete(softModel+"member="+memberId);
    }

    @Override
    public String createWeiXinToken(String appid,String appscret) {
        //获取微信token
        String token = null;
        token = (String) redisTemplate.boundValueOps("weixin="+appid).get();
        if (token == null) {
            try {
                token = JwTokenAPI.getAccessToken(appid,appscret);
            } catch (WexinReqException e) {
                e.printStackTrace();
            }
            //存储到redis并设置过期时间
            redisTemplate.boundValueOps("weixin="+appid).set(token, JwtConstants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        }
        return token;
    }

    @Override
    public boolean checkWeiXinToken(TokenModel model){
        if (model == null) {
            return false;
        }
        String token = (String) redisTemplate.boundValueOps("weixin="+model.getAppid()).get();
        if (token == null || !token.equals(model.getToken())) {
            return false;
        }
        return true;
    }

    @Override
    public TokenModel getWeiXinToken(String token, String appid) {
        return new TokenModel("", token,appid);
    }

    @Override
    public void deleteWeiXinToken(String appid) {
        redisTemplate.delete("weixin="+appid);
    }

    @Override
    public Object getMemberIdByToken(String token) {
        // 验证token
        Claims claims = null;
        claims = Jwts.parser().setSigningKey(JwtConstants.JWT_SECRET).parseClaimsJws(token).getBody();
        return claims.getId();
    }
}
