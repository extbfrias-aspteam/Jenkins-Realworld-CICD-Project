package net.cero.ahorro.common;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class JDBCUtil {
    public static ResultSet executePreparedStatementWithParams(PreparedStatement ps, List<SqlQueryParams> params) throws SQLException {
        setPsParams(ps, params.toArray(new SqlQueryParams[]{}));
        ps.execute();
        return ps.getResultSet();
    }

    public static ResultSet executePreparedStatement(PreparedStatement ps) throws SQLException {
        ps.execute();
        return ps.getResultSet();
    }

    private static void setPsParams(PreparedStatement ps, SqlQueryParams[] params) throws SQLException {
        if(Objects.nonNull(params)){
            for (int i = 0; i< params.length; i++){
                ps.setObject(i+1, params[i].getValue(), params[i].getType());
            }
        }
    }

    public static PreparedStatement getPreparedStatementFromDs(String query, DataSource dataSource) throws SQLException {
        return dataSource.getConnection().prepareStatement(query);
    }
}
