package com.roshan.dbdiff.service;

import com.roshan.dbdiff.mapper.DatabaseMapper;
import com.roshan.dbdiff.mapper.TableMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

@Getter
public class ConnectionManager {
    private SqlSession src;
    private SqlSession tar;
    private HikariDataSource dataSource;

    public void init(String srcUrl, String srcUsername, String srcPassword, String srcDatabase,
                     String tarUrl, String tarUsername, String tarPassword, String tarDatabase) {
        this.src = this.connect(srcUrl, srcUsername, srcPassword, srcDatabase);
        this.tar = this.connect(tarUrl, tarUsername, tarPassword, tarDatabase);
    }

    private SqlSession connect(String url, String username, String password, String database) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(2);
        HikariDataSource dataSource = new HikariDataSource(config);
        this.dataSource = dataSource;
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(DatabaseMapper.class);
        configuration.addMapper(TableMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        return sqlSessionFactory.openSession();
    }

    public void clear() {
        this.src.close();
        this.tar.close();
        dataSource.close();
    }
}
