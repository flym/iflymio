package ${mapperPackageName};

<#list mapperImportSet as imp>
import ${imp};
</#list>

/**
 * 处理${table.tableComment!''}的数据操作
 * Created by ${author} at ${.now?string("yyyy-MM-dd")}
 */
public interface ${table.mapperName} extends Mapper<${table.className}> {
}
