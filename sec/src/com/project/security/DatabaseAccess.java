package com.project.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

/**
 * [추가] SQL Injection 방지를 위해 PreparedStatement 사용 예시를 제공한다.
 */
public final class DatabaseAccess {
    private DatabaseAccess() {
    }

    public static <T> T findUser(Connection connection, String userId,
                                  Function<ResultSet, T> mapper) throws SQLException {
        String sql = "SELECT id, password_hash, password_salt, role FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? mapper.apply(result) : null;
            }
        }
    }
}
