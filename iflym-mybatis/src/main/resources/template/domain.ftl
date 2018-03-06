package ${domainPackageName};

<#list domainImportSet as imp>
import ${imp};
</#list>

/**
 * ${table.tableComment!''}
 * created by ${author} at ${.now?string("yyyy-MM-dd")}
 */
@Table(name = "${table.tableName}")
@Getter
@Setter
@NoArgsConstructor
public class ${table.className} implements Entity {
    <#list columnMap?values as column>
<#if column.key>
    @Id
</#if>
    @Column(comment = "${column.columnComment}", type = JdbcType.${column.columnMybatisType.name()})
    private ${column.propertyType.simpleName} ${column.propertyName};

    </#list>
	@Override
	public Key key() {
		return Key.of(${indexSequence});
	}
}