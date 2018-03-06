package io.iflym.mybatis.generator;

import java.util.regex.Pattern;

/**
 * 默认实现路径定位
 *
 * @author flym
 * Created by flym on 2017/10/10.
 */
public class DefaultPathLocator implements PathLocator {
    public static final DefaultPathLocator INSTANCE = new DefaultPathLocator();

    private static final Pattern PATTERN_DOT = Pattern.compile("\\.");
    private static final String DOT_REPLACE = "/";

    @Override
    public String domainPackageName(String packageName) {
        return packageName + ".domain";
    }

    @Override
    public String domainPath(String modulePath, String domainPackageName) {
        return modulePath + "/src/main/java/" + PATTERN_DOT.matcher(domainPackageName).replaceAll(DOT_REPLACE);
    }

    @Override
    public String mapperPackageName(String packageName) {
        return packageName + ".mapper";
    }

    @Override
    public String mapperPath(String modulePath, String mapperPackageName) {
        return modulePath + "/src/main/java/" + PATTERN_DOT.matcher(mapperPackageName).replaceAll(DOT_REPLACE);
    }

    @Override
    public String mapperXmlPath(String modulePath, String mapperPackageName) {
        return modulePath + "/src/main/resources/" + PATTERN_DOT.matcher(mapperPackageName).replaceAll(DOT_REPLACE);
    }
}
