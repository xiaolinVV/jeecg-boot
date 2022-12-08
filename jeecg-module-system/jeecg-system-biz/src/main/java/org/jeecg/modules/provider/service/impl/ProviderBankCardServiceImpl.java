package org.jeecg.modules.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.provider.dto.ProviderBankCardDTO;
import org.jeecg.modules.provider.entity.ProviderBankCard;
import org.jeecg.modules.provider.mapper.ProviderBankCardMapper;
import org.jeecg.modules.provider.service.IProviderBankCardService;
import org.jeecg.modules.provider.vo.ProviderBankCardVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 供应商银行卡
 * @Author: jeecg-boot
 * @Date: 2020-01-04
 * @Version: V1.0
 */
@Service
public class ProviderBankCardServiceImpl extends ServiceImpl<ProviderBankCardMapper, ProviderBankCard> implements IProviderBankCardService {
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public Result<ProviderBankCard> updateOrSaveBankCard(ProviderBankCardVO providerBankCardVO) {
        Result<ProviderBankCard> result = new Result<>();
        //获取验证码
        Object code = redisUtil.get(providerBankCardVO.getPhone());
        //判断验证码
        if (!providerBankCardVO.getSbCode().equals(code)) {
            result.error500("验证码错误!");
        } else {
            //获取当前登录人
            ProviderBankCard providerBankCard = new ProviderBankCard();
            BeanUtils.copyProperties(providerBankCardVO, providerBankCard);
            //判断前端传递过来的参数id是否为空
            if (StringUtils.isBlank(providerBankCardVO.getId())) {
                LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
                providerBankCard.setSysUserId(sysUser.getId());
                return add(providerBankCard);
            } else {
                return edit(providerBankCard);
            }
        }
        return result;
    }

    /**
     * 新增
     * @param providerBankCard
     * @return
     */
    @Override
    public Result<ProviderBankCard> add(ProviderBankCard providerBankCard) {
        Result<ProviderBankCard> result = new Result<ProviderBankCard>();
        try {
            this.save(providerBankCard);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑
     * @param providerBankCard
     * @return
     */
    @Override
    public Result<ProviderBankCard> edit(ProviderBankCard providerBankCard) {
        Result<ProviderBankCard> result = new Result<ProviderBankCard>();
        ProviderBankCard providerBankCardEntity = this.getById(providerBankCard.getId());
        if (providerBankCardEntity == null) {
            result.error500("未找到对应实体");
        } else {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            boolean ok = this.updateById(providerBankCard
                    .setUpdateBy(sysUser.getUsername())
                    .setUpdateTime(new Date())
            );
            if (ok) {
                result.success("修改成功!");
            }
        }

        return result;
    }

    /**
     * 提现信息返显
     * @return
     */
    @Override
    public Map<String, Object> returnBankCard() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        QueryWrapper<ProviderBankCard> providerBankCardQueryWrapper = new QueryWrapper<>();
        providerBankCardQueryWrapper.eq("sys_user_id", sysUser.getId());
        List<ProviderBankCard> list = this.list(providerBankCardQueryWrapper);
        HashMap<String, Object> map = new HashMap<>();
        for (ProviderBankCard providerBankCard : list) {
            if ("1".equals(providerBankCard.getCarType())){
                map.put("aliPay",providerBankCard);
            }else if ("0".equals(providerBankCard.getCarType())){
                map.put("bankCard",providerBankCard);
            }else {
                return null;
            }
        }
        return map;
    }

    @Override
    public IPage<ProviderBankCardVO> queryPageList(Page<ProviderBankCardVO> page, ProviderBankCardDTO providerBankCardDTO) {
        return baseMapper.queryPageList(page,providerBankCardDTO);
    }
}
