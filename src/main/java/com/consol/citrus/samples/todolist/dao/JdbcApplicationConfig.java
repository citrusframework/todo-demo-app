/*
 * Copyright 2006-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.samples.todolist.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerAcl;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author Christoph Deppisch
 */
@Configuration
@Profile("jdbc")
public class JdbcApplicationConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server database() {
        Server database = new Server();
        try {
            database.setProperties(HsqlProperties.delimitedArgPairsToProps("server.database.0=file:target/testdb;server.dbname.0=testdb;server.remote_open=true;hsqldb.reconfig_logging=false", "=", ";", null));
        } catch (IOException | ServerAcl.AclFormatException e) {
            throw new BeanCreationException("Failed to create embedded database storage", e);
        }
        return database;
    }

    @Bean(destroyMethod = "close")
    @DependsOn("database")
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUrl("jdbc:hsqldb:hsql://localhost/testdb");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        return dataSource;
    }
}
