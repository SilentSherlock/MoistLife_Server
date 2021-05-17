package com.program.moist.control;

import com.program.moist.service.InfoService;
import com.program.moist.service.SearchService;
import com.program.moist.utils.Result;
import com.program.moist.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Date: 2021/5/9
 * Author: SilentSherlock
 * Description: describe the class features
 */
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Resource
    private SearchService searchService;
    @Resource
    private InfoService infoService;

    /**
     * 分页获取搜索结果,大小固定为10条
     * @param keyWord
     * @param cateName
     * @param pageIndex
     * @return
     */
    @RequestMapping("/searchInfoPage")
    public Result searchInfoPage(String keyWord, String cateName, Integer pageIndex) {
        List<Integer> ids = searchService.searchInfo(keyWord, cateName, pageIndex, 10);
        if (ids == null || ids.size() == 0) {
            return Result.createByNotFound("未查询到相关结果");
        } else {
            Result result = Result.createBySuccess();
            result.getResultMap().put(TokenUtil.INFOS, infoService.getInfoByIds(ids));
            return result;
        }
    }
}
