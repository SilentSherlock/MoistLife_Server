package com.program.moist.service;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
import com.mysql.cj.log.Log;
import com.program.moist.entity.infoEntities.Information;
import com.program.moist.entity.infoEntities.Post;
import com.program.moist.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 2021/5/9
 * Author: SilentSherlock
 * Description: 利用lucene实现文字检索
 */
@Service
@Slf4j
public class SearchService {

    /**
     * 创建索引文件
     * @param list 为给定的数据创建索引文件
     * @param dir 文件夹名字，可以是category或者全部，索引文件固定存放在TokenUtil.SEARCH_DIR中
     */
    public void createIndexForInfo(List<Information> list, String dir) {
        if (dir == null) dir = "all";
        //设置索引文件文件夹
        String indexFold = TokenUtil.SEARCH_DIR + dir + "/";
        File file = new File(indexFold);
        if (!file.exists()) {
            file.mkdir();
            file.setWritable(true);
        }
        log.info("信息条数 " + list.size() + file.exists() + file.toPath());
        try {
            Directory directory = FSDirectory.open(file.toPath());
            //配置分词器
            Analyzer analyzer = new MMSegAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            IndexWriter writer = new IndexWriter(directory, config);
            log.info("开始分词创建文件");
            for (Information information :
                 list) {
                String content = information.getInfoId() + TokenUtil.DIVIDE +
                        information.getInfoTitle() + TokenUtil.DIVIDE +
                        information.getDetail();//合并字段便于检索
                Document document = new Document();
                document.add(new TextField("content", content, Field.Store.YES));
                writer.addDocument(document);
            }
            writer.close();
        } catch (Exception e) {
            log.error("创建路径" + dir + "出错", e);
        }
    }

    public void createIndexForPost(List<Post> list, String dir) {
        //设置索引文件文件夹
        String indexFold = TokenUtil.SEARCH_DIR + dir + "/";
        File file = new File(indexFold);
        if (!file.exists()) {
            file.mkdir();
        }

        try {
            Directory directory = FSDirectory.open(file.toPath());
            //配置分词器
            Analyzer analyzer = new MMSegAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            IndexWriter writer = new IndexWriter(directory, config);
            log.info("开始分词创建文件");
            for (Post post :
                    list) {
                String content = post.getPostTitle() + TokenUtil.DIVIDE + post.getDetail();//合并字段便于检索
                Document document = new Document();
                document.add(new TextField("content", content, Field.Store.YES));
                writer.addDocument(document);
            }
            writer.close();
        } catch (Exception e) {
            log.error("创建路径" + dir + "出错", e);
        }
    }

    /**
     * 分页获取搜索结果
     * @param keyWord
     * @param dir
     * @param pageIndex 最小为一
     * @param pageSize
     * @return 返回命中的info_id
     */
    public List<Integer> searchInfo(String keyWord, String dir, Integer pageIndex, Integer pageSize) {
        if (dir == null) dir = "all";
        if (pageIndex <= 0) pageIndex = 1;
        String fileDir = TokenUtil.SEARCH_DIR + dir + "/";
        File file = new File(fileDir);
        if (!file.exists()) {
            log.error("要检索的文件不存在！！！！");
            log.info("转向全部检索");
            dir = "all";
            file = new File(TokenUtil.SEARCH_DIR + dir + "/");
        }

        List<Integer> ids = new LinkedList<>();
        log.info("路径：" + file.toPath());
        try {
            Directory directory = FSDirectory.open(file.toPath());
            IndexReader indexReader = DirectoryReader.open(directory);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            Analyzer analyzer = new MMSegAnalyzer();

            //设置查询时的分词解析器
            QueryParser queryParser = new QueryParser("content", analyzer);//对content字段进行分词搜索
            Query query = queryParser.parse(keyWord);//对keyword进行搜索
            TopDocs topDocs = indexSearcher.search(query, pageIndex * pageSize);

            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            int last = Math.min((pageIndex - 1) * pageSize - 1, scoreDocs.length-1);
            ScoreDoc scoreDoc = last < 0 || pageIndex == 1 ? null : scoreDocs[last];
            topDocs = indexSearcher.searchAfter(scoreDoc, query, pageSize);


            for (ScoreDoc sc :
                    topDocs.scoreDocs) {
                Document document = indexSearcher.doc(sc.doc);
                String[] contents = document.get("content").split(TokenUtil.DIVIDE);
                ids.add(Integer.valueOf(contents[0]));
            }
        } catch (IOException | ParseException e) {
            log.error("检索info出现错误", e);
        } catch (ArrayIndexOutOfBoundsException e) {
            log.warn("已经没有数据了");
            ids = null;
        }

        return ids;
    }
}
