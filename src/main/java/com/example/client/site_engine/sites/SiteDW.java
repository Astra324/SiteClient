package com.example.client.site_engine.sites;

import com.example.client.site_engine.SiteBuilder;
import com.example.client.site_engine.SiteParser;
import com.example.client.site_engine.SiteView;
import com.example.client.model.TagMap;
import com.example.client.site_engine.helpers.MyParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class SiteDW extends SiteBuilder {
    private int index = 0;

    private SiteParser catalogParser = new SiteParser() {
        private final static String linkPrefix = "https://www.dw.com";

        @Override
        public <T> T parseDoc(Document document) {
            String parsePattern = "a, h2, p, span.date, img";
            Elements allNews = document.select("div.news");

            ArrayList<TagMap> resultMap = new ArrayList<>();
            StringBuilder outHtml = new StringBuilder("");

            for (Element el : allNews) {
                Elements content = el.select(parsePattern);
                TagMap newItem = new TagMap.Builder().buildEmpty().setSiteName(getName());

                for (Element target : content) {
                    if (target.is("a")) {
                        newItem.key(linkPrefix + target.attr("href"));
                    } else if (target.is("img")) {
                        newItem.value(target.attr("src"));
                    } else if (target.is("h2")) {
                        newItem.setTitle(target.text());
                    } else if (target.is("p")) {
                        newItem.setText(target.text());
                    } else if (target.is("span.date")) {
                        newItem.setDateString(target.text());
                    }
                }
                resultMap.add(newItem);
                //System.out.println(newItem.toString());
            }
            var cleanMap = resultMap.stream().filter(e -> !e.value().isEmpty()).collect(Collectors.toList());
            setTagMaps((ArrayList<TagMap>) cleanMap);
            System.out.println(getName() + " : Catalog size : " + getTagMaps().size());
            return (T) cleanMap;
        }

        @Override
        public ArrayList<TagMap> getArticleMap() {
            return getTagMaps();
        }

        @Override
        public <T> T test(Document document, String pattern) {
            return (T) parseDoc(document);
        }
    };
    private SiteParser pageParserOld = new SiteParser() {
        private final ArrayList<TagMap> articleMap = new ArrayList<>();

        @Override
        public <T> T parseDoc(Document doc) {
            articleMap.clear();
            MyParser parser = new MyParser(Objects.requireNonNull(doc), getName());
            AtomicInteger index = new AtomicInteger(0);
            ArrayList<TagMap> pageContent = new ArrayList<>();

            Element bodyContent = doc.getElementById("bodyContent");
            Elements content = bodyContent.select("h1, p.intro, a.overlayLink:has(img), div.longText p, iframe, li:contains(????????)");
            for (Element el : content) {
                TagMap newItem = new TagMap.Builder().buildEmpty().setSiteName(getName()).setId(index.getAndIncrement());
                if (el.is("h1")) {
                    String title = "<h1>" + el.text() + "</h1>";
                    newItem.setTitle(title);
                }
                if (el.is("p.intro")) {
                    String title1 = "<h3>" + el.text() + "</h3>";
                    newItem.setText(title1);
                }
                if (el.is("a.overlayLink:has(img)")) {
                    newItem.value(el.outerHtml());
                }
                if (el.is("div.longText p")) {
                    String text = "<p>" + el.text() + "</p>";
                    newItem.setText(text);
                }
                if (el.is("iframe")) {
                    newItem.setVideo(el.outerHtml());
                }
                if (el.is("li:contains(????????)")) {
                    newItem.setDateString(el.text());
                }
                articleMap.add(newItem);
            }
            return (T) getArticleMap();
        }
        @Override
        public ArrayList<TagMap> getArticleMap() {
            return articleMap;
        }
        @Override
        public <T> T test(Document document, String pattern) {
            return (T) parseDoc(document);
        }
    };
    private SiteParser pageParser = new SiteParser() {
        private final ArrayList<TagMap> articleMap = new ArrayList<>();

        @Override
        public <T> T parseDoc(Document doc) {
            articleMap.clear();
            MyParser parser = new MyParser(Objects.requireNonNull(doc), getName());
            AtomicInteger index = new AtomicInteger(0);
            ArrayList<TagMap> pageContent = new ArrayList<>();

            Element bodyContent = doc.getElementById("root");
            Elements content = bodyContent.select("h1, p.teaser-text, img.lq-img, div.rich-text p, iframe, li:contains(????????)");
            for (Element el : content) {
                TagMap newItem = new TagMap.Builder().buildEmpty().setSiteName(getName()).setId(index.getAndIncrement());
                if (el.is("h1")) {
                    String title = "<h1>" + el.text() + "</h1>";
                    newItem.setTitle(title);
                }
                if (el.is("p.teaser-text")) {
                    String title1 = "<h3>" + el.text() + "</h3>";
                    newItem.setText(title1);
                }
                if (el.is("img.lq-img")) {
                    String image = el.outerHtml();
                    System.out.println("Image found : " + image);
                    newItem.value(image);
                }
                if (el.is("div.rich-text p")) {
                    String text = "<p>" + el.text() + "</p>";
                    newItem.setText(text);
                }
                if (el.is("iframe")) {
                    newItem.setVideo(el.outerHtml());
                }
                if (el.is("li:contains(????????)")) {
                    newItem.setDateString(el.text());
                }
                articleMap.add(newItem);
            }
            return (T) getArticleMap();
        }
        @Override
        public ArrayList<TagMap> getArticleMap() {
            return articleMap;
        }
        @Override
        public <T> T test(Document document, String pattern) {
            return (T) parseDoc(document);
        }
    };
    protected SiteDW(Builder builder) {
        super(builder);
        putSiteParser(ParserTypes.CATALOG, catalogParser);
        putSiteParser(ParserTypes.ARTICLE, pageParser);
        putSiteParser(ParserTypes.AGGREGATE, catalogParser);
        putSiteParser(ParserTypes.OPTIONAL, pageParserOld);

        putSiteView(ViewTypes.CATALOG_VIEW, catalogView);
        putSiteView(ViewTypes.PAGE_VIEW, pageView);
    }

    private SiteView catalogView = new SiteView() {
        @Override
        public <T> T view(ArrayList<TagMap> tagMap) {
//            Patcher.PatchDoc patcher = new Patcher().CreateDoc();
//            for(TagMap tm : tagMap){
//                String result = patcher.imgSrcPatch(tm.value().replace(".webp", ""), "small_image");
//                result += patcher.paragraphPath("catalog_title", tm.getText());
//                result += patcher.paragraphPath("", "<b>" + tm.getType() + "</b>");
//                result = patcher.divPatch(tm.key(), tm.getId(), "", tm.getSiteName(), result);
//                tm.setHtml(result);
//            }
            return (T) "";//SiteViews.CLEAR_VIEW_CATALOG.view(tagMap);
        }
    };
    private SiteView pageView = new SiteView() {
        @Override
        public <T> T view(ArrayList<TagMap> tagMap) {
//            Patcher.PatchDoc patcher = new Patcher().CreateDoc();
//            AtomicInteger indexer = new AtomicInteger(0);
//            String resultHtml = tagMap.stream().map((e)->{
//
//                StringBuilder html = new StringBuilder();
//                if(e.getTagName().equals("header")){
//                    String header = "<h2>" + e.getText().toUpperCase(Locale.ROOT) + "</h2><p>";
//                    html.append(header);
//                }else if(e.getTagName().equals("image")){
//                    String img = patcher.imgSrcPatch(e.key().replace(".webp", ""), e.value(),"bbc-image");
//                    String link = patcher.linkPath(e.key() + "?site_name=" + e.getSiteName()
//                            + "&view_type=image&id=" + (indexer.getAndIncrement()) + "&navigate_param=base&", "", img);
//                    html.append(link);
//                }else if(e.getTagName().equals("text")){
//                    String text = patcher.paragraphPath("", e.getText());
//                    html.append(text);
//                }else if(e.getTagName().equals("video")){
//                    html.append(e.getVideo());
//                }
//                return html.toString();
//
//            }).reduce((s1, s2)->s1+s2).orElse("");
//
//            //System.out.println(resultHtml);
//            tagMap.get(0).setHtml(resultHtml);

            return (T) "";//SiteViews.CLEAR_VIEW_PAGE.view(tagMap);
        }
    };


    public static class Builder extends SiteBuilder.Builder<Builder> {

        @Override
        public SiteBuilder build() {
            return new SiteDW(this);
        }

        @Override
        public Builder self() {
            return this;
        }
    }

}
