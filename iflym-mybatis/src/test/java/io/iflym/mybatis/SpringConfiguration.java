package io.iflym.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.iflym.mybatis.domain.field.json.JsonedMapperFactory;
import io.iflym.mybatis.mybatis.ext.ConfigurationExt;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * 构建起测试环境
 * Created by flym on 2017/11/8.
 */
@Configuration
@ComponentScan("io.iflym.mybatis.**")
public class SpringConfiguration {

    @Bean(initMethod = "init", destroyMethod = "close")
    public DruidDataSource dataSource(@Value("${db.url}") String url, @Value("${db.username}") String username, @Value("${db.password}") String password) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setConfiguration(new ConfigurationExt());

        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public static MapperScannerConfigurer mapperScanner() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionTemplateBeanName("sqlSessionTemplate");
        mapperScannerConfigurer.setBasePackage("io.iflym.mybatis.**.mapper");
        return mapperScannerConfigurer;
    }

    @PostConstruct
    private void init() {
        //用于在某个地方注册此对象
        JsonedMapperFactory.INSTANCE.registerDefaultJacksonJsonedMapper(new ObjectMapper());
    }
}
