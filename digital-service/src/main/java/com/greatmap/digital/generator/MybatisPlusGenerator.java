package com.greatmap.digital.generator;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @author greatmap
 * @desp  代码生成器
 */
public class MybatisPlusGenerator {

    public static void main(String[] args) {

        // 全局配置
        GlobalConfig globalConfig = globalConfig();

        // 数据源配置
        DataSourceConfig dataSourceConfig = dataSourceConfig();

        // 策略配置
        StrategyConfig strategyConfig = strategyConfig();

        // 包名策略配置
        PackageConfig packageConfig = packageConfig();

        // 整合配置
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(globalConfig);
        autoGenerator.setDataSource(dataSourceConfig);
        autoGenerator.setStrategy(strategyConfig);
        autoGenerator.setPackageInfo(packageConfig);

        autoGenerator.execute();

    }

    /**
     * 全局配置
     *
     * @return
     */
    private static GlobalConfig globalConfig() {
        GlobalConfig config = new GlobalConfig();
        // 是否开启AR模式
        config.setActiveRecord(false)
                // 作者
                .setAuthor("gaorui")
                //生成路径
                .setOutputDir("E:/a")
                //文件是否覆盖
                .setFileOverride(true)
                // 主键策略
                .setIdType(IdType.ID_WORKER)
                //默认情况下生成的Service接口的名字首字母都带有I
                .setServiceName("%sService")
                // 是否生成基本的sql中的ResultMap
                .setBaseResultMap(true)
                // 是否生成基本的sql列
                .setBaseColumnList(true);
        return config;
    }

    /**
     * 数据源配置
     *
     * @return
     */
    private static DataSourceConfig dataSourceConfig() {
        DataSourceConfig dsConfig = new DataSourceConfig();
        // 设置数据库类型
        dsConfig.setDbType(DbType.ORACLE);
        dsConfig.setDriverName("oracle.jdbc.OracleDriver");
        dsConfig.setUrl("jdbc:oracle:thin:@39.98.176.209:1521/orcl");
        dsConfig.setUsername("dc");
        dsConfig.setPassword("dc");
        return dsConfig;
    }

    /**
     * 策略配置
     * @return
     */
    private static StrategyConfig strategyConfig() {
        StrategyConfig strategyConfig = new StrategyConfig();
        // 从数据库表到文件的命名策略
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        return strategyConfig;
    }

    /**
     * 包名策略配置
     * @return
     */
    private static PackageConfig packageConfig() {
        PackageConfig packageConfig = new PackageConfig();
        // 所需要生成的包下
        packageConfig.setParent("com.greatmap.digital");
        // 设置实体生成包名
        packageConfig.setEntity("pojo");
        // 设置控制层包名，以下以此类推
        packageConfig.setController("controller");
        packageConfig.setService("service");
        packageConfig.setMapper("mapper");
        packageConfig.setXml("mapper.xml");
        return packageConfig;
    }
}
