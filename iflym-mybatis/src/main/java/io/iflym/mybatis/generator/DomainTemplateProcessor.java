package io.iflym.mybatis.generator;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.Getter;
import lombok.val;

import java.io.IOException;
import java.io.StringWriter;

/**
 * FreeMaker模板解析
 * Created by flym on 5/24/2017.
 *
 * @author flym
 */
public class DomainTemplateProcessor {
    @Getter
    public static DomainTemplateProcessor defaultProcessor = new DomainTemplateProcessor();

    private static final String ENCODING = "UTF-8";
    private static final String TEMPLATE_PATH_DEFAULT = "/template";
    /** 模板路径 */
    private final String templatePath;
    private Configuration configuration;

    private DomainTemplateProcessor() {
        this(TEMPLATE_PATH_DEFAULT);
    }

    public DomainTemplateProcessor(String templatePath) {
        this.templatePath = templatePath;
        init(this.getClass().getClassLoader());
    }

    private void init(ClassLoader classLoader) {
        val templateLoader = new ClassTemplateLoader(classLoader, templatePath);

        configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding(ENCODING);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        configuration.setLogTemplateExceptions(true);
        configuration.setTemplateLoader(templateLoader);
    }

    /** 生成相应的内容信息 */
    public String generate(String templateName, Object model) {
        StringWriter writer = new StringWriter();
        try{
            Template template = configuration.getTemplate(templateName);
            template.process(model, writer);
        } catch(IOException | TemplateException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return writer.toString();
    }
}
