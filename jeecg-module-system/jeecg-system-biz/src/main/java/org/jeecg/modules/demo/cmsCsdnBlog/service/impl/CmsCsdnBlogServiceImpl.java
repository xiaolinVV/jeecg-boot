package org.jeecg.modules.demo.cmsCsdnBlog.service.impl;

import org.jeecg.modules.demo.cmsCsdnBlog.entity.CmsCsdnBlog;
import org.jeecg.modules.demo.cmsCsdnBlog.mapper.CmsCsdnBlogMapper;
import org.jeecg.modules.demo.cmsCsdnBlog.service.ICmsCsdnBlogService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



/**
 * @Description: csdn博客表
 * @Author: jeecg-boot
 * @Date:   2022-10-09
 * @Version: V1.0
 */
@Service("cmsCsdnBlogService")
public class CmsCsdnBlogServiceImpl extends ServiceImpl<CmsCsdnBlogMapper, CmsCsdnBlog> implements ICmsCsdnBlogService  {

}
