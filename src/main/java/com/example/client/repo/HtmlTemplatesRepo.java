package com.example.client.repo;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class HtmlTemplatesRepo {

    private final String htmlTemplatesRepoPath;
    private final Map<String, String> templates = new HashMap<>();

    public  HtmlTemplatesRepo(){
        htmlTemplatesRepoPath = System.getProperty("user.dir") + "\\src\\main\\resources\\templates\\html-templates";
        System.out.println("Html templates path : " + htmlTemplatesRepoPath);
        loadTemplateData("ConflictResolveTemplates.html");
    }


    public String getTemplate(String templateName, Function<String, String> post){
        String template = Optional.ofNullable(templates.get(templateName)).orElseThrow(NullPointerException::new);
        return post.apply(template);
    }

    public String loadTemplateData( String fileName){
        Elements elements = loadTemplateDatafile(fileName).getElementsByTag("template-data");
        for(Element el : elements){
            templates.put(el.attr("id"), el.html());
            System.out.println(templates.get(el.attr("id")));
        }
        return "";
    }

    @SneakyThrows
    private Document loadTemplateDatafile(String filename){
        String templatePath = htmlTemplatesRepoPath + "\\" + filename;
        File template = new File(templatePath);
        Document document = Jsoup.parse(template);
        return Optional.ofNullable(document).orElseThrow(NullPointerException::new);
    }

}
