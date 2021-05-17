package com.program.moist.control.manage;

import com.program.moist.service.InfoService;
import com.program.moist.service.SearchService;
import com.program.moist.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Date: 2021/5/10
 * Author: SilentSherlock
 * Description: describe the class features
 */
@RestController
@RequestMapping("/manage")
@Slf4j
public class SearchManageController {

    @Resource
    private SearchService searchService;
    @Resource
    private InfoService infoService;
    /**
     * 创建info的索引文件
     * @param cateName 文件夹名字,一般是类别名,用作针对性的搜索,也可以是all,用作全部info的检索
     * @param cateId cateName为all时，cate_id值无效
     * @return
     */
    @RequestMapping("/createInfoIndex")
    public Result createInfoIndex(String cateName, Integer cateId) {
        if ("all".equals(cateName)) {
            searchService.createIndexForInfo(infoService.getAllInfo(), null);
        } else {
            HashMap<String, Object> params = new HashMap<>();
            params.put("cateId", cateId);
            searchService.createIndexForInfo(infoService.getInfoByMap(params), cateName);
        }
        return Result.createBySuccess();
    }
}
