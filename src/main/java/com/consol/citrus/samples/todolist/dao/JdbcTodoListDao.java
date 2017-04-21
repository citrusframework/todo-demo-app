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

import com.consol.citrus.samples.todolist.model.TodoEntry;
import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * @author Christoph Deppisch
 */
public class JdbcTodoListDao implements TodoListDao {

    private final DataSource dataSource;

    public JdbcTodoListDao() {
        dataSource = createDataSource();
    }

    private DataSource createDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUrl("jdbc:hsqldb:hsql://localhost/testdb");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        return dataSource;
    }

    @Override
    public void save(TodoEntry entry) {
        try {
            Connection connection = getConnection();
            try {
                connection.setAutoCommit(true);
                PreparedStatement statement = connection.prepareStatement("INSERT INTO todo_entries (id, title, description, done) VALUES (?, ?, ?, ?)");
                try {
                    statement.setString(1, getNextId());
                    statement.setString(2, entry.getTitle());
                    statement.setString(3, entry.getDescription());
                    statement.setBoolean(4, entry.isDone());
                    statement.executeUpdate();
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not save entry " + entry, e);
        }
    }

    private String getNextId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public List<TodoEntry> list() {
        try {
            Connection connection = getConnection();
            try {
                Statement statement = connection.createStatement();
                try {
                    ResultSet resultSet = statement.executeQuery("SELECT id, title, description FROM todo_entries");
                    try {
                        List<TodoEntry> list = new ArrayList<>();
                        while (resultSet.next()) {
                            String id = resultSet.getString(1);
                            String title = resultSet.getString(2);
                            String description = resultSet.getString(3);
                            list.add(new TodoEntry(UUID.fromString(id), title, description));
                        }
                        return list;
                    } finally {
                        resultSet.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not list entries", e);
        }
    }

    @Override
    public void delete(TodoEntry entry) {
        try {
            Connection connection = getConnection();
            try {
                connection.setAutoCommit(true);
                PreparedStatement statement = connection.prepareStatement("DELETE FROM todo_entries WHERE id = ?");
                try {
                    statement.setString(1, entry.getId().toString());
                    statement.executeUpdate();
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not delete entries for title " + entry.getId().toString(), e);
        }
    }

    @Override
    public void deleteAll() {
        try {
            Connection connection = getConnection();
            try {
                Statement statement = connection.createStatement();
                try {
                    statement.executeQuery("DELETE FROM todo_entries");
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not delete entries", e);
        }
    }

    @Override
    public void update(TodoEntry entry) {
        try {
            Connection connection = getConnection();
            try {
                connection.setAutoCommit(true);
                PreparedStatement statement = connection.prepareStatement("UPDATE todo_entries SET (title, description, done) VALUES (?, ?, ?) WHERE id = ?");
                try {
                    statement.setString(1, entry.getTitle());
                    statement.setString(2, entry.getDescription());
                    statement.setBoolean(3, entry.isDone());
                    statement.setString(4, entry.getId().toString());
                    statement.executeUpdate();
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not update entry " + entry, e);
        }
    }

    public Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    private DataSource getDataSource() {
        return dataSource;
    }
}
