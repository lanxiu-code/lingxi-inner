package com.lingxi.lingxibackend.websocket.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

/**
 * Description: jwt的token生成与解析
 */
@Slf4j
@Component
public class JwtUtils {

    /**
     * token秘钥，请勿泄露，请勿随便修改
     */
    @Value("${jwt.secret}")
    private String secret;

    private static final String UID_CLAIM = "uid";
    private static final String CREATE_TIME = "createTime";

    /**
     * JWT生成Token.<br/>
     * <p>
     * JWT构成: header, payload, signature
     */
    public String createToken(Long uid) {
        final JWTSigner signer = JWTSignerUtil.hs256(secret.getBytes());
        Date date = new Date();
        String token = JWT.create()
                // 生效时间
                .setNotBefore(date)
                // 失效时间 1小时
                .setExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .setKey(uid.toString().getBytes())
                .setPayload(UID_CLAIM, date)
                .setPayload(UID_CLAIM, uid)
                .setSigner(signer)
                .sign();
        return token;
    }

    /**
     * 解密Token
     *
     * @param token
     * @return
     */
    public Boolean verifyToken(String token) {
        final JWTSigner signer = JWTSignerUtil.hs256(secret.getBytes());
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        try {
            JWTValidator.of(token).validateDate(DateUtil.date());
            return JWTUtil.verify(token, signer);
        } catch (Exception e) {
            log.error("decode error,token:{}", token, e);
        }
        return null;
    }
    /**
     * 根据Token获取uid
     *
     * @param token
     * @return uid
     */
    public Long getUidOrNull(String token) {
        final JWT jwt = JWTUtil.parseToken(token);
        String uid = jwt.getPayload(UID_CLAIM).toString();
        return Optional.ofNullable(uid)
                .map(Long::valueOf)
                .orElse(null);
    }

}
