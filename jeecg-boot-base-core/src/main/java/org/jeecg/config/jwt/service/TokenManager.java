package org.jeecg.config.jwt.service;


import org.jeecg.config.jwt.model.TokenModel;

/**
 * 对token进行操作的接口
 * @author ScienJus
 * @date 2015/7/31.
 */
public interface TokenManager {

    /**
     * 创建一个token关联上指定用户
     * @return 生成的token
     */
    public String createToken(String memberId,String softModel);

    /**
     * 检查token是否有效
     * @param model token
     * @return 是否有效
     */
    public boolean checkToken(TokenModel model,String softModel);

    /**
     * 从字符串中解析token
     * @return
     */
    public TokenModel getToken(String token, String memberId);

    /**
     * 清除token
     */
    public void deleteToken(String memberId,String softModel);

    /**
     * 创建一个token关联上指定用户
     * @return 生成的token
     */
    public String createWeiXinToken(String appid,String appscret);

    /**
     * 检查token是否有效
     * @return 是否有效
     */
    public boolean checkWeiXinToken(TokenModel model);

    /**
     * 从字符串中解析token
     * @return
     */
    public TokenModel getWeiXinToken(String token, String appid);

    /**
     * 清除token
     */
    public void deleteWeiXinToken(String appid);


    public Object getMemberIdByToken(String token);

}
