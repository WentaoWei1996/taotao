package com.taotao.content.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.jedis.JedisClient;
import com.taotao.content.service.ContentService;
import com.taotao.content.service.util.JsonUtils;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author wwt
 * @date 2019/9/20 16:46
 */
@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private JedisClient client;

    @Autowired
    TbContentMapper contentMapper;

    /*@Value("${CONTENT_KEY}")
    private String CONTENT_KEY;*/

    @Override
    public EasyUIDataGridResult getContentList(Long id, int page, int rows) {
        if (page==0)page=1;
        if (rows==0)rows=30;
        PageHelper.startPage(page,rows);

        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(id);

        List<TbContent> list = contentMapper.selectByExample(example);

        PageInfo<TbContent> pageInfo = new PageInfo<>(list);

        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(pageInfo.getList());

        return result;
    }

    @Override
    public TaotaoResult saveContent(TbContent content) {

        content.setCreated(new Date());
        content.setUpdated(content.getCreated());

        contentMapper.insertSelective(content);

        try {
            //当添加内容时需要清空此内容所属的分类下的所有缓存
            client.hdel("TBCONTENT_KEY",content.getCategoryId()+"");
            System.out.println("clean when insert");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult editContent(TbContent content) {
        contentMapper.updateByPrimaryKeySelective(content);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteContent(List<Long> ids) {
        /*System.out.println(id.length);
        for (int i = 0; i < id.length; i++) {
            contentMapper.deleteByPrimaryKey(id[i]);
        }*/
        for (Long id :ids){
            contentMapper.deleteByPrimaryKey(id);
        }
        return TaotaoResult.ok();
    }

    @Override
    public List<TbContent> getContentListByCatId(Long categoryId) {

        //添加缓存不能影响正常的业务逻辑

        //判断是否redis中有数据，如果有，直接从redis中获取数据返回

        try {
            String jsonstr = client.hget("TBCONTENT_KEY", categoryId + "");//从redis中获取内容分类下的所有内容

            //如果存在，说明有缓存
            if (StringUtils.isNotBlank(jsonstr)){
                System.out.println("yes!!!");
                return JsonUtils.jsonToList(jsonstr,TbContent.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        TbContentExample example = new TbContentExample();
        example.createCriteria().andCategoryIdEqualTo(categoryId);

        List<TbContent> list = contentMapper.selectByExample(example);

        //将数据写入到redis中
        //注入jedisclient
        //调用方法写入redis  key - value
        try {
            System.out.println("no!!!");
            client.hset("TBCONTENT_KEY",categoryId+"",JsonUtils.objectToJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
