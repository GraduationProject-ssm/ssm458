
package com.controller;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import com.alibaba.fastjson.JSONObject;
import java.util.*;
import org.springframework.beans.BeanUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.ContextLoader;
import javax.servlet.ServletContext;
import com.service.TokenService;
import com.utils.*;
import java.lang.reflect.InvocationTargetException;

import com.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import com.annotation.IgnoreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.*;
import com.entity.view.*;
import com.service.*;
import com.utils.PageUtils;
import com.utils.R;
import com.alibaba.fastjson.*;

/**
 * 实验室
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/shiyanshi")
public class ShiyanshiController {
    private static final Logger logger = LoggerFactory.getLogger(ShiyanshiController.class);

    @Autowired
    private ShiyanshiService shiyanshiService;


    @Autowired
    private TokenService tokenService;
    @Autowired
    private DictionaryService dictionaryService;

    //级联表service

    @Autowired
    private XueshengService xueshengService;
    @Autowired
    private LaoshiService laoshiService;


    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(StringUtil.isEmpty(role))
            return R.error(511,"权限为空");
        else if("学生".equals(role))
            params.put("xueshengId",request.getSession().getAttribute("userId"));
        else if("老师".equals(role))
            params.put("laoshiId",request.getSession().getAttribute("userId"));
        if(params.get("orderBy")==null || params.get("orderBy")==""){
            params.put("orderBy","id");
        }
        PageUtils page = shiyanshiService.queryPage(params);

        //字典表数据转换
        List<ShiyanshiView> list =(List<ShiyanshiView>)page.getList();
        for(ShiyanshiView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
        }
        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        ShiyanshiEntity shiyanshi = shiyanshiService.selectById(id);
        if(shiyanshi !=null){
            //entity转view
            ShiyanshiView view = new ShiyanshiView();
            BeanUtils.copyProperties( shiyanshi , view );//把实体数据重构到view中

            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody ShiyanshiEntity shiyanshi, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,shiyanshi:{}",this.getClass().getName(),shiyanshi.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(StringUtil.isEmpty(role))
            return R.error(511,"权限为空");

        Wrapper<ShiyanshiEntity> queryWrapper = new EntityWrapper<ShiyanshiEntity>()
            .eq("shiyanshi_uuid_number", shiyanshi.getShiyanshiUuidNumber())
            .eq("shiyanshi_weizhi", shiyanshi.getShiyanshiWeizhi())
            .eq("shiyanshi_renshu", shiyanshi.getShiyanshiRenshu())
            .eq("shiyanshikemu_types", shiyanshi.getShiyanshikemuTypes())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        ShiyanshiEntity shiyanshiEntity = shiyanshiService.selectOne(queryWrapper);
        if(shiyanshiEntity==null){
            shiyanshi.setCreateTime(new Date());
            shiyanshiService.insert(shiyanshi);
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody ShiyanshiEntity shiyanshi, HttpServletRequest request){
        logger.debug("update方法:,,Controller:{},,shiyanshi:{}",this.getClass().getName(),shiyanshi.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(StringUtil.isEmpty(role))
//            return R.error(511,"权限为空");
        //根据字段查询是否有相同数据
        Wrapper<ShiyanshiEntity> queryWrapper = new EntityWrapper<ShiyanshiEntity>()
            .notIn("id",shiyanshi.getId())
            .andNew()
            .eq("shiyanshi_uuid_number", shiyanshi.getShiyanshiUuidNumber())
            .eq("shiyanshi_weizhi", shiyanshi.getShiyanshiWeizhi())
            .eq("shiyanshi_renshu", shiyanshi.getShiyanshiRenshu())
            .eq("shiyanshikemu_types", shiyanshi.getShiyanshikemuTypes())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        ShiyanshiEntity shiyanshiEntity = shiyanshiService.selectOne(queryWrapper);
        if(shiyanshiEntity==null){
            shiyanshiService.updateById(shiyanshi);//根据id更新
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        shiyanshiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save( String fileName){
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}",this.getClass().getName(),fileName);
        try {
            List<ShiyanshiEntity> shiyanshiList = new ArrayList<>();//上传的东西
            Map<String, List<String>> seachFields= new HashMap<>();//要查询的字段
            Date date = new Date();
            int lastIndexOf = fileName.lastIndexOf(".");
            if(lastIndexOf == -1){
                return R.error(511,"该文件没有后缀");
            }else{
                String suffix = fileName.substring(lastIndexOf);
                if(!".xls".equals(suffix)){
                    return R.error(511,"只支持后缀为xls的excel文件");
                }else{
                    URL resource = this.getClass().getClassLoader().getResource("static/upload/" + fileName);//获取文件路径
                    File file = new File(resource.getFile());
                    if(!file.exists()){
                        return R.error(511,"找不到上传文件，请联系管理员");
                    }else{
                        List<List<String>> dataList = PoiUtil.poiImport(file.getPath());//读取xls文件
                        dataList.remove(0);//删除第一行，因为第一行是提示
                        for(List<String> data:dataList){
                            //循环
                            ShiyanshiEntity shiyanshiEntity = new ShiyanshiEntity();
//                            shiyanshiEntity.setShiyanshiUuidNumber(data.get(0));                    //实验室编号 要改的
//                            shiyanshiEntity.setShiyanshiWeizhi(data.get(0));                    //所在位置 要改的
//                            shiyanshiEntity.setShiyanshiRenshu(Integer.valueOf(data.get(0)));   //容纳人数 要改的
//                            shiyanshiEntity.setShiyanshikemuTypes(Integer.valueOf(data.get(0)));   //实验科目 要改的
//                            shiyanshiEntity.setShiyanshiContent("");//照片
//                            shiyanshiEntity.setCreateTime(date);//时间
                            shiyanshiList.add(shiyanshiEntity);


                            //把要查询是否重复的字段放入map中
                                //实验室编号
                                if(seachFields.containsKey("shiyanshiUuidNumber")){
                                    List<String> shiyanshiUuidNumber = seachFields.get("shiyanshiUuidNumber");
                                    shiyanshiUuidNumber.add(data.get(0));//要改的
                                }else{
                                    List<String> shiyanshiUuidNumber = new ArrayList<>();
                                    shiyanshiUuidNumber.add(data.get(0));//要改的
                                    seachFields.put("shiyanshiUuidNumber",shiyanshiUuidNumber);
                                }
                        }

                        //查询是否重复
                         //实验室编号
                        List<ShiyanshiEntity> shiyanshiEntities_shiyanshiUuidNumber = shiyanshiService.selectList(new EntityWrapper<ShiyanshiEntity>().in("shiyanshi_uuid_number", seachFields.get("shiyanshiUuidNumber")));
                        if(shiyanshiEntities_shiyanshiUuidNumber.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(ShiyanshiEntity s:shiyanshiEntities_shiyanshiUuidNumber){
                                repeatFields.add(s.getShiyanshiUuidNumber());
                            }
                            return R.error(511,"数据库的该表中的 [实验室编号] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                        shiyanshiService.insertBatch(shiyanshiList);
                        return R.ok();
                    }
                }
            }
        }catch (Exception e){
            return R.error(511,"批量插入数据异常，请联系管理员");
        }
    }






}
