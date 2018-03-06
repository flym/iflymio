package io.iflym.mybatis.generator;

/**
 * 定位相应生成时的各个位置
 * Created by flym on 2017/10/10.
 *
 * @author flym
 */
public interface PathLocator {
    /**
     * domain类的包名
     *
     * @param packageName 整个项目的包名,即domain前面的包名
     * @return 实际的生成domain所在的包名
     */
    String domainPackageName(String packageName);

    /**
     * domain文件的存储位置
     *
     * @param modulePath        整个项目模块所在的文件系统实际地址
     * @param domainPackageName 生成domain所在的包名
     * @return 实际domain文件的文件系统文件夹地址
     */
    String domainPath(String modulePath, String domainPackageName);

    /**
     * mapper类的包名
     *
     * @param packageName 整个项目的包名,即mapper前面的包名
     * @return 实际生成的mapper所在的包名
     */
    String mapperPackageName(String packageName);


    /**
     * mapper文件的存储位置
     *
     * @param modulePath        整个项目模块所在的文件系统实际地址
     * @param mapperPackageName 生成mapper所在的包名
     * @return 实际mapper文件的文件系统文件夹地址
     */
    String mapperPath(String modulePath, String mapperPackageName);

    /**
     * mapper Xml文件的存储位置
     *
     * @param modulePath        整个项目模块所在的文件系统实际地址
     * @param mapperPackageName 生成mapper所在的包名
     * @return 实际mapper的xml文件的文件系统文件夹地址
     */
    String mapperXmlPath(String modulePath, String mapperPackageName);

    /**
     * 获取默认实现此接口的实例
     *
     * @return 默认的实例对象
     */
    static PathLocator defaultInstance() {
        return DefaultPathLocator.INSTANCE;
    }
}
