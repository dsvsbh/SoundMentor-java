package com.soundmentor.soundmentorweb.mybatis;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author make
 * <p>
 * 生成数据库表对应的 实体/Dao/Service/Controller
 */
@Slf4j
public class CodeGenerator {

    /**
     * 包含的表. 若空则表示该库中全部的表.
     */
    private static String[] tables = {
            "college_examination_paper_cert_rel",
    };

    /**
     * 排除的表.
     */
    private static String[] exTables = {
    };

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        log.debug("projectPath: " + projectPath);
        // 生成的文件输出目录
        gc.setOutputDir(projectPath + "/temp");
        // 类注释中 @author 的值
        gc.setAuthor("Make");
        // 生成的Dao命名格式: 表名+Dao
        gc.setMapperName("%sDao");
        // 生成的实体类命名格式: T+表名
        gc.setEntityName("%sDO");
        // 数据库日期/时间戳字段在java中映射的类
        gc.setDateType(DateType.TIME_PACK);
        // 是否覆盖之前生成的文件
        gc.setFileOverride(true);
        gc.setOpen(false);
        // 是否在Mapper文件中生成实体的映射
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        // 实体属性 Swagger2 注解
        gc.setSwagger2(false);

        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://rm-bp1sluyi88448we.mysql.rds.aliyuncs.com:3306/rpa_college?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("scheduler_user");
        dsc.setPassword("scheduler_pd");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        //
        pc.setParent("com.soundmentor.soundmentorbase");
        pc.setEntity("domain.entity");
        pc.setMapper("dao");
        pc.setService("service.basic");
        pc.setServiceImpl("service");
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setInclude(tables);
        if (ArrayUtils.isEmpty(tables)) {
            strategy.setExclude(exTables);
        }
        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix("t_");
        strategy.setEntityTableFieldAnnotationEnable(true);

        mpg.setStrategy(strategy);

        TemplateConfig tc = new TemplateConfig();
        mpg.setTemplate(tc);
        mpg.execute();
    }

}