package com.guanqing.commontools.huaban;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple PageProcessor.
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
public class SougouImgProcessor {

    private String url;
    private SougouImgPipeline pipeline;
    private List<JSONObject> dataList;
    private List<String> urlList;
    private String word;

    public SougouImgProcessor(String url,String word) {
        this.url = url;
        this.word = word;
        this.pipeline = new SougouImgPipeline();
        this.dataList = new ArrayList<>();
        this.urlList = new ArrayList<>();
    }

    public void process(int idx) {
        String res = HttpClientUtils.get(String.format(this.url, this.word, idx));
        JSONObject object = JSONObject.parseObject(res);
        List<JSONObject> items = (List<JSONObject>)((JSONObject)object.get("pins"));
        for(JSONObject item : items){
            this.urlList.add(item.getJSONObject("file").getString("key"));
        }
        this.dataList.addAll(items);
    }

    // 下载
    public void pipelineData(){
//        pipeline.process(this.urlList, word);   // 单线程
        pipeline.processSync(this.urlList, this.word);    // 多线程

    }


    public static void main(String[] args) {
//        String url = "https://pic.sogou.com/napi/pc/searchList?mode=1&start=%s&xml_len=%s&query=%s";
        String url = "https://huaban.com/search/?q=%s&kt7475no&page=%s&per_page=20&wfl=1";
        SougouImgProcessor processor = new SougouImgProcessor(url,"嫦娥素材");

        int page = 0, totalPage=5; // 定义爬取开始索引、每次爬取数量、总共爬取数量

        for(int i=page;i<totalPage;i++)
            processor.process(i);

        processor.pipelineData();

    }

}