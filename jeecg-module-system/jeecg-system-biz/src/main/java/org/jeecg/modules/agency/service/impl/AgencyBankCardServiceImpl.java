package org.jeecg.modules.agency.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.agency.dto.AgencyBankCardDTO;
import org.jeecg.modules.agency.entity.AgencyBankCard;
import org.jeecg.modules.agency.mapper.AgencyBankCardMapper;
import org.jeecg.modules.agency.service.IAgencyBankCardService;
import org.jeecg.modules.agency.vo.AgencyBankCardVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 代理银行卡
 * @Author: jeecg-boot
 * @Date:   2020-03-05
 * @Version: V1.0
 */
@Service
public class AgencyBankCardServiceImpl extends ServiceImpl<AgencyBankCardMapper, AgencyBankCard> implements IAgencyBankCardService {
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 新增
     * @param agencyBankCard
     * @return
     */
    @Override
    public Result<AgencyBankCard> add(AgencyBankCard agencyBankCard) {
        Result<AgencyBankCard> result = new Result<AgencyBankCard>();
        try {
            this.save(agencyBankCard);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑
     * @param agencyBankCard
     * @return
     */
    @Override
    public Result<AgencyBankCard> edit(AgencyBankCard agencyBankCard) {
        Result<AgencyBankCard> result = new Result<AgencyBankCard>();
        AgencyBankCard agencyBankCardEntity = this.getById(agencyBankCard.getId());
        if(agencyBankCardEntity==null) {
            result.error500("未找到对应实体");
        }else {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            boolean ok = this.updateById(agencyBankCard
                    .setUpdateBy(sysUser.getUsername())
                    .setUpdateTime(new Date())
            );
            if(ok) {
                result.success("修改成功!");
            }
        }
        return result;
    }
    /**
     * 代理银行卡设置
     * @param agencyBankCardVO
     * @return
     */
    @Override
    public Result<AgencyBankCard> agencyBankCardAudit(AgencyBankCardVO agencyBankCardVO) {
        Result<AgencyBankCard> result = new Result<>();
        Object code = redisUtil.get(agencyBankCardVO.getPhone());
        if (!agencyBankCardVO.getSbCode().equals(code)){
            result.error500("验证码错误!");
        }else {
            AgencyBankCard agencyBankCard = new AgencyBankCard();
            BeanUtils.copyProperties(agencyBankCardVO,agencyBankCard);
            if (StringUtils.isBlank(agencyBankCard.getId())){
                LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                agencyBankCard.setSysUserId(sysUser.getId());
                return add(agencyBankCard);
            }else {
                return edit(agencyBankCard);
            }
        }
        return result;
    }

    /**
     * 代理银行卡返显
     * @return
     */
    @Override
    public Map<String, Object> findAgencyBankCard() {
        HashMap<String, Object> map = new HashMap<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        QueryWrapper<AgencyBankCard> agencyBankCardQueryWrapper = new QueryWrapper<>();
        agencyBankCardQueryWrapper.and(i->i.eq("sys_user_id",sysUser.getId()).eq("del_flag","0"));
        List<AgencyBankCard> agencyBankCards = baseMapper.selectList(agencyBankCardQueryWrapper);
        for (AgencyBankCard agencyBankCard : agencyBankCards) {
            if (agencyBankCard.getCarType().equals("0")){
                map.put("bankCard",agencyBankCard);
            }else if (agencyBankCard.getCarType().equals("1")){
                map.put("aliPay",agencyBankCard);
            }else {
                return null;
            }
        }
        return map;
    }

    @Override
    public List<AgencyBankCardDTO> findBankCardById(String sysUserId) {
        return baseMapper.findBankCardById(sysUserId);
    }

    @Override
    public IPage<AgencyBankCardVO> queryPageList(Page<AgencyBankCardVO> page, AgencyBankCardDTO agencyBankCardDTO) {
        return baseMapper.queryPageList(page,agencyBankCardDTO);
    }
}
