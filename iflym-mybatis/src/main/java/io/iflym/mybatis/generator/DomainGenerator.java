package io.iflym.mybatis.generator;


import com.google.common.collect.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用于生成相应的实体类对象
 *
 * @author flym
 */
@Slf4j
@Setter
public class DomainGenerator {
    private static final Set<String> DOMAIN_IMPORT_SET_DEFAULT = Sets.newTreeSet();
    private static final Set<String> MAPPER_IMPORT_SET_DEFAULT = Sets.newTreeSet();

    static {
        DOMAIN_IMPORT_SET_DEFAULT.add("io.iflym.mybatis.domain.Entity");
        DOMAIN_IMPORT_SET_DEFAULT.add("io.iflym.mybatis.domain.Key");
        DOMAIN_IMPORT_SET_DEFAULT.add("io.iflym.mybatis.domain.annotation.Column");
        DOMAIN_IMPORT_SET_DEFAULT.add("io.iflym.mybatis.domain.annotation.Id");
        DOMAIN_IMPORT_SET_DEFAULT.add("io.iflym.mybatis.domain.annotation.Table");
        DOMAIN_IMPORT_SET_DEFAULT.add("lombok.Getter");
        DOMAIN_IMPORT_SET_DEFAULT.add("lombok.NoArgsConstructor");
        DOMAIN_IMPORT_SET_DEFAULT.add("lombok.Setter");
        DOMAIN_IMPORT_SET_DEFAULT.add("org.apache.ibatis.type.JdbcType");

        MAPPER_IMPORT_SET_DEFAULT.add("io.iflym.mybatis.mapperx.Mapper");
    }

    /** 访问数据源 */
    private DataSource dataSource;

    /** 数据表目录 */
    private String catalog;

    /** 数据表模式 */
    private String schema;

    /** 模板处理器 */
    private DomainTemplateProcessor domainTemplateProcessor = DomainTemplateProcessor.getDefaultProcessor();

    /** 路径定位 */
    private PathLocator pathLocator = PathLocator.defaultInstance();

    /** 当原文件存在时是否替换原文件 */
    private boolean replace = false;

    /** 是否生成domain类 */
    private boolean generateDomain = true;

    /** 是否生成mapper类 */
    private boolean generateMapper = true;

    /** 相应的处理的表以及相应的类 */
    private Map<String, String> tableMap = ImmutableMap.of();

    /** 相应的父级包名 */
    private String packageName;

    /** 当前模块路径 */
    private String modulePath;

    /** 全局排除的列 */
    private Set<String> globalExcludeColumnSet = ImmutableSet.of();

    /** 作者 */
    private String author = "flym";

    private void check() {
        Objects.requireNonNull(dataSource, "必须提供数据源");
        Objects.requireNonNull(packageName, "必须提供待生成类的包名");
        Objects.requireNonNull(modulePath, "必须提供当前模块所在的路径地址");
    }

    private void doGenerate(Connection connection, String table, String javaClassName) throws Exception {
        val domainPackageName = pathLocator.domainPackageName(packageName);
        val domainPath = pathLocator.domainPath(modulePath, domainPackageName);

        DatabaseMetaData databaseMetaData = connection.getMetaData();

        Map<String, Object> map = Maps.newHashMap();
        map.put("domainPackageName", domainPackageName);
        map.put("author", author);
        Set<String> domainImportSet = Sets.newTreeSet(DOMAIN_IMPORT_SET_DEFAULT);
        map.put("domainImportSet", domainImportSet);

        //表信息
        Table tableInfo;
        try(ResultSet tableResultSet = databaseMetaData.getTables(catalog, schema, table, null)){
            tableResultSet.next();
            tableInfo = Table.build(tableResultSet, table, javaClassName);
            map.put("table", tableInfo);
        }

        //列信息
        Map<String, Column> columnMap = Maps.newLinkedHashMap();
        try(ResultSet resultSet = databaseMetaData.getColumns(catalog, schema, table, null)){
            while(resultSet.next()) {
                Column column = Column.build(resultSet);
                if(globalExcludeColumnSet.contains(column.getColumnName())) {
                    continue;
                }

                columnMap.put(column.getColumnName(), column);
            }
        }
        map.put("columnMap", columnMap);

        //修正列的显示长度
        try(ResultSet resultSet = connection.createStatement().executeQuery("select * from " + table + " where 1 = 0")){
            val meta = resultSet.getMetaData();
            int columnCount = meta.getColumnCount();
            for(int i = 1; i <= columnCount; i++) {
                String columnName = meta.getColumnLabel(i).toLowerCase();
                int displaySize = meta.getColumnDisplaySize(i);
                val column = columnMap.get(columnName);
                if(column != null) {
                    column.setDisplaySize(displaySize);
                    column.reCalcPropertyType();
                }
            }
        }

        //额外的类型引入
        columnMap.values().stream().map(Column::getPropertyType)
                .filter(t -> !t.isPrimitive())
                .filter(t -> !t.getName().startsWith("java.lang"))
                .forEach(t -> domainImportSet.add(t.getName()));

        //索引信息
        List<Index> indexList = Lists.newArrayList();
        try(ResultSet resultSet = databaseMetaData.getPrimaryKeys(catalog, schema, table)){
            while(resultSet.next()) {
                Index index = Index.build(resultSet);
                indexList.add(index);
                columnMap.get(index.getColumnName()).setKey(true);
            }
        }
        indexList.sort(Index::compareTo);
        map.put("indexList", indexList);
        map.put("indexSequence", indexList.stream().map(Index::getPropertyName).collect(Collectors.joining(", ")));

        generateFile(domainPath, javaClassName, ".java", "domain.ftl", map, replace);

        val mapperPackageName = pathLocator.mapperPackageName(packageName);
        val mapperPath = pathLocator.mapperPath(modulePath, mapperPackageName);

        map.put("mapperPackageName", mapperPackageName);

        Set<String> mapperImportSet = Sets.newTreeSet(MAPPER_IMPORT_SET_DEFAULT);
        //追加对domain类的引用
        mapperImportSet.add(domainPackageName + "." + tableInfo.getClassName());
        map.put("mapperImportSet", mapperImportSet);

        generateFile(mapperPath, tableInfo.getMapperName(), ".java", "mapper.ftl", map, replace);

        val mapperXmlPath = pathLocator.mapperXmlPath(modulePath, mapperPackageName);
        generateFile(mapperXmlPath, tableInfo.getMapperName(), ".xml", "mapperXml.ftl", map, replace);
    }

    private void generateFile(String path, String fileName, String postfix, String templateName, Map<String, Object> model, boolean replace) throws IOException {
        Files.createDirectories(Paths.get(path));

        File file = new File(path, fileName + postfix);
        if(file.exists() && !replace) {
            return;
        }

        String body = domainTemplateProcessor.generate(templateName, model);

        FileWriter fileWriter = new FileWriter(file);

        fileWriter.write(body);
        fileWriter.close();
    }

    public void generate() throws Exception {
        check();

        try(Connection connection = dataSource.getConnection()){
            for(Map.Entry<String, String> e : tableMap.entrySet()) {
                doGenerate(connection, e.getKey(), e.getValue());
                log.info("完成生成->{}", e.getKey());
            }
        }
    }
}
